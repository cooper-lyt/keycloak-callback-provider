FROM jboss/keycloak:10.0.2

WORKDIR /tmp

RUN curl -fSL https://repo1.maven.org/maven2/com/alibaba/rocketmq/rocketmq-client/3.6.2.Final/rocketmq-client-3.6.2.Final.jar -o rocketmq-client-3.6.2.Final.jar

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



