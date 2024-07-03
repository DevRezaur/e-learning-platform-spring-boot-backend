package com.devrezaur.user.service.service;

import com.devrezaur.user.service.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Service class for Keycloak admin API.
 * <p>
 * This class provides methods for interacting with Keycloak auth server.
 * And allows the application to register new users, update user information, manage user roles, and perform
 * other operations related to user management in a Keycloak realm.
 *
 * @author Rezaur Rahman
 */
@Service
public class KeycloakService {

    @Value("${keycloak.realm-name}")
    private String realmName;

    private final Keycloak keycloak;

    /**
     * Constructor for KeycloakService class.
     *
     * @param keycloak instance of the Keycloak client used to interact with the Keycloak server.
     */
    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    /**
     * Registers a new user in the Keycloak realm.
     *
     * @param user the user object containing user information.
     * @return UUID of the registered user.
     * @throws Exception if an error occurs during user registration.
     */
    public UUID registerNewUser(User user) throws Exception {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEnabled(true);

        Response response = keycloak.realm(realmName).users().create(userRepresentation);

        if (response.getStatus() != 201) {
            throw new Exception(response.getStatusInfo().toString());
        }

        String keyCloakUserId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        updateUserCredentials(userRepresentation, user.getPassword());
        updateUserRole(keyCloakUserId, user.getRole());

        return UUID.fromString(keyCloakUserId);
    }

    /**
     * Updates user information in the Keycloak realm.
     *
     * @param user the user object containing updated information.
     * @throws Exception if an error occurs during user information update.
     */
    public void updateUser(User user) throws Exception {
        UserResource userResource = getUserResourceById(user.getUserId().toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (userRepresentation == null) {
            throw new Exception("No such user found!");
        }
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userResource.update(userRepresentation);
    }

    /**
     * Retrieves a UserResource object for a user with the specified id.
     *
     * @param keyCloakUserId the user's Keycloak id.
     * @return a UserResource object of the user.
     */
    public UserResource getUserResourceById(String keyCloakUserId) {
        return keycloak.realm(realmName).users().get(keyCloakUserId);
    }

    /**
     * Updates the role of a user in the Keycloak realm.
     *
     * @param keyCloakUserId the user's Keycloak id.
     * @param role           new role for the user.
     */
    private void updateUserRole(String keyCloakUserId, String role) {
        List<RoleRepresentation> roleRepresentationList = new LinkedList<>();
        roleRepresentationList.add(keycloak.realm(realmName).roles().get(role).toRepresentation());
        keycloak.realm(realmName).users().get(keyCloakUserId).roles().realmLevel().add(roleRepresentationList);
    }

    /**
     * Updates the credentials of a user in the Keycloak realm.
     *
     * @param userRepresentation the UserRepresentation object representing the user.
     * @param password           new password for the user.
     */
    public void updateUserCredentials(UserRepresentation userRepresentation, String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(password);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
    }

    /**
     * Retrieves the UUIDs of users with a specific role in the Keycloak realm.
     *
     * @param role the role for which to retrieve user UUIDs.
     * @return list of UUIDs representing users with the specified role.
     */
    public List<UUID> getUserIdsByRole(String role) {
        List<UUID> userIds = new ArrayList<>();
        List<UserRepresentation> userRepresentationList = keycloak.realm(realmName).roles().get(role).getUserMembers();
        for (UserRepresentation userRepresentation : userRepresentationList) {
            userIds.add(UUID.fromString(userRepresentation.getId()));
        }
        return userIds;
    }

}
