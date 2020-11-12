package cc.coopersoft.keycloak.callback.providers;

import cc.coopersoft.keycloak.callback.providers.spi.CallbackSenderService;
import cc.coopersoft.keycloak.callback.providers.spi.CallbackService;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.UserModel;

import java.util.Optional;

public class RocketmqCallbackProvider implements CallbackSenderService {
  private static final Logger logger = Logger.getLogger(RocketmqCallbackProvider.class);



  private static final String NAME_SERVER_ADDRESS_PARAM_NAME ="namesrv"; //"192.168.1.2:9876;192.168.1.3:9876"
  private static final String TOPIC_PARAM_NAME = "topic";
  private static final String REQUIRE_PARAM_NAME = "require";

  private static final String PRODUCER_GROUP_NAME = "keycloak-callback";
  private static final String TAGS = "registration";
  private static final String KEY = "id";

  private final DefaultMQProducer producer;
  private String topic;
  private boolean require;

  public RocketmqCallbackProvider(Config.Scope config) {


    topic = config.get(TOPIC_PARAM_NAME);

    require = Optional.ofNullable(config.get(REQUIRE_PARAM_NAME))
            .map(v -> !("false".equals(v.toLowerCase()) || "no".equals(v.toLowerCase())))
            .orElse(true);

    producer = new DefaultMQProducer(PRODUCER_GROUP_NAME);
    producer.setNamesrvAddr(config.get(NAME_SERVER_ADDRESS_PARAM_NAME));
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
    for (int i = 0; i < 10000000; i++)
      try {
        {
          Message msg = new Message(topic,// topic
                  TAGS,// tag
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
