package com.example.jpashop.repository;

import org.springframework.stereotype.Repository;
import com.example.jpashop.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /*
     * - Java에서 JPA(Java Persistence API)를 사용할 때 사용하는 애노테이션
     * - 이 애노테이션은 EntityManager를 주입할 때 사용됩니다.
     * - EntityManager는 JPA에서 데이터베이스 작업을 수행하는 주요 인터페이스입니다.
     * - 엔티티를 저장, 수정, 삭제, 조회할 때 사용됩니다
     */

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /*
     * JPQL(Java Persistence Query Language)
     * JPA(Java Persistence API)에서 사용되는 쿼리 언어로,
     * 데이터베이스 테이블이 아닌 엔티티 객체를 대상으로 질의를 수행하는 언어입니다.
     */

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();

    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

    }

}
