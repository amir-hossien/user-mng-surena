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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestUserMngApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerUpdateIT {

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
    public void givenUserIdAndValidNames_whenUpdateUserNames_thenStatusOk() {
        User testUser = userRepository.findAll().get(0);
        Long userId = testUser.getId();
        String url = "http://localhost:" + port + "/api/users/" + userId + "?firstName=UpdatedFirstName&lastName=UpdatedLastName";

        ResponseEntity<UserResDto> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, UserResDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserResDto userResDto = responseEntity.getBody();
        assertThat(userResDto).isNotNull();
        assertThat(userResDto.getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(userResDto.getLastName()).isEqualTo("UpdatedLastName");

        User updatedUser = userRepository.findById(userId).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstName()).isEqualTo("UpdatedFirstName");
        assertThat(updatedUser.getLastName()).isEqualTo("UpdatedLastName");
    }

    @Test
    public void givenUserIdAndEmptyNames_whenUpdateUserNames_thenStatusBadRequest() {
        User testUser = userRepository.findAll().get(0);
        Long userId = testUser.getId();
        String url = "http://localhost:" + port + "/api/users/" + userId + "?firstName=&lastName=";

        ResponseEntity<UserResDto> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, UserResDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenNonExistingUserId_whenUpdateUserNames_thenStatusNotFound() {
        Long nonExistingUserId = 999L;
        String url = "http://localhost:" + port + "/api/users/" + nonExistingUserId + "?firstName=FirstName&lastName=LastName";

        ResponseEntity<UserResDto> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, null, UserResDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}