package com.itsurena.usermng.service.user;


import com.itsurena.usermng.api.web.user.dto.UserCreateReqDto;
import com.itsurena.usermng.api.web.user.dto.UserResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResDto createUser(UserCreateReqDto userCreateReqDto);

    void deleteUserById(Long id);

    void deleteUserByUsername(String username);

    UserResDto updateUserNames(Long id, String firstName, String lastName);

    void changePassword(String username, String oldPassword, String newPassword);

    UserResDto getUserByUsername(String username);

    Page<UserResDto> getAllUsers(Pageable pageable);

}
