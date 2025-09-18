package com.task.JwtAuthentication.Service;

import com.task.JwtAuthentication.Dao.UserDaoRepository;
import com.task.JwtAuthentication.Dto.SellerDto;
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
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminServiceInterface
{
    @Autowired private UserDaoRepository userDaoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public List<SellerDto> findByRole(String role)
    {
        List<User> sellers = userDaoRepository.findByRole("ROLE_SELLER");
        return sellers.stream().map(EntityMapper::toSellerDto).toList();
    }

    @Transactional
    @Override
    public String deleteUserById(String uuid)
    {
        User user = userDaoRepository.findByUuid(uuid);
        if(user == null) throw new UserNotFoundException("Admin Not Found");

        userDaoRepository.delete(user);

        return uuid;
    }

    @Transactional
    @Override
    public UserDto saveNewUser(UserRegisterDto user)
    {
        User newUser = new User();
        newUser.setRole("ROLE_ADMIN");
        newUser.setCreated_at(LocalDateTime.now());

        if(user.getName() != null) newUser.setName(user.getName());
        if(user.getUsername() != null) newUser.setUsername(user.getUsername());
        if(user.getPassword() != null) newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        String base = newUser.getUsername()+":"+newUser.getCreated_at();
        newUser.setUuid(UUID.nameUUIDFromBytes(base.getBytes()).toString());

        userDaoRepository.save(newUser);
        return EntityMapper.toUserDto(newUser);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserRegisterDto userRequestDto)
    {
        User user = userDaoRepository.findByUuid(AuthUtil.getCurrentUUID());
        if(user == null) throw new UserNotFoundException("Admin Not Found");

        if(userRequestDto.getName() != null) user.setName(userRequestDto.getName());
        if(userRequestDto.getUsername() != null) user.setUsername(userRequestDto.getUsername());
        if(userRequestDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userDaoRepository.save(user);

        return EntityMapper.toUserDto(user);
    }
}
