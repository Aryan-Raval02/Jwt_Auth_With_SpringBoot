package com.task.JwtAuthentication.Mapper;

import com.task.JwtAuthentication.Dto.ProductDto;
import com.task.JwtAuthentication.Dto.SellerDto;
import com.task.JwtAuthentication.Dto.UserDto;
import com.task.JwtAuthentication.Entity.Product;
import com.task.JwtAuthentication.Entity.User;

public class EntityMapper
{
    public static UserDto toUserDto(User user)
    {
        if(user == null) return null;

        UserDto dto = new UserDto();

        dto.setName(user.getName());
        dto.setRole(user.getRole());
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreated_at());

        return dto;
    }

    public static SellerDto toSellerDto(User user)
    {
        if(user == null) return null;

        SellerDto dto = new SellerDto();

        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreated_at());
        dto.setUuid(user.getUuid());

        return dto;
    }

    public static ProductDto toProductDto(Product product)
    {
        if(product == null) return null;

        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory());
        productDto.setDescription(product.getDescription());
        productDto.setInventory(product.getInventory());
        productDto.setUser(toUserDto(product.getUser()));

        return productDto;
    }
}