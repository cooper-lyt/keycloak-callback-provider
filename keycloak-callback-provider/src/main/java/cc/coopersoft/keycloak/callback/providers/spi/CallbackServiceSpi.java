package cc.coopersoft.keycloak.callback.providers.spi;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

public class CallbackServiceSpi implements Spi {
  @Override
  public boolean isInternal() {
    return false;
  }

  @Override
  public String getName() {
    return "callbackService";
  }

  @Override
  public Class<? extends Provider> getProviderClass() {
    return CallbackService.class;
  }

  @Override
  public Class<? extends ProviderFactory> getProviderFactoryClass() {
    return CallbackServiceProviderFactory.class;
  }
}
