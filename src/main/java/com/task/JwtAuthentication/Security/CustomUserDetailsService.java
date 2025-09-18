package com.task.JwtAuthentication.Security;

import com.task.JwtAuthentication.Dao.UserDaoRepository;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserDaoRepository userDaoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) // throws UserNotFoundException
    {
        User user = userDaoRepository.findByUsername(username);
        if(user == null) throw new UserNotFoundException("User Not Found");
        var authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getUuid(), user.getPassword(), authorities);
    }
}
