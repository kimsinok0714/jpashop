package com.example.jpa_project.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.example.jpa_project.item.Album;
// import com.example.jpa_project.item.Book;
import jakarta.transaction.Transactional;
import static org.assertj.core.api.Assertions.*;


@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class ItemServiceTest {
  
    @Autowired
    private ItemService itemService;

    @Test
    @Rollback(value = false)
    void testCreateItem() {

        //given
        // Book book = new Book();
        // book.setAuthor("루시 모드 몽고메리");
        // book.setName("빨강 머리 앤");
        // book.setPrice(30000);
        // book.setIsbn("12345");
        // book.setStockQuantity(100);

        Album album = new Album();
        album.setArtist("잔나비");
        album.setEtc("...");
        album.setName("전설");
        album.setStockQuantity(1000);
        album.setPrice(30000);

        //when  
        itemService.createItem(album);

        //then
        assertThat(album.getId()).isNotNull();
        
    }
}
