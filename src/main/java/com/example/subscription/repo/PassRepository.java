package com.example.subscription.repo;

import com.example.subscription.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassRepository extends JpaRepository<Pass, Long> {
    List<Pass> findAllByMemberId(Long memberId);
}
