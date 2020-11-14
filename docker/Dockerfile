FROM jboss/keycloak:11.0.3

WORKDIR /tmp

RUN curl -fSL https://repo1.maven.org/maven2/org/apache/rocketmq/rocketmq-client/4.5.2/rocketmq-client-4.5.2.jar -o rocketmq-client-4.5.2.jar \
  && curl -fSL https://repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar -o commons-lang3-3.4.jar \
  && curl -fSL https://repo1.maven.org/maven2/org/apache/rocketmq/rocketmq-remoting/4.5.2/rocketmq-remoting-4.5.2.jar -o rocketmq-remoting-4.5.2.jar \
  && curl -fSL https://repo1.maven.org/maven2/com/alibaba/fastjson/1.2.51/fastjson-1.2.51.jar -o fastjson-1.2.51.jar \
  && curl -fSL https://repo1.maven.org/maven2/org/apache/rocketmq/rocketmq-logging/4.5.2/rocketmq-logging-4.5.2.jar -o rocketmq-logging-4.5.2.jar \
  && curl -fSL https://repo1.maven.org/maven2/io/netty/netty-tcnative-boringssl-static/1.1.33.Fork26/netty-tcnative-boringssl-static-1.1.33.Fork26.jar -o netty-tcnative-boringssl-static-1.1.33.Fork26.jar \
  && curl -fSL https://repo1.maven.org/maven2/org/apache/rocketmq/rocketmq-common/4.5.2/rocketmq-common-4.5.2.jar -o rocketmq-common-4.5.2.jar \
  && curl -fSL https://repo1.maven.org/maven2/org/apache/rocketmq/rocketmq-srvutil/4.5.2/rocketmq-srvutil-4.5.2.jar -o rocketmq-srvutil-4.5.2.jar \
  && curl -fSL https://repo1.maven.org/maven2/org/apache/rocketmq/rocketmq-acl/4.5.2/rocketmq-acl-4.5.2.jar -o rocketmq-acl-4.5.2.jar

# yuntongxin provider need gson 2.8.6, keycloak 10.0.2 gson version is low need higher
#RUN curl -fSL https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar -o gson-2.8.6.jar

# main provider
COPY modules/keycloak-callback-provider/main/keycloak-callback-provider.jar .

#http provider
COPY modules/keycloak-callback-provider-http/main/keycloak-callback-provider-http.jar .

#rocketmq provider
COPY modules/keycloak-callback-provider-rocketmq/main/keycloak-callback-provider-rocketmq.jar .


COPY module-add.cli cli/
RUN $JBOSS_HOME/bin/jboss-cli.sh --file=cli/module-add.cli
RUN rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current



