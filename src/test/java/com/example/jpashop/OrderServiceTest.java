package com.example.jpashop;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.exception.NotEnoughStockException;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.service.OrderService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = new Member();
        member.setName("일길동");
        member.setAddress(new Address("서울시", "강남로", "1234"));
        em.persist(member);

        Item book = new Book();
        book.setName("JPA");
        book.setPrice(30000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findedOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 상태는 ORDER이어야 합니다.", OrderStatus.ORDER, findedOrder.getStatus());
        assertEquals("주문환 상품 종류의 수가  정확해야 합니다.", 1, findedOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량입니다", 60000, findedOrder.getTotalPrice());
        assertEquals("주문 수량 만큼 재고가 줄어야 합니다.", 8, book.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("서울", "강남로", "1234"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int orderPrice) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(orderPrice);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("JPA", 30000);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("마이크로서비스 패턴", 30000);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 시 주문 상태는 CANCEL이어야 한다", OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals("주문 취소 시 상품의 재고가 증가해야 한다.", 10, book.getStockQuantity());

    }

}
