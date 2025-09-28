package com.example.jpashop.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.jpashop.domain.Member;
import com.example.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

/*
 * Service
 * 1. 비즈니스 로직
 * 2. 트랜잭션 처리
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    /*
     * @RequiredArgsConstructor
     * final 또는 **@NonNull**로 선언된 모든 필드를 인수로 갖는 생성자를 자동으로 생성해줍니다.
     */

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Database에서 name 컬럼에 UNIQUE 제약 조건 설정하기를 권장한다.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new RuntimeException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findMember(Long id) {
        return memberRepository.findOne(id);
    }

}
