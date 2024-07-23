package com.example.thecoffeehouse.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.thecoffeehouse.entity.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR lower(p.name) LIKE %:name%) AND " +
            "(:typeID IS NULL OR p.TypeID = :typeID)")
    Page<Product> getAllProducts(@Param("name") String name, @Param("typeID") Long typeID, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.TypeID = :typeID")
    List<Product> findByTypeAndName(@Param("name") String name, @Param("typeID") Long typeID);

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    List<Product> findByName(@Param("name") String name);

    @Query("SELECT p FROM Product p WHERE p.TypeID = :typeID")
    List<Product> getProductsByTypeID(@Param("typeID") Long typeID);

    @Query("SELECT p FROM Product p JOIN ProductType pt ON p.TypeID = pt.id WHERE (:name IS NULL OR pt.name = :name)")
    List<Product> getProductsByTypeName(@Param("name") String name);

    @Query("SELECT p, AVG(pr.rate) as avg_rating " +
            "FROM Product p " +
            "JOIN ProductReview pr ON p.id = pr.productId " +
            "GROUP BY p.id, p.name " +
            "ORDER BY avg_rating DESC LIMIT 4")
    List<Product> getAvgRating();

    @Query("SELECT p, SUM(bp.quantityProduct) as total_quantity " +
            "FROM Product p " +
            "JOIN BillProduct bp ON p.id = bp.productID " +
            "GROUP BY p.id, p.name " +
            "ORDER BY total_quantity DESC LIMIT 4")
    List<Product> getTotalQuantity();

    @Query("SELECT p, COUNT(up.id) as total_likes " +
            "FROM Product p " +
            "JOIN UserProduct up ON p.id = up.productId " +
            "WHERE up.liked = true " +
            "GROUP BY p.id, p.name " +
            "ORDER BY total_likes DESC LIMIT 4")
    List<Product> getTotalLikes();

    @Query("SELECT p " +
            "FROM Product p " +
            "ORDER BY p.createTime DESC LIMIT 2")
    List<Product> getNewestProducts();

}
