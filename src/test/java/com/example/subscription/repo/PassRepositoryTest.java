package com.example.subscription.repo;

import com.example.subscription.entity.Member;
import com.example.subscription.entity.Pass;
import com.example.subscription.entity.PassProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PassRepositoryTest {

    @Autowired
    PassRepository passRepository;
    @Autowired
    PassProductRepository passProductRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("사용자 id를 인자로 가지고 있는 구독권을 조회한다.")
    @Test
    void findByMemberId() {
        // given
        PassProduct passProduct = passProductRepository.findById(1L).get();
        Member member = memberRepository.findById(1L).get();
        passRepository.save(Pass.builder()
                .startDt(LocalDateTime.now())
                .endDt(LocalDateTime.now().plusDays(passProduct.getPeriod()))
                .passProduct(passProduct)
                .member(member)
            .build());

        // when
        Pass pass = passRepository.findAllByMemberIdAndActiveIsTrue(1L).get(0);

        // then
        assertThat(pass.getPassProduct().getAmount()).isEqualTo(55000L);
    }

}