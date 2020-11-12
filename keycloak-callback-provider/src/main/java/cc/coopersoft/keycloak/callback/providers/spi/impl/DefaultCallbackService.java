package cc.coopersoft.keycloak.callback.providers.spi.impl;

import cc.coopersoft.keycloak.callback.providers.spi.CallbackSenderService;
import cc.coopersoft.keycloak.callback.providers.spi.CallbackService;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;

public class DefaultCallbackService implements CallbackService {

  private final KeycloakSession session;
  private final String service;

  public DefaultCallbackService(KeycloakSession session, Config.Scope config) {
    this.session = session;
    this.service = session.listProviderIds(CallbackSenderService.class)
            .stream().filter(s -> s.equals(config.get("service")))
            .findFirst().orElse(
                    session.listProviderIds(CallbackSenderService.class)
                            .stream().findFirst().orElse("")
            );
  }

  @Override
  public void onRegistration(UserModel user) {
    session.getProvider(CallbackSenderService.class, service).registrationCallback(user);
  }

  @Override
  public void close() {

  }
}
