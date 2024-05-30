package com.itsurena.usermng.api.web.user;

import com.itsurena.usermng.api.web.BaseResDto;
import com.itsurena.usermng.api.web.PageBaseResDto;
import com.itsurena.usermng.api.web.user.dto.UserCreateReqDto;
import com.itsurena.usermng.api.web.user.dto.UserResDto;
import com.itsurena.usermng.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<BaseResDto<UserResDto>> createUser(@Valid @RequestBody UserCreateReqDto userCreateReqDto) {
        log.info("Received request to create user with username: {}", userCreateReqDto.getUsername());
        UserResDto userResDto = userService.createUser(userCreateReqDto);
        log.info("User created successfully with username: {}", userResDto.getUsername());
        return ResponseEntity.ok(BaseResDto.<UserResDto>builder()
                .body(userResDto)
                .message("the create user")
                .status(HttpStatus.OK.value()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long id) {
        log.info("Received request to delete user with id: {}", id);
        userService.deleteUserById(id);
        log.info("User with id: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/username/{username}")
    public ResponseEntity<Object> deleteUserByUsername(@PathVariable String username) {
        log.info("Received request to delete user with username: {}", username);
        userService.deleteUserByUsername(username);
        log.info("User with username: {} deleted successfully", username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResDto> updateUserNames(
            @PathVariable Long id,
            @RequestParam String firstName,
            @RequestParam String lastName) {
        log.info("Received request to update user with id: {}. New firstName: {}, new lastName: {}", id, firstName, lastName);
        UserResDto userResDto = userService.updateUserNames(id, firstName, lastName);
        log.info("Updated user with id: {}. New firstName: {}, new lastName: {}", id, userResDto.getFirstName(), userResDto.getLastName());
        return ResponseEntity.ok(userResDto);
    }

    @PutMapping("/password")
    public ResponseEntity<Object> changePassword(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        log.info("Received request to change password for username: {}", username);
        userService.changePassword(username, oldPassword, newPassword);
        log.info("Password changed successfully for username: {}", username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResDto> getUserByUsername(@PathVariable String username) {
        log.info("Received request to get user by username: {}", username);
        UserResDto userResDto = userService.getUserByUsername(username);
        log.info("Returning user details for username: {}", username);
        return ResponseEntity.ok(userResDto);
    }

    @GetMapping
    public ResponseEntity<PageBaseResDto<List<UserResDto>>> indexUser(@RequestParam(name = "pageNumber") Integer pageN
            , @RequestParam("pageSize") Integer pageS
            , @RequestParam(name = "sort") List<String> sort) {
        log.info("indexUser controller start with pageNumber: {}, pageSize: {}, sort: {}", pageN, pageS, sort);
        Pageable pageable = PageRequest.of(pageN, pageS,
                Sort.by(Sort.Direction.fromString(sort.get(0)), sort.get(1)));
        Page<UserResDto> userResDtos = userService.getAllUsers(pageable);
        log.info("indexUser controller end with users: {}", userResDtos);
        return ResponseEntity.ok(PageBaseResDto.<List<UserResDto>>builder()
                .body(userResDtos.getContent())
                .message("The index user operation was successful")
                .pageSize(userResDtos.getPageable().getPageSize())
                .pageNumber(userResDtos.getPageable().getPageNumber())
                .totalPage(userResDtos.getTotalPages())
                .totalElement(userResDtos.getTotalElements())
                .status(HttpStatus.OK.value()).build());
    }

}
