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

import java.util.*;

public class RegistrationProviderCallback implements FormAction, FormActionFactory {

  public static final String PROVIDER_ID = "registration-callback-provider";

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
    return false;
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

    context.getSession().getProvider(CallbackService.class).onRegistration(context.getUser());
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
    return null;
  }

  @Override
  public FormAction create(KeycloakSession keycloakSession) {
    return this;
  }

  @Override
  public void init(Config.Scope scope) {



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
