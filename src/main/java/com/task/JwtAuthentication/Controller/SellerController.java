package com.task.JwtAuthentication.Controller;

import com.task.JwtAuthentication.Dto.UserDto;
import com.task.JwtAuthentication.Dto.UserRegisterDto;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Service.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController
{
    @Autowired private UserServiceImpl userService;

    @PostMapping("/new")
    private ResponseEntity<UserDto> createNewUser(@RequestBody UserRegisterDto user)
    {
        UserDto userDto = userService.saveNewUser(user);
        return ResponseEntity.status(201).body(userDto);
    }

    @PutMapping("")
    private ResponseEntity<UserDto> updateUser(@RequestBody UserRegisterDto userRequestDto)
    {
        return ResponseEntity.ok(userService.updateUser(userRequestDto));
    }
}
