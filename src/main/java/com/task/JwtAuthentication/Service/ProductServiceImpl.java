package com.task.JwtAuthentication.Service;

import com.task.JwtAuthentication.Dao.ProductDaoRepository;
import com.task.JwtAuthentication.Dao.UserDaoRepository;
import com.task.JwtAuthentication.Dto.ProductDto;
import com.task.JwtAuthentication.Dto.ProductRequestDto;
import com.task.JwtAuthentication.Entity.Product;
import com.task.JwtAuthentication.Entity.User;
import com.task.JwtAuthentication.Exception.ProductNotFoundException;
import com.task.JwtAuthentication.Exception.UserNotFoundException;
import com.task.JwtAuthentication.Mapper.EntityMapper;
import com.task.JwtAuthentication.Util.AuthUtil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductServiceInterface
{
    @Autowired ProductDaoRepository productDaoRepository;
    @Autowired EntityManager entityManager;
    @Autowired private UserDaoRepository userDaoRepository;

    @Override
    public List<ProductDto> findByUserUuid()
    {
        User user = userDaoRepository.findByUuid(AuthUtil.getCurrentUUID());
        if(user == null) throw new UserNotFoundException("Seller Not Found");

        return productDaoRepository.findByUserUuid(AuthUtil.getCurrentUUID()).stream().map(EntityMapper::toProductDto).toList();
    }

    @Override
    public ProductDto findByUserUuidAndPid(int pid)
    {
        User user = userDaoRepository.findByUuid(AuthUtil.getCurrentUUID());
        if(user == null) throw new UserNotFoundException("Seller Not Found");

        Product product = entityManager.find(Product.class, pid);
        if(product == null) throw new ProductNotFoundException("Product Not Found");

        return EntityMapper.toProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductRequestDto request)
    {
        User user = userDaoRepository.findByUuid(AuthUtil.getCurrentUUID());
        if(user == null) throw new UserNotFoundException("Seller Not Found");

        Product product = new Product();

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setUser(user);

        productDaoRepository.save(product);

        return EntityMapper.toProductDto(product);
    }

    @Override
    @Transactional
    public int deleteProductById(int pid)
    {
        User user = userDaoRepository.findByUuid(AuthUtil.getCurrentUUID());
        if(user == null) throw new UserNotFoundException("Seller Not Found");

        Product product = entityManager.find(Product.class, pid);
        if(product == null) throw new ProductNotFoundException("Product Not Found");

        productDaoRepository.delete(product);

        return pid;
    }

    @Override
    @Transactional
    public ProductDto updateProductById(ProductRequestDto request, int pid)
    {
        User user = userDaoRepository.findByUuid(AuthUtil.getCurrentUUID());
        if(user == null) throw new UserNotFoundException("Seller Not Found");

        Product existing = entityManager.find(Product.class, pid);
        if(existing == null) throw new ProductNotFoundException("Product Not Found");

        if( request.getName() != null ) existing.setName(request.getName());
        if( request.getPrice() != 0.0 ) existing.setPrice(request.getPrice());
        if( request.getCategory() != null ) existing.setCategory(request.getCategory());
        if( request.getInventory() != 0 ) existing.setInventory(request.getInventory());
        if( request.getDescription() != null ) existing.setDescription(request.getDescription());

        productDaoRepository.save(existing);

        return EntityMapper.toProductDto(existing);
    }
}
