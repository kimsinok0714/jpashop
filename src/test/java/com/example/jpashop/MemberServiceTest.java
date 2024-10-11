package com.example.jpashop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Address;
import com.example.jpashop.repository.MemberRepository;
import com.example.jpashop.service.MemberService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/*
 * 1. @RunWith(SpringRunner.class)
 * 
 * JUnit 테스트에서 Spring의 기능을 활용하려고 할때 사용합니다.
 * 테스트에서 Spring의 의존성 주입(Dependency Injection)과 트랜잭션 관리 같은 기능을 사용할 수 있게 됩니다.
 * 
 * 
 * 2. @SpringBootTest
 * 
 * 테스트에서 Spring Boot 애플리케이션 컨텍스트를 모두 로드하도록 지시하는 애노테이션입니다. 
 * 애플리케이션이 실행될 때와 동일한 환경을 만들어서, 모든 스프링 빈을 로드하고 테스트할 수 있습니다. 
 * 애플리케이션의 여러 계층(서비스, 레포지토리, 컨트롤러 등)을 통합적으로 테스트할 수 있습니다. 
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Test
    @Rollback(false)
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("일길동");
        member.setAddress(new Address("서울시", "강남로", "1234"));

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("일길동");

        Member member2 = new Member();
        member2.setName("일길동");

        // wheb
        memberService.join(member1);

        assertThrows(Exception.class, () -> {
            memberService.join(member2);
        });

        // then
        fail("예외가 발생합니다.");

    }

}
