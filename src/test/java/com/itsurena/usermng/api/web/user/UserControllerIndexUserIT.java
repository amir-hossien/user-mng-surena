package com.itsurena.usermng.api.web.user;

import com.itsurena.usermng.TestUserMngApplication;
import com.itsurena.usermng.api.web.BaseResDto;
import com.itsurena.usermng.api.web.PageBaseResDto;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestUserMngApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIndexUserIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        List<User> users = Arrays.asList(
                new User(null,null, "user1", "password1", "FirstName1", "LastName1", null, null),
                new User(null,null, "user2", "password2", "FirstName2", "LastName2", null, null),
                new User(null,null, "user3", "password3", "FirstName3", "LastName3", null, null)
        );

        userRepository.saveAll(users);
    }

    @Test
    public void givenValidPageRequest_whenIndexUser_thenStatusOkAndReturnsUsers() {
        Integer pageNumber = 0;
        Integer pageSize = 2;
        List<String> sort = Arrays.asList("ASC", "username");
        String url = String.format("http://localhost:%d/api/users?pageNumber=%d&pageSize=%d&sort=%s&sort=%s", port, pageNumber, pageSize, sort.get(0), sort.get(1));

        ResponseEntity<PageBaseResDto<List<UserResDto>>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PageBaseResDto<List<UserResDto>> body = responseEntity.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.getMessage()).isEqualTo("The index user operation was successful");
        assertThat(body.getBody()).isNotNull();
        assertThat(body.getBody().size()).isEqualTo(2);

        // Additional assertions on the content
        List<UserResDto> users = body.getBody();
        assertThat(users.get(0).getUsername()).isEqualTo("user1");
        assertThat(users.get(1).getUsername()).isEqualTo("user2");
    }

}