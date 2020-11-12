package cc.coopersoft.keycloak.callback.providers.spi;

import org.keycloak.models.UserModel;
import org.keycloak.provider.Provider;

public interface CallbackSenderService extends Provider {

  void registrationCallback(UserModel user);
}
