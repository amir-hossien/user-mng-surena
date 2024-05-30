package com.itsurena.usermng.api.web.user;

import com.itsurena.usermng.TestUserMngApplication;
import com.itsurena.usermng.api.web.user.dto.UserResDto;
import com.itsurena.usermng.model.user.User;
import com.itsurena.usermng.model.user.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestUserMngApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerGetUserIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userRepository.save(testUser);
    }

    @Test
    public void givenExistingUsername_whenGetUserByUsername_thenStatusOk() {
        String username = "testuser";
        String url = "http://localhost:" + port + "/api/users/username/" + username;

        ResponseEntity<UserResDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResDto userResDto = responseEntity.getBody();
        assertThat(userResDto).isNotNull();
        assertThat(userResDto.getUsername()).isEqualTo("testuser");
        assertThat(userResDto.getFirstName()).isEqualTo("Test");
        assertThat(userResDto.getLastName()).isEqualTo("User");
    }

    @Test
    public void givenNonExistingUsername_whenGetUserByUsername_thenStatusNotFound() {
        String username = "nonexistinguser";
        String url = "http://localhost:" + port + "/api/users/username/" + username;

        ResponseEntity<UserResDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, UserResDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}