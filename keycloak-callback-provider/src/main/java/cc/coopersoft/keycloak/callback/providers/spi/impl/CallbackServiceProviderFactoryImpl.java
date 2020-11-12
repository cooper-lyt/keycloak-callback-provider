package cc.coopersoft.keycloak.callback.providers.spi.impl;

import cc.coopersoft.keycloak.callback.providers.spi.CallbackService;
import cc.coopersoft.keycloak.callback.providers.spi.CallbackServiceProviderFactory;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class CallbackServiceProviderFactoryImpl implements CallbackServiceProviderFactory {

  private Config.Scope scope;

  @Override
  public CallbackService create(KeycloakSession keycloakSession) {
    return new CallbackServiceImpl(keycloakSession,scope);
  }

  @Override
  public void init(Config.Scope scope) {
    this.scope = scope;
  }

  @Override
  public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

  }

  @Override
  public void close() {

  }

  @Override
  public String getId() {
    return "default";
  }
}
