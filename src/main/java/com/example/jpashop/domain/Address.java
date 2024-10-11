package com.example.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

//값 타입 설계 시 고려사항

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // protected 로 설정하기를 권장합니다.
    protected Address() {

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
