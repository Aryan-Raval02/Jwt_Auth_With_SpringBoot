package com.task.JwtAuthentication.Controller;

import com.task.JwtAuthentication.Dto.ProductDto;
import com.task.JwtAuthentication.Dto.ProductRequestDto;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController
{
    @Autowired private ProductServiceImpl productService;

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAllProductsByUUID()
    {
        return ResponseEntity.ok(productService.findByUserUuid());
    }

    @GetMapping("/{pid}")
    public ResponseEntity<ProductDto> getProductByUUID(@PathVariable int pid)
    {
        return ResponseEntity.ok(productService.findByUserUuidAndPid(pid));
    }

    // When JWT Implements @PathVariable remove
    // Post Request For Insert New Products
    @PostMapping("/add")
    private ResponseEntity<ProductDto> insertProducts(@RequestBody ProductRequestDto request)
    {
        ProductDto productDto = productService.addProduct(request);
        return ResponseEntity.status(201).body(productDto);
    }

    // When JWT implements only product id remain in request parameter
    // Delete Request For Products
    @DeleteMapping("/{pid}")
    private ResponseEntity<String> deleteProduct(@PathVariable int pid)
    {
        productService.deleteProductById(pid);
        return ResponseEntity.ok("Product Deleted Successfully");
    }

    // Update Request For Products
    @PutMapping("/{pid}")
    private ResponseEntity<ProductDto> updateProduct(@RequestBody ProductRequestDto request, @PathVariable int pid)
    {
        ProductDto updateProductById = productService.updateProductById(request, pid);

        return ResponseEntity.ok(updateProductById);
    }
}
