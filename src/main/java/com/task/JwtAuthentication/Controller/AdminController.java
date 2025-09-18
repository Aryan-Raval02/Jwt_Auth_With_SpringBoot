package com.task.JwtAuthentication.Controller;

import com.task.JwtAuthentication.Dto.SellerDto;
import com.task.JwtAuthentication.Dto.UserDto;
import com.task.JwtAuthentication.Dto.UserRegisterDto;
import com.task.JwtAuthentication.Service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired private AdminServiceImpl adminService;

    @GetMapping("")
    public ResponseEntity<List<SellerDto>> getAllSellers()
    {
        return ResponseEntity.ok(adminService.findByRole("ROLE_SELLER"));
    }

    @DeleteMapping("/{uuid}")
    private ResponseEntity<String> deleteById(@PathVariable String uuid)
    {
        String str = adminService.deleteUserById(uuid);
        return ResponseEntity.ok("User Deleted Successfully");
    }

    @PostMapping("/new")
    private ResponseEntity<UserDto> createNewUser(@RequestBody UserRegisterDto user)
    {
        UserDto userDto = adminService.saveNewUser(user);
        return ResponseEntity.status(201).body(userDto);
    }

    @PutMapping("")
    private ResponseEntity<UserDto> updateUser(@RequestBody UserRegisterDto userRequestDto)
    {
        return ResponseEntity.ok(adminService.updateUser(userRequestDto));
    }
}
