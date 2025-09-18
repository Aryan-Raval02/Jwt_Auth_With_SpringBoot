package com.task.JwtAuthentication.Service;

import com.task.JwtAuthentication.Dto.ProductDto;
import com.task.JwtAuthentication.Dto.ProductRequestDto;
import com.task.JwtAuthentication.Entity.Product;

import java.util.List;

public interface ProductServiceInterface
{
    List<ProductDto> findByUserUuid();

    ProductDto findByUserUuidAndPid(int pid);

    ProductDto addProduct(ProductRequestDto request);

    int deleteProductById(int pid);

    ProductDto updateProductById(ProductRequestDto request, int pid);
}
