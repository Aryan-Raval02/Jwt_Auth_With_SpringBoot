package com.task.JwtAuthentication.Service;

import com.task.JwtAuthentication.Dto.UserDto;
import com.task.JwtAuthentication.Dto.UserRegisterDto;

public interface UserServiceInterface
{
    UserDto saveNewUser(UserRegisterDto user);

    UserDto updateUser(UserRegisterDto userRequestDto);
}
