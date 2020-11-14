package cc.coopersoft.keycloak.callback.providers;

import cc.coopersoft.keycloak.callback.providers.spi.CallbackSenderService;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.logging.InternalLoggerFactory;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.common.RemotingUtil;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.UserModel;
import org.keycloak.models.RealmModel;
import org.keycloak.validation.Validator;

import java.util.Optional;

public class RocketmqCallbackProvider implements CallbackSenderService {
  private static final Logger logger = Logger.getLogger(RocketmqCallbackProvider.class);



  private static final String NAME_SERVER_ADDRESS_PARAM_NAME ="namesrv"; //"192.168.1.2:9876;192.168.1.3:9876"
  private static final String TOPIC_PARAM_NAME = "topic";
  private static final String REQUIRE_PARAM_NAME = "require";
  private static final String GROUP_PARAM_NAME ="group";
  private static final String TAGS_PARAM_NAME ="tags";
  private static final String ACCESS_KEY_PARAM_NAME = "access-key";
  private static final String SECRET_KEY_PARAM_NAME = "secret-key";

//  private static final String PRODUCER_GROUP_NAME = "keycloak-callback";
//  private static final String TAGS = "registration";
  private static final String KEY = "id";

  private DefaultMQProducer producer;
  private String topic;
  private String tags;
  private boolean require;

  private String getConfig(Config.Scope config, RealmModel realm, String paramName){
    return Optional.ofNullable(config.get(realm.getName().toUpperCase() + "_" + paramName)).orElse(config.get(paramName));
  }

  public RocketmqCallbackProvider(Config.Scope config, RealmModel realm) {

    topic = getConfig(config,realm,TOPIC_PARAM_NAME);
    tags = getConfig(config,realm,TAGS_PARAM_NAME);
    require = Optional.ofNullable(config.get(realm.getName().toUpperCase() + "_" + REQUIRE_PARAM_NAME))
            .map(v -> !("false".equals(v.toLowerCase()) || "no".equals(v.toLowerCase())))
            .orElse(true);

    Optional.ofNullable(config.get(ACCESS_KEY_PARAM_NAME))
            .ifPresentOrElse(
                    k -> producer = new DefaultMQProducer(getConfig(config,realm,GROUP_PARAM_NAME),
                      new AclClientRPCHook(new SessionCredentials(k, config.get(SECRET_KEY_PARAM_NAME))))
                    ,
                    () -> producer = new DefaultMQProducer(getConfig(config,realm,GROUP_PARAM_NAME)));

    producer = new DefaultMQProducer(getConfig(config,realm,GROUP_PARAM_NAME),
            new AclClientRPCHook(new SessionCredentials("YOUR ACCESS KEY", "YOUR SECRET KEY")));
    producer.setNamesrvAddr(getConfig(config,realm,NAME_SERVER_ADDRESS_PARAM_NAME));
    try {
      producer.start();
    } catch (MQClientException e) {
      if (require) {
        throw new IllegalArgumentException("producer start fail!", e);
      }else{
        logger.error("rockmq producer start fail!" , e);
      }
    }
  }


  @Override
  public void close() {
    producer.shutdown();
  }


  @Override
  public void registrationCallback(UserModel user) {

      try {
        {
          Message msg = new Message(topic,// topic
                  tags,// tag
                  KEY,// key
                  user.getId().getBytes(RemotingHelper.DEFAULT_CHARSET));// body
          SendResult sendResult = producer.send(msg);
          if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())){
            logger.debug("message is send ok " );
          }else if (require){
            throw new IllegalArgumentException("send message fail ! status:" + sendResult.getSendStatus());
          }else{
            logger.error("send message fail ! status:" + sendResult.getSendStatus());
          }

        }

      } catch (Exception e) {
        if (require) {
          throw new IllegalArgumentException("send message fail!", e);
        }else {
          logger.error("rockmq send message fail!" , e);
        }
      }
  }
}
