package com.example.jpashop.domain;

import com.example.jpashop.domain.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
   
    @Column(name = "order_price")
    private int orderPrice;

    private int count;
    
    // 연관 관게 편의 메소드
    public void setItem(Item item) {
        this.item = item;
        this.item.getOrderItems().add(this);
    }

    
    // 비즈니스 로직
    // 주문 항목 생성  
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        // 주문 항목의 재고량을 변경한다. (주문한 수량 만큼 상품의 재고량을 감소한다.)
        item.removeStock(count);
        return orderItem;
    }

    // 주문 취소
    public void cancel() {
        this.getItem().addStock(this.count);
    }

    // 총금액 계산
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }

}
