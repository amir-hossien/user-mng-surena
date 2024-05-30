package com.itsurena.usermng.service.user.impl;

import com.itsurena.usermng.api.web.user.dto.UserCreateReqDto;
import com.itsurena.usermng.api.web.user.dto.UserResDto;
import com.itsurena.usermng.exception.UserAlreadyExistsException;
import com.itsurena.usermng.exception.UserDataNotFoundException;
import com.itsurena.usermng.model.user.User;
import com.itsurena.usermng.model.user.dao.UserRepository;
import com.itsurena.usermng.service.user.UserService;
import com.itsurena.usermng.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserResDto createUser(UserCreateReqDto userCreateReqDto) {
        log.debug("Checking if username '{}' already exists", userCreateReqDto.getUsername());
        if (userRepository.existsByUsername(userCreateReqDto.getUsername())) {
            log.warn("Username '{}' already exists", userCreateReqDto.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = userMapper.userDTOToUser(userCreateReqDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserResDto userResDto = userMapper.userToUserDTO(userRepository.save(user));
        log.info("User '{}' created successfully", userCreateReqDto.getUsername());
        return userResDto;
    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Checking if user with id '{}' exists", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found with id: {}", id);
            throw new UserDataNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User with id '{}' deleted successfully", id);

    }

    @Transactional
    @Override
    public void deleteUserByUsername(String username) {
        log.debug("Checking if user with username '{}' exists", username);
        if (!userRepository.existsByUsername(username)) {
            log.warn("User not found with username: {}", username);
            throw new UserDataNotFoundException("User not found with username: " + username);
        }
        userRepository.deleteByUsername(username);
        log.info("User with username '{}' deleted successfully", username);
    }

    @Override
    public UserResDto updateUserNames(Long id, String firstName, String lastName) {
        log.debug("Checking if user update with id '{}' exists", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserDataNotFoundException("User not found with id: " + id));

        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            log.warn("Invalid parameter(s) for user with id '{}': firstName='{}', lastName='{}'", id, firstName, lastName);
            throw new InvalidParameterException("First name and last name cannot be empty");
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        UserResDto userResDto = userMapper.userToUserDTO(userRepository.save(user));
        log.info("User with id '{}' updated successfully: new firstName='{}', new lastName='{}'", id, userResDto.getFirstName(), userResDto.getLastName());
        return userResDto;
    }


    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.debug("Fetching user with username '{}'", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDataNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Old password is incorrect for username '{}'", username);
            throw new InvalidParameterException("Old password is incorrect");
        }

        if (StringUtils.isBlank(newPassword)) {
            log.warn("New password cannot be empty for username '{}'", username);
            throw new InvalidParameterException("New password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for username '{}'", username);

    }

    @Override
    public UserResDto getUserByUsername(String username) {
        log.debug("Fetching user with username_ '{}'", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDataNotFoundException("User not found with username: " + username));
        UserResDto userResDto = userMapper.userToUserDTO(user);
        log.info("User details retrieved for username '{}'", username);
        return userResDto;
    }

    @Override
    public Page<UserResDto> getAllUsers(Pageable pageable) {
        log.debug("getAllUsers service start with pageable: {}", pageable);
        Page<User> page = userRepository.findAll(pageable);
        Page<UserResDto> userResDtos = userMapper.pageUserToListUserResDto(page);
        log.debug("getAllUsers service end with userResDtos: {}", userResDtos);
        return userResDtos;
    }
}
