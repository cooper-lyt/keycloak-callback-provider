package cc.coopersoft.keycloak.callback.providers;

import cc.coopersoft.keycloak.callback.providers.spi.CallbackSenderService;
import cc.coopersoft.keycloak.callback.providers.spi.CallbackSenderServiceProviderFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserModel;
import org.keycloak.models.RealmModel;

import java.io.IOException;
import java.util.Optional;

public class HttpGetCallback implements CallbackSenderService, CallbackSenderServiceProviderFactory {

  private static final String URL_PARAM_NAME = "URL";

  private OkHttpClient client;

  private Config.Scope scope;
  private RealmModel realm;

  @Override
  public CallbackSenderService create(KeycloakSession keycloakSession) {
    client = new OkHttpClient();
    realm = keycloakSession.getContext().getRealm();
    return this;
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
    return "http-get";
  }

  @Override
  public void registrationCallback(UserModel user) {
    String url = Optional.ofNullable(scope.get(realm.getName().toUpperCase() + "_" + URL_PARAM_NAME))
            .orElse(scope.get(URL_PARAM_NAME));

    Request request = new Request.Builder()
            .url(url + user.getId())
            .build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful())
        throw new IllegalArgumentException("Unexpected code " + response);
    } catch (IOException e) {
      throw new IllegalArgumentException("connect fail");
    }
  }
}
