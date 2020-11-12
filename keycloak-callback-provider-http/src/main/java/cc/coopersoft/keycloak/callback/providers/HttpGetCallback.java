package cc.coopersoft.keycloak.callback.providers;

import cc.coopersoft.keycloak.callback.providers.spi.CallbackService;
import cc.coopersoft.keycloak.callback.providers.spi.CallbackServiceProviderFactory;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserModel;

import java.io.IOException;

public class HttpGetCallback implements CallbackService, CallbackServiceProviderFactory {

  private static final String URL_PARAM_NAME = "url";

  private OkHttpClient client;

  private Config.Scope scope;

  @Override
  public void onRegistration(UserModel user) {
    Request request = new Request.Builder()
            .url(scope.get(URL_PARAM_NAME) + user.getId())
            .build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful())
        throw new IllegalArgumentException("Unexpected code " + response);
    } catch (IOException e) {
      throw new IllegalArgumentException("connect fail");
    }


  }

  @Override
  public CallbackService create(KeycloakSession keycloakSession) {
    client = new OkHttpClient();
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
}