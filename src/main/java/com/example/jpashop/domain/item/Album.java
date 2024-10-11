package com.example.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("album")
@Setter
@Getter
public class Album extends Item {

    private String artist;
    private String etc;

}
