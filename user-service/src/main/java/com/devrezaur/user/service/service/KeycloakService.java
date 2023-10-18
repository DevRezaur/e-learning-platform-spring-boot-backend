package com.devrezaur.user.service.service;

import com.devrezaur.user.service.model.Role;
import com.devrezaur.user.service.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class KeycloakService {

    @Value("${keycloak.realm-name}")
    private String realmName;

    private final Keycloak keycloak;

    public KeycloakService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public UUID registerNewUser(User user) throws Exception {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEnabled(true);
        Response response = keycloak.realm(realmName).users().create(userRepresentation);

        if (response.getStatus() == 201) {
            try {
                String keyCloakUserId = getKeyCloakUserId(user.getEmail());
                UserResource userResource = keycloak.realm(realmName).users().get(keyCloakUserId);
                updateUserRole(userResource, user.getRole());
                updateUserCredentials(userResource, user.getPassword());
                return UUID.fromString(keyCloakUserId);
            } catch (Exception ex) {
                throw new Exception("Unable to register user in auth server! Reason: " + ex.getMessage());
            }
        } else {
            throw new Exception("Unable to register user in auth server! Reason: " + response.getStatusInfo());
        }
    }

    public void updateUser(User user) throws Exception {
        UserResource userResource = keycloak.realm(realmName).users().get(user.getUserId().toString());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (userRepresentation == null) {
            throw new Exception("No user found in auth server with this email!");
        }
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userResource.update(userRepresentation);
    }

    private String getKeyCloakUserId(String email) throws Exception {
        List<UserRepresentation> userRepresentationList = keycloak.realm(realmName).users().searchByEmail(email, true);
        if (CollectionUtils.isEmpty(userRepresentationList)) {
            throw new Exception("No user found in auth server with this email!");
        }
        return userRepresentationList.get(0).getId();
    }

    private void updateUserRole(UserResource userResource, Role role) {
        List<RoleRepresentation> roleRepresentationList = new LinkedList<>();
        roleRepresentationList.add(keycloak.realm(realmName).roles().get(role.toString()).toRepresentation());
        userResource.roles().realmLevel().add(roleRepresentationList);
    }

    private void updateUserCredentials(UserResource userResource, String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(password);
        userResource.resetPassword(credentialRepresentation);
    }

    public List<UUID> getUserIdsByRole(Role role) {
        List<UUID> userIds = new ArrayList<>();
        List<UserRepresentation> userRepresentationList =
                keycloak.realm(realmName).roles().get(role.toString()).getUserMembers();
        for (UserRepresentation userRepresentation : userRepresentationList) {
            userIds.add(UUID.fromString(userRepresentation.getId()));
        }
        return userIds;
    }

}
