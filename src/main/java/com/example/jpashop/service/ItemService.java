package com.example.jpashop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long createItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    public Item findItem(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItems() {
        List<Item> items = itemRepository.findAll();
        log.info("아이템 목록 갯수 : {}", items.size());
        return items;
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        // 변경 감지 : 베스트 프랙티스
        // 영속성 엔티티
        // 트랜잭션이 커밋되면 JPA는 flush()를 수행한다.
        // 영속성 컨텍스트 중에 변경된 내용을 찾아서 DB에 업데이트를 수행한다.
        Item foundItem = itemRepository.findOne(itemId);

        // foundItem.change() 코드로 변경
        // foundItem.setPrice(price);
        // foundItem.setName(name);
        // foundItem.setStockQuantity(stockQuantity);
        // foundItem.setPrice(price);
        foundItem.change(name, price, stockQuantity);

    }

}
