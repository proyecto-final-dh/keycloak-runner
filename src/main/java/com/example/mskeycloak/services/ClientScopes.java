package com.example.mskeycloak.services;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientScopeRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ClientScopes {
    private Keycloak keycloak;

    public ClientScopeRepresentation createClientScope(String name, String realm){
        try{
            List<ProtocolMapperRepresentation> protocolMapperRepresentations = new ArrayList<>();
            ProtocolMapperRepresentation protocolMapper = new ProtocolMapperRepresentation();
            protocolMapper.setProtocolMapper("oidc-group-membership-mapper");
            protocolMapper.setProtocol("openid-connect");
            protocolMapper.setName(name);
            Map<String, String> config = new HashMap<>();
            config.put("access.token.claim", "true");
            config.put("claim.name", name);
            config.put("full.path", "true");
            config.put("id.token.claim", "true");
            config.put("userinfo.token.claim", "true");
            protocolMapper.setConfig(config);

            protocolMapperRepresentations.add(protocolMapper);

            Map<String, String> attributes = new HashMap<>();
            attributes.put("consent.screen.text", "");
            attributes.put("gui.order", "");
            attributes.put("display.on.consent.screen", "true");
            attributes.put("include.in.token.scope", "true");
            ClientScopeRepresentation client = new ClientScopeRepresentation();
            client.setAttributes(attributes);
            client.setName(name);
            client.setDescription(name);
            client.setProtocolMappers(protocolMapperRepresentations);
            client.setProtocol("openid-connect");


            keycloak.realm(realm).clientScopes().create(client);

            return client;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

}
