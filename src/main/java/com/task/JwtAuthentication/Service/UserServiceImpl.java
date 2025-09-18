package com.task.JwtAuthentication.Service;

import com.task.JwtAuthentication.Dao.UserDaoRepository;
import com.task.JwtAuthentication.Dto.UserDto;
import com.task.JwtAuthentication.Dto.UserRegisterDto;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Exception.UserNotFoundException;
import com.task.JwtAuthentication.Mapper.EntityMapper;
import com.task.JwtAuthentication.Util.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserServiceInterface
{
    @Autowired private UserDaoRepository userDaoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto saveNewUser(UserRegisterDto user)
    {
        User newUser = new User();
        newUser.setRole("ROLE_SELLER");
        newUser.setCreated_at(LocalDateTime.now());

        if(user.getName() != null) newUser.setName(user.getName());
        if(user.getUsername() != null) newUser.setUsername(user.getUsername());
        if(user.getPassword() != null) newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        String base = newUser.getUsername()+":"+newUser.getCreated_at();
        newUser.setUuid(UUID.nameUUIDFromBytes(base.getBytes()).toString());

        userDaoRepository.save(newUser);
        return EntityMapper.toUserDto(newUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserRegisterDto userRequestDto)
    {
        User user = userDaoRepository.findByUuid(AuthUtil.getCurrentUUID());
        if(user == null) throw new UserNotFoundException("Seller Not Found");

        if(userRequestDto.getName() != null) user.setName(userRequestDto.getName());
        if(userRequestDto.getUsername() != null) user.setUsername(userRequestDto.getUsername());
        if(userRequestDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userDaoRepository.save(user);

        return EntityMapper.toUserDto(user);
    }
}
