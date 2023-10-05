package com.devrezaur.user.service;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.user.service.model.User;
import com.devrezaur.user.service.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.UUID;

import static com.devrezaur.common.module.constant.CommonConstant.CONTENT_TYPE_HEADER_KEY;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDemo {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @SpyBean
    private UserService userService;

    @Test
    public void testAddRegularUser() {
        User user = new User();
        user.setFirstName("Rezaur");
        user.setLastName("Rahman");
        user.setEmail("email@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE_HEADER_KEY, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<CustomHttpResponse> responseEntity = testRestTemplate.exchange("http://localhost:" + port +
                "/user-service/user", HttpMethod.POST, requestEntity, CustomHttpResponse.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void getRegularUser() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setUserId(userId);
        user.setFirstName("Rezaur");
        user.setLastName("Rahman");
        user.setEmail("email@gmail.com");

        Mockito.when(userService.getUser(any())).thenReturn(user);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<CustomHttpResponse> responseEntity = testRestTemplate.exchange("http://localhost:" + port +
                "/user-service/user/" + userId, HttpMethod.GET, requestEntity, CustomHttpResponse.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

}
