package com.example.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.example.jpa_project.domain.Address;
import com.example.jpa_project.domain.Member;
import com.example.jpa_project.repository.MemberRepository;
import jakarta.transaction.Transactional;
import static org.assertj.core.api.Assertions.*;

/*
 @SpringBootTest
 * 
 * 테스트에서 Spring Boot 애플리케이션 컨텍스트를 모두 로드하도록 지시하는 애노테이션입니다. 
 * 애플리케이션이 실행될 때와 동일한 환경을 만들어서, 모든 스프링 빈을 로드하고 테스트할 수 있습니다. 
 * 애플리케이션의 여러 계층(서비스, 레포지토리, 컨트롤러 등)을 통합적으로 테스트할 수 있습니다. 
 */


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    @Rollback(false)
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("일길동");
        member.setAddress(new Address("서울시", "강남로", "1234"));

        // when
        Long savedId = memberService.createMember(member);

        // then
        assertThat(member.getId()).isNotNull();
    }

}
