package com.example.mskeycloak.services;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ClientScopeRepresentation;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Clients {

    private Keycloak keycloak;

    public String createClient(String clientName, String realm, String secret, String url, ClientScopeRepresentation groupsClientScope){
        try{

        List<String> webOrigins = new ArrayList<>();
        webOrigins.add(url + "*");
        webOrigins.add("http://localhost" + "*");

        List<String> all = new ArrayList<>();
        all.add("*");


        ClientRepresentation client = new ClientRepresentation();

        client.setClientId(clientName);
        client.setName(clientName);
        client.setDirectAccessGrantsEnabled(true);
        client.setServiceAccountsEnabled(true);
        client.setSecret(secret);
        client.setAdminUrl(url);
        client.setRootUrl(url);
        client.setWebOrigins(all);
//        client.setWebOrigins(webOrigins);
        client.setRedirectUris(webOrigins);
        client.setPublicClient(true);

        if(groupsClientScope != null){
            List<String> defaultClientScopesNames = new ArrayList<>();
            List<String> optionalClientScopesNames = new ArrayList<>();
            if(client.getDefaultClientScopes() != null){
                defaultClientScopesNames = client.getDefaultClientScopes();
            }
            if(client.getOptionalClientScopes() != null){
                optionalClientScopesNames = client.getOptionalClientScopes();
            }

            defaultClientScopesNames.add(groupsClientScope.getName());
            defaultClientScopesNames.add("web-origins");
            defaultClientScopesNames.add("acr");
            defaultClientScopesNames.add("profile");
            defaultClientScopesNames.add("roles");
            defaultClientScopesNames.add("email");

            optionalClientScopesNames.add("address");
            optionalClientScopesNames.add("phone");
            optionalClientScopesNames.add("offline_access");
            optionalClientScopesNames.add("microprofile-jwt");

            client.setDefaultClientScopes(defaultClientScopesNames);
            client.setOptionalClientScopes(optionalClientScopesNames);
        }

        Response response = keycloak.realm(realm).clients().create(client);

        return CreatedResponseUtil.getCreatedId(response);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
    }
}
