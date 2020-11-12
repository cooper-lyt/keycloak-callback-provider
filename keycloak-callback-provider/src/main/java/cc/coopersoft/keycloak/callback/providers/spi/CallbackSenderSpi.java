package cc.coopersoft.keycloak.callback.providers.spi;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class CallbackSenderSpi implements Spi {
  @Override
  public boolean isInternal() {
    return false;
  }

  @Override
  public String getName() {
    return "callbackSender";
  }

  @Override
  public Class<? extends Provider> getProviderClass() {
    return CallbackSenderService.class;
  }

  @Override
  public Class<? extends ProviderFactory> getProviderFactoryClass() {
    return CallbackSenderServiceProviderFactory.class;
  }
}
