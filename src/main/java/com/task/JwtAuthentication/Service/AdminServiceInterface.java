package com.task.JwtAuthentication.Service;

import com.task.JwtAuthentication.Dto.SellerDto;
import com.task.JwtAuthentication.Dto.UserDto;
import com.task.JwtAuthentication.Dto.UserRegisterDto;
import com.task.JwtAuthentication.Entity.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface AdminServiceInterface
{
    List<SellerDto> findByRole(String role);
    String deleteUserById(String uuid);

    UserDto saveNewUser(UserRegisterDto user);
    UserDto updateUser(UserRegisterDto userRequestDto);
}
