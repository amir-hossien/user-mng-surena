package com.itsurena.usermng.service.user.mapper;

import com.itsurena.usermng.api.web.user.dto.UserCreateReqDto;
import com.itsurena.usermng.api.web.user.dto.UserResDto;
import com.itsurena.usermng.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "username")
    UserResDto userToUserDTO(User user);

    @Mapping(source = "username", target = "username")
    User userDTOToUser(UserCreateReqDto userCreateReqDto);

    List<UserResDto> listUserToListUserResDto(List<User> users);

    default Page<UserResDto> pageUserToListUserResDto(Page<User> entityPage){
        return new PageImpl<>(listUserToListUserResDto(entityPage.getContent()), entityPage.getPageable(), entityPage.getTotalElements());
    }
}
