package com.domain.eonite.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.domain.eonite.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{
    List<Product> findAllById(Integer id);

    List<Product> findAllByVendorId(Integer id);

    @Query(value="SELECT SUM(rating)/count(*) FROM product_review WHERE product_id= :id",nativeQuery=true)
    float updateRatingProduct(Integer id);

    @Query(value="SELECT SUM(pr.rating)/count(*) FROM product_review pr JOIN product p ON p.id = pr.product_id where p.vendor_id = :id",nativeQuery = true)
    float updateRatingVendor(Integer id);
}
