package com.example.mskeycloak.services;

import com.example.mskeycloak.models.User;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;

import javax.ws.rs.core.Response;

@AllArgsConstructor
public class Users {

    private Keycloak keycloak;

    public String createUser(User newUser, String realm) {
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setEnabled(true);
        keycloakUser.setId(newUser.getId());
        keycloakUser.setEmail(newUser.getEmail());
        keycloakUser.setUsername(newUser.getUsername());
        keycloakUser.setFirstName(newUser.getFirstName());
        keycloakUser.setLastName(newUser.getLastName());
        keycloakUser.setGroups(newUser.getGroups());

        try{
            UsersResource usersResource = keycloak.realm(realm).users();
            Response response = usersResource.create(keycloakUser);
            return CreatedResponseUtil.getCreatedId(response);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void setPassword(String userId, String password, UsersResource usersResource){
        try{
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(password);

            UserResource userResource = usersResource.get(userId);

            userResource.resetPassword(credentialRepresentation);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void setGroup(String userId, String groupName, RealmResource realmResource, UsersResource usersResource){
        try{
            GroupRepresentation group = realmResource.getGroupByPath(groupName);
            UserResource userResource = usersResource.get(userId);
            userResource.groups().add(group);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
