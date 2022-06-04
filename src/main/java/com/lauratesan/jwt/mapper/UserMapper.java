package com.lauratesan.jwt.mapper;

import com.lauratesan.jwt.dto.UserDTO;
import com.lauratesan.jwt.model.auth.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
