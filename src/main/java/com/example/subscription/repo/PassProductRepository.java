package com.example.subscription.repo;

import com.example.subscription.entity.PassProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassProductRepository extends JpaRepository<PassProduct, Long> {
}
