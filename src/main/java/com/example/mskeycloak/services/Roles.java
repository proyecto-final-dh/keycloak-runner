package com.example.mskeycloak.services;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@AllArgsConstructor
public class Roles {

    private Keycloak keycloak;

    public RoleRepresentation createRole(String roleName, String realm, String clientId){
        try {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            keycloak.realm(realm).clients().get(clientId).roles().create(role);
            return role;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void setRole(String roleName, String realm, String username, String clientId){
        try{
            UserRepresentation userRepresentation = keycloak.realm(realm).users().search(username).get(0);
            UserResource userResource = keycloak.realm(realm).users().get(userRepresentation.getId());
            RoleRepresentation role = keycloak.realm(realm).clients().get(clientId).roles().get(roleName).toRepresentation();
            userResource.roles().clientLevel(clientId).add(List.of(role));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
