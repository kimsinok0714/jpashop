package com.example.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;

import com.example.jpashop.domain.Category;
import com.example.jpashop.exception.NotEnoughStockException;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 전략
@DiscriminatorColumn(name = "dtype")
@Setter
@Getter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;


    // 확인해 볼것!!
    // 1:N 단방향인 경우 이 코드는 필요 없음
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();


    
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직
    /*
     * 재고 수량 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /*
     * 재고 수량 감소
     */
    public void removeStock(int quantity) {

        int restQunatity = this.stockQuantity - quantity;

        if (restQunatity < 0) {
            throw new NotEnoughStockException("재고 수량이 부족합니다.");
        }

        this.stockQuantity -= quantity;
    }

}
