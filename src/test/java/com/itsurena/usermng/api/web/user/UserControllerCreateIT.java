package com.itsurena.usermng.api.web.user;

import com.itsurena.usermng.TestUserMngApplication;
import com.itsurena.usermng.api.web.BaseResDto;
import com.itsurena.usermng.api.web.user.dto.UserCreateReqDto;
import com.itsurena.usermng.api.web.user.dto.UserResDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestUserMngApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerCreateIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/users";
    }

    @Test
    public void givenValidUserDetails_whenCreateUser_thenUserIsCreatedSuccessfully() {
        UserCreateReqDto userCreateReqDto = new UserCreateReqDto();
        userCreateReqDto.setUsername("testuser");
        userCreateReqDto.setPassword("testpassword");
        userCreateReqDto.setFirstName("Test");
        userCreateReqDto.setLastName("User");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserCreateReqDto> request = new HttpEntity<>(userCreateReqDto, headers);

        ResponseEntity<BaseResDto<UserResDto>> response = restTemplate.exchange(
                getRootUrl(),
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<BaseResDto<UserResDto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getBody().getFirstName()).isEqualTo("Test");
        assertThat(response.getBody().getBody().getLastName()).isEqualTo("User");
    }
}