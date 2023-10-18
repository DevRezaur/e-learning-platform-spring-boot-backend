package com.devrezaur.user.service.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KeycloakConfiguration {

	@Value("${keycloak.server-url}")
	private String serverUrl;

	@Value("${keycloak.realm-name}")
	private String realmName;

	@Value("${keycloak.admin-cli-client-id}")
	private String adminCliClientId;

	@Value("${keycloak.admin-cli-client-secret}")
	private String adminCliClientSecret;

	@Bean
	public Keycloak keycloak() {
		return KeycloakBuilder
				.builder()
				.serverUrl(serverUrl)
				.realm(realmName)
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
				.clientId(adminCliClientId)
				.clientSecret(adminCliClientSecret)
				.build();
	}
}
