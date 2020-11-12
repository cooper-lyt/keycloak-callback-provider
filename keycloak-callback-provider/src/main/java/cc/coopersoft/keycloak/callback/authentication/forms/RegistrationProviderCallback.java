package cc.coopersoft.keycloak.callback.authentication.forms;

import cc.coopersoft.keycloak.callback.providers.spi.CallbackService;
import org.keycloak.Config;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.validation.Validation;

import java.util.*;
import java.util.stream.Collectors;

public class RegistrationProviderCallback implements FormAction, FormActionFactory {

  public static final String PROVIDER_ID = "registration-callback-provider";

  public static final String PROVIDER_PARAM_NAMES = "registration.callback.provider";

  private KeycloakSession session;

  private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

  @Override
  public String getDisplayType() {
    return "Registration provider callback";
  }

  @Override
  public String getReferenceCategory() {
    return null;
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  @Override
  public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
    return new AuthenticationExecutionModel.Requirement[0];
  }

  @Override
  public boolean isUserSetupAllowed() {
    return false;
  }

  @Override
  public void buildPage(FormContext formContext, LoginFormsProvider loginFormsProvider) {

  }

  @Override
  public void validate(ValidationContext validationContext) {

  }

  @Override
  public void success(FormContext context) {
    String providerId = context.getAuthenticatorConfig().getConfig().get(PROVIDER_PARAM_NAMES);
    if (!Validation.isBlank(providerId))
      session.getProvider(CallbackService.class,providerId).onRegistration(context.getUser());
  }

  @Override
  public boolean requiresUser() {
    return false;
  }

  @Override
  public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

  }

  @Override
  public String getHelpText() {
    return "Registration provider callback";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return configProperties;
  }

  @Override
  public FormAction create(KeycloakSession keycloakSession) {
    this.session = keycloakSession;
    return this;
  }

  @Override
  public void init(Config.Scope scope) {

    ProviderConfigProperty callbackType;
    callbackType = new ProviderConfigProperty();
    callbackType.setName(PROVIDER_PARAM_NAMES);
    callbackType.setLabel("Provider id");
    callbackType.setType(ProviderConfigProperty.LIST_TYPE);
    callbackType.setOptions(session.listProviderIds(CallbackService.class).stream().sorted().collect(Collectors.toList()));
    callbackType.setHelpText("Callback Provider id");

    configProperties.add(callbackType);

  }

  @Override
  public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

  }

  @Override
  public void close() {

  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }
}
