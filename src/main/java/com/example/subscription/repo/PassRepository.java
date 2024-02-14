package com.example.subscription.repo;

import com.example.subscription.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PassRepository extends JpaRepository<Pass, Long> {

    @Query("select p from Pass p where p.member.id = :memberId and p.isActive = true")
    List<Pass> findAllByMemberIdAndActive(@Param("memberId") Long memberId);
}
