package com.devrezaur.user.service;

import com.devrezaur.common.module.model.CustomHttpResponse;
import com.devrezaur.user.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static com.devrezaur.common.module.constant.CommonConstant.CONTENT_TYPE_HEADER_KEY;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDemo {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

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

}
