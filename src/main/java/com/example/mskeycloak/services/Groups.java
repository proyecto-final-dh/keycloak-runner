package com.example.mskeycloak.services;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;

@AllArgsConstructor
public class Groups {

    private Keycloak keycloak;

    public void createGroup(String groupName, String realm){
        try{
            GroupRepresentation groupRepresentation = new GroupRepresentation();
            groupRepresentation.setName(groupName);
            keycloak.realm(realm).groups().add(groupRepresentation);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
