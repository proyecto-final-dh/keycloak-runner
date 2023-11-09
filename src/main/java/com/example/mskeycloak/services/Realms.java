package com.example.mskeycloak.services;

import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;

@AllArgsConstructor
public class Realms {

    private Keycloak keycloak;

    public void createRealm(String realm){
        try{
            RealmRepresentation realmRepresentation = new RealmRepresentation();
            realmRepresentation.setRealm(realm);
            realmRepresentation.setEnabled(true);
            realmRepresentation.setRegistrationAllowed(true);
            keycloak.realms().create(realmRepresentation);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
