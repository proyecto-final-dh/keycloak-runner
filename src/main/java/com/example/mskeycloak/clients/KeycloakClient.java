package com.example.mskeycloak.clients;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeycloakClient {

    public Keycloak buildClient(String serverUrl, String adminRealm, String username, String password, String adminClientId) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)
                .username(username)
                .password(password)
                .clientId(adminClientId)
                .build();
    }
}
