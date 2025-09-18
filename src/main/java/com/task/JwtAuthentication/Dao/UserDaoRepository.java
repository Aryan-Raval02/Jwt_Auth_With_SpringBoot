package com.task.JwtAuthentication.Dao;

import com.task.JwtAuthentication.Dto.UserDto;
import com.task.JwtAuthentication.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDaoRepository extends JpaRepository<User, Integer>
{
    User findByUuid(String uuid);
    List<User> findByRole(String role);
    User findByUsername(String username);
}
