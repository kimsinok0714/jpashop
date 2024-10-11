package com.example.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApplication1 {

    public static void main(String[] args) {
        SpringApplication.run(SpringApplication1.class, args);

    }

}

/*
 * 엔티티 설계시 고려사항
 * 
 * 1. 엔티티는 가급적 Setter를 사용하지 말자
 * 2. 실무에서 모든 연관 관계는 지연(LAZY) 로딩을 사용하자. (N+1문제)
 * 3. 연관된 엔티티를 함께 DB에서 조회해야 하는 경우 Fetch Join 또는 엔티티 그래프 기능을 사용한다.
 * - @OneToMany : Default Fetch Type LAZY
 * - @OneToOne : Default Fetch Type LAZY
 * - @ManyToONE : Default Fetch Type EAGER *
 * 4. 컬렉션은 필드에서 초기화 하자
 * 
 * 
 * 테이블, 컬럼명 생성 전략 : 스프링 부트 (SpringPhysicalNamingStrategy)
 * 
 * 엔티티(필드) -> 테이블(컬럼)
 * 
 * - 카멜 케이스 -> 언더스코어 (memberPoint -> member_point)
 * - .(dot) -> 언더스코더
 * - 대문자 -> 소문자
 * 
 * 
 */

/*
 *
 * @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
 * private List<OrderItem> orderItems = new ArrayList<>();
 * 주문 정보가 저장될 때 주문 아이템 정보들로 같이 저장된다. (persist)
 * 
 * 
 * 연관 관계 편입 메소드를 구현한다.
 * 
 * 
 * 
 * 1.로그인과 권한 관리
 * 2. 검증과 예외처리 단순화
 * 3. 상품은 도서만
 * 4. 카테고리 정보 사용 X
 * 5. 배송 정보 사용 X
 */

/*
 * 애플리케이션 아키텍처
 * 패키지 구조
 * 
 * 1. domain
 * 2. exception
 * 3. repository
 * 4. service
 * 5. web
 * 
 * 8 핵심 비즈니스 계층을 먼저 구현하고 테스트를 수행한 후 웹 계층 구현
 */

// 1. 회원 도메일 개발
// 구현 기능
// 1. 회원 등록
// 2. 회원 목록 조회
