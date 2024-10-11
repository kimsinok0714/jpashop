package com.example.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.IllegalStateException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 직접 생성하지 말고 다른 생성 메소드를 사용하여 생성할 것!!
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    // 연관 관계의 주인을 정해준다.
    // Foreign key 가 있는 테이블을 연관관계의 주인으로 정한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 : ORDER, CANCEL

    // 연관 관계 메소드
    public void setMember(Member member) {
        this.member = member;
        this.member.getOrders().add(this);
    }

    // 연관 관계 메소드
    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        orderItems.add(orderItem);
    }

    // 연관 관계 메소드
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        this.delivery.setOrder(this);
    }

    // 생성 메소드

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직

    // 주문 취소

    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMPLETE) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 전체 주문가격 조회

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
