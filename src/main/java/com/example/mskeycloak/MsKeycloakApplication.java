package com.example.mskeycloak;

import com.example.mskeycloak.clients.KeycloakClient;
import com.example.mskeycloak.models.User;
import com.example.mskeycloak.services.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientScopeRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class MsKeycloakApplication implements CommandLineRunner {

	@Value("${microservice.serverUrl}")
	private String microserviceUrl;
	@Value("${keycloak.serverUrl}")
	private String serverUrl;
	@Value("${keycloak.admin.realm}")
	private String adminRealm;
	@Value("${keycloak.username}")
	private String username;
	@Value("${keycloak.password}")
	private String password;
	@Value("${keycloak.admin.clientId}")
	private String adminClientId;
	@Value("${keycloak.defaultUrl}")
	private String defaultUrl;
	@Value("${keycloak.kc-resqpet-auth.fe-resqpet.client}")
	private String resqpetClient;
	@Value("${keycloak.kc-resqpet-auth.fe-resqpet.clientSecret}")
	private String resqpetClientSecret;
	@Value("${spring.application.name}")
	private String realmName;

	public static void main(String[] args) {
		SpringApplication.run(MsKeycloakApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		Constantes
		String RESQPET_REALM = realmName;
//		String RESQPET_REALM = "kc-resqpet-auth";
		String CLIENT_ROLE = "USER";
		String ADMIN_ROLE = "ADMIN";
		String REALM_MANAGEMENT_CLIENT = "realm-management";
		String VIEW_USERS_ROLE = "view-users";
		String GROUPS_LABEL = "groups";
//		String GROUPS_VALUE = "PROVIDERS";

//		Instancia del cliente de Keycloak
		KeycloakClient client = new KeycloakClient();
		Keycloak keycloak = client.buildClient(serverUrl, adminRealm, username, password, adminClientId);

//		Creación de reino
		Realms realms = new Realms(keycloak);
		realms.createRealm(RESQPET_REALM);

		RealmResource realmResource = keycloak.realm(RESQPET_REALM);
		UsersResource usersResource = realmResource.users();

		Clients clients = new Clients(keycloak);
		Roles roles = new Roles(keycloak);
		ClientScopes scopes = new ClientScopes(keycloak);
		Users users = new Users(keycloak);
//		Groups groups = new Groups(keycloak);

//		Creación de cliente scope para los grupos
		ClientScopeRepresentation groupsClientScope = scopes.createClientScope(GROUPS_LABEL, RESQPET_REALM);

//		Creación de los clientes
		String resqpetClientId = clients.createClient(resqpetClient, RESQPET_REALM,resqpetClientSecret, defaultUrl, groupsClientScope);

//		Creacion de los grupos y rol base
//		groups.createGroup(GROUPS_VALUE, RESQPET_REALM);
		roles.createRole(CLIENT_ROLE, RESQPET_REALM, resqpetClientId);
		roles.createRole(ADMIN_ROLE, RESQPET_REALM, resqpetClientId);

//		ClientRepresentation billsClient = keycloak.realm(RESQPET_REALM).clients().findByClientId("bills-ms").get(0);


//		Creación de usuarios

//		Modificación usuario por default para tener permisos de view-users
//		Obtenemos usuario default
		UserRepresentation serviceAccountResqpet = keycloak.realm(RESQPET_REALM).clients().get(resqpetClientId).getServiceAccountUser();
//		Obtenemos el Id del cliente de realm management
		String realmManagementClientId = keycloak.realm(RESQPET_REALM).clients().findByClientId(REALM_MANAGEMENT_CLIENT).get(0).getId();
//		Obtenemos el role de view-useres
		RoleRepresentation viewUsersRole = keycloak.realm(RESQPET_REALM).clients().get(realmManagementClientId).roles().get(VIEW_USERS_ROLE).toRepresentation();
//		Creamos el recurso de usuarios para el usuario que queremos modificar
		UserResource userResource = keycloak.realm(RESQPET_REALM).users().get(serviceAccountResqpet.getId());
//		Creamos el recurso de roles
		RoleScopeResource roleResource = userResource.roles().clientLevel(realmManagementClientId);
//		Adicionamos role de view-users y actualizamos usuario
		roleResource.add(Collections.singletonList(viewUsersRole));
		userResource.update(serviceAccountResqpet);

//		Creamos usuarios de prueba donde el usuario 1 es el unico que tendrá el grupo providers
		List<String> groupList = new ArrayList<>();
//		groupList.add(GROUPS_VALUE);

//		User adminUser = new User("","resqpet-admin", "Admin", "Admin", "", groupList);
//		User clientUser = new User("","monterrosaf", "Felipe", "Monterrosa", "", new ArrayList<>());

		User cesarUser = new User("cesar", "fierro");
		User ezequielUser = new User("ezequiel", "safdie");
		User javierUser = new User("javier", "triana");
		User nataliaUser = new User("natalia", "garcia");
		User danielUser = new User("daniel", "arcila");
		User felipeUser = new User("felipe", "monterrosa");
		User jesicaUser = new User("jesica", "munoz");
		User jessicaUser = new User("jessica", "ortiz");
		User juniorUser = new User("junior", "naranjo");
		User sofiaUser = new User("sofia", "munoz");
		User stefanyUser = new User("stefany", "salamanca");

//		String idUser1 = users.createUser(adminUser, RESQPET_REALM);
//		String idUser2 = users.createUser(clientUser, RESQPET_REALM);

		String idCesar = users.createUser(cesarUser, RESQPET_REALM);
		String idEzequiel = users.createUser(ezequielUser, RESQPET_REALM);
		String idJavier = users.createUser(javierUser, RESQPET_REALM);
		String idNatalia = users.createUser(nataliaUser, RESQPET_REALM);
		String idDaniel = users.createUser(danielUser, RESQPET_REALM);
		String idFelipe = users.createUser(felipeUser, RESQPET_REALM);
		String idJesica = users.createUser(jesicaUser, RESQPET_REALM);
		String idJessica = users.createUser(jessicaUser, RESQPET_REALM);
		String idJunior = users.createUser(juniorUser, RESQPET_REALM);
		String idSofia = users.createUser(sofiaUser, RESQPET_REALM);
		String idStefany = users.createUser(stefanyUser, RESQPET_REALM);

//		users.setPassword(idUser1, adminUser.getUsername(), usersResource);
//		users.setPassword(idUser2, clientUser.getUsername(), usersResource);

		users.setPassword(idCesar, cesarUser.getUsername(), usersResource);
		users.setPassword(idEzequiel, ezequielUser.getUsername(), usersResource);
		users.setPassword(idJavier, javierUser.getUsername(), usersResource);
		users.setPassword(idNatalia, nataliaUser.getUsername(), usersResource);
		users.setPassword(idDaniel, danielUser.getUsername(), usersResource);
		users.setPassword(idFelipe, felipeUser.getUsername(), usersResource);
		users.setPassword(idJesica, jesicaUser.getUsername(), usersResource);
		users.setPassword(idJessica, jessicaUser.getUsername(), usersResource);
		users.setPassword(idJunior, juniorUser.getUsername(), usersResource);
		users.setPassword(idSofia, sofiaUser.getUsername(), usersResource);
		users.setPassword(idStefany, stefanyUser.getUsername(), usersResource);

//		roles.setRole(CLIENT_ROLE, RESQPET_REALM, adminUser.getUsername(), resqpetClientId);
//		roles.setRole(CLIENT_ROLE, RESQPET_REALM, clientUser.getUsername(), resqpetClientId);

		roles.setRole(CLIENT_ROLE, RESQPET_REALM, cesarUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, ezequielUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, javierUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, nataliaUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, danielUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, felipeUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, jesicaUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, jessicaUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, juniorUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, sofiaUser.getUsername(), resqpetClientId);
		roles.setRole(CLIENT_ROLE, RESQPET_REALM, stefanyUser.getUsername(), resqpetClientId);

//		roles.setRole(ADMIN_ROLE, RESQPET_REALM, adminUser.getUsername(), resqpetClientId);

//		users.setGroup(idUser1,GROUPS_VALUE, realmResource, usersResource);

//		TODO: Cambiar por variables de entorno
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String postUrl = microserviceUrl+"/user-details";

		HttpPost httpPost = new HttpPost(postUrl);

		httpPost.addHeader("Content-Type", "application/json");

		String[] userIds = {idCesar, idEzequiel, idDaniel, idJavier, idNatalia, idFelipe, idJesica, idJessica, idJunior,idSofia, idStefany};

		for (String userId : userIds) {
			// Crear un objeto JSON
			JSONObject jsonBody = new JSONObject();
			jsonBody.put("userId", userId);
			jsonBody.put("cellphone", "123456789");
			jsonBody.put("locationId", 1);

			// Configurar el cuerpo de la solicitud POST
			httpPost.setEntity(new StringEntity(jsonBody.toString(), ContentType.APPLICATION_JSON));

			try {
				// Ejecutar la solicitud POST
				CloseableHttpResponse response = httpClient.execute(httpPost);

				// Obtener la respuesta y procesarla como sea necesario
				HttpEntity entity = response.getEntity();
				String responseContent = EntityUtils.toString(entity);

				System.out.println("Respuesta para el usuario " + userId + ": " + responseContent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		System.out.println("Proceso finalizado de forma exitosa");
	}

}
