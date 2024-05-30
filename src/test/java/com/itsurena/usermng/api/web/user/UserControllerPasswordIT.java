package com.itsurena.usermng.api.web.user;

import com.itsurena.usermng.TestUserMngApplication;
import com.itsurena.usermng.model.user.User;
import com.itsurena.usermng.model.user.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestUserMngApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerPasswordIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("oldpassword"));
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        userRepository.save(testUser);
    }

    @Test
    public void givenValidCredentials_whenChangePassword_thenStatusNoContent() {
        String username = "testuser";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        String url = "http://localhost:" + port + "/api/users/password?username=" + username + "&oldPassword=" + oldPassword + "&newPassword=" + newPassword;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        User updatedUser = userRepository.findByUsername(username).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(passwordEncoder.matches(newPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    public void givenInvalidOldPassword_whenChangePassword_thenStatusBadRequest() {
        String username = "testuser";
        String oldPassword = "wrongoldpassword";
        String newPassword = "newpassword";
        String url = "http://localhost:" + port + "/api/users/password?username=" + username + "&oldPassword=" + oldPassword + "&newPassword=" + newPassword;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenEmptyNewPassword_whenChangePassword_thenStatusBadRequest() {
        String username = "testuser";
        String oldPassword = "oldpassword";
        String newPassword = "";
        String url = "http://localhost:" + port + "/api/users/password?username=" + username + "&oldPassword=" + oldPassword + "&newPassword=" + newPassword;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenNonExistingUsername_whenChangePassword_thenStatusNotFound() {
        String username = "nonexistinguser";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        String url = "http://localhost:" + port + "/api/users/password?username=" + username + "&oldPassword=" + oldPassword + "&newPassword=" + newPassword;

        ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}