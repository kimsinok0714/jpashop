package com.example.jpa_project.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import com.example.jpa_project.item.Book;
import com.example.jpa_project.item.Album;
import com.example.jpa_project.item.Item;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.*;


@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CategoryTest {
    
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("카테고리 생성 및 저장 테스트")
    @Rollback(value = false)
    void testCreateCategory() {
        // given
        Category category = new Category();
        category.setName("도서");

        // when
        em.persist(category);
        em.flush();
        em.clear();

        // then
        Category foundCategory = em.find(Category.class, category.getId());
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("도서");
        assertThat(foundCategory.getId()).isNotNull();
        
        log.info("생성된 카테고리 ID: {}, 이름: {}", foundCategory.getId(), foundCategory.getName());
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    @Rollback(value = false)
    void testUpdateCategory() {
        // given
        Category category = new Category();
        category.setName("도서");
        em.persist(category);
        em.flush();
        
        // when
        category.setName("전자책");
        em.flush();
        em.clear();
        
        // then
        Category updatedCategory = em.find(Category.class, category.getId());
        assertThat(updatedCategory.getName()).isEqualTo("전자책");
        
        log.info("수정된 카테고리 이름: {}", updatedCategory.getName());
    }

    @Test
    @DisplayName("카테고리와 아이템 연관관계 테스트")
    @Rollback(value = false)
    void testCategoryItemRelation() {
        // given
        Category bookCategory = new Category();
        bookCategory.setName("도서");
        
        Category musicCategory = new Category();
        musicCategory.setName("음반");
        
        Book book = new Book();
        book.setName("Spring Boot");
        book.setPrice(30000);
        book.setStockQuantity(100);
        book.setAuthor("홍길동동");
        book.setIsbn("978-1234567890");
        
        Album album = new Album();
        album.setName("Best Album");
        album.setPrice(25000);
        album.setStockQuantity(50);
        album.setArtist("아티스트");
        album.setEtc("특별판");

        // when
        bookCategory.addItem(book);
        musicCategory.addItem(album);
        
        // 책은 두 카테고리에 속할 수 있음
        musicCategory.addItem(book);
        
        em.persist(bookCategory);
        em.persist(musicCategory);
        em.persist(book);
        em.persist(album);
        em.flush();
        em.clear();

        // then
        Category foundBookCategory = em.find(Category.class, bookCategory.getId());
        Category foundMusicCategory = em.find(Category.class, musicCategory.getId());
        
        assertThat(foundBookCategory.getItems()).hasSize(1);
        assertThat(foundMusicCategory.getItems()).hasSize(2); // album + book
        
        Book foundBook = (Book) em.find(Item.class, book.getId());
        assertThat(foundBook.getCategories()).hasSize(2); // bookCategory + musicCategory
        
        log.info("도서 카테고리의 아이템 수: {}", foundBookCategory.getItems().size());
        log.info("음반 카테고리의 아이템 수: {}", foundMusicCategory.getItems().size());
        log.info("책이 속한 카테고리 수: {}", foundBook.getCategories().size());
    }

    @Test
    @DisplayName("카테고리 계층 구조 테스트")
    @Rollback(value = false)
    void testCategoryHierarchy() {
        // given
        Category rootCategory = new Category();
        rootCategory.setName("전체");
        
        Category booksCategory = new Category();
        booksCategory.setName("도서");
        
        Category fictionCategory = new Category();
        fictionCategory.setName("소설");
        
        Category nonFictionCategory = new Category();
        nonFictionCategory.setName("비소설");
        
        Category techCategory = new Category();
        techCategory.setName("기술서적");

        // when - 계층 구조 설정
        rootCategory.addChildCategory(booksCategory);
        booksCategory.addChildCategory(fictionCategory);
        booksCategory.addChildCategory(nonFictionCategory);
        nonFictionCategory.addChildCategory(techCategory);
        
        em.persist(rootCategory);
        em.persist(booksCategory);
        em.persist(fictionCategory);
        em.persist(nonFictionCategory);
        em.persist(techCategory);
        em.flush();
        em.clear();

        // then
        Category foundRootCategory = em.find(Category.class, rootCategory.getId());
        Category foundBooksCategory = em.find(Category.class, booksCategory.getId());
        Category foundTechCategory = em.find(Category.class, techCategory.getId());
        
        // 루트 카테고리는 자식이 1개 (도서)
        assertThat(foundRootCategory.getChild()).hasSize(1);
        assertThat(foundRootCategory.getChild().get(0).getName()).isEqualTo("도서");
        
        // 도서 카테고리는 자식이 2개 (소설, 비소설)
        assertThat(foundBooksCategory.getChild()).hasSize(2);
        assertThat(foundBooksCategory.getParent().getName()).isEqualTo("전체");
        
        // 기술서적은 부모가 비소설
        assertThat(foundTechCategory.getParent().getName()).isEqualTo("비소설");
        assertThat(foundTechCategory.getChild()).isEmpty(); // 자식이 없음
        
        log.info("루트 카테고리의 자식 수: {}", foundRootCategory.getChild().size());
        log.info("도서 카테고리의 자식 수: {}", foundBooksCategory.getChild().size());
        log.info("기술서적의 부모: {}", foundTechCategory.getParent().getName());
    }

    @Test
    @DisplayName("연관관계 편의 메서드 테스트")
    @Rollback(value = false)
    void testConvenienceMethods() {
        // given
        Category category = new Category();
        category.setName("프로그래밍");
        
        Book book = new Book();
        book.setName("Clean Code");
        book.setPrice(35000);
        book.setStockQuantity(50);
        book.setAuthor("Robert C. Martin");
        book.setIsbn("978-0132350884");
        
        Category subCategory = new Category();
        subCategory.setName("Java");

        // when - addItem 메서드 테스트
        category.addItem(book);
        
        // when - addChildCategory 메서드 테스트
        category.addChildCategory(subCategory);
        
        em.persist(category);
        em.persist(book);
        em.persist(subCategory);
        em.flush();

        // then - 양방향 연관관계가 모두 설정되었는지 확인
        assertThat(category.getItems()).contains(book);
        assertThat(book.getCategories()).contains(category);
        
        assertThat(category.getChild()).contains(subCategory);
        assertThat(subCategory.getParent()).isEqualTo(category);
        
        log.info("카테고리의 아이템: {}", category.getItems().get(0).getName());
        log.info("아이템의 카테고리: {}", book.getCategories().get(0).getName());
        log.info("부모 카테고리: {}", category.getName());
        log.info("자식 카테고리: {}", subCategory.getName());
        log.info("자식의 부모: {}", subCategory.getParent().getName());
    }

    @Test
    @DisplayName("복잡한 카테고리 구조와 아이템 관계 테스트")
    @Rollback(value = false)
    void testComplexCategoryStructure() {
        // given
        Category electronics = new Category();
        electronics.setName("전자제품");
        
        Category computers = new Category();
        computers.setName("컴퓨터");
        
        Category laptops = new Category();
        laptops.setName("노트북");
        
        Category gaming = new Category();
        gaming.setName("게임");
        
        Book programmingBook = new Book();
        programmingBook.setName("Java Programming");
        programmingBook.setPrice(40000);
        programmingBook.setStockQuantity(30);
        programmingBook.setAuthor("Oracle");
        programmingBook.setIsbn("978-1111111111");
        
        Album gameOST = new Album();
        gameOST.setName("Game OST Collection");
        gameOST.setPrice(20000);
        gameOST.setStockQuantity(100);
        gameOST.setArtist("Various Artists");
        gameOST.setEtc("Limited Edition");

        // when - 복잡한 구조 설정
        electronics.addChildCategory(computers);
        computers.addChildCategory(laptops);
        
        // 프로그래밍 책은 컴퓨터 카테고리에
        computers.addItem(programmingBook);
        
        // 게임 OST는 게임 카테고리에
        gaming.addItem(gameOST);
        
        // 프로그래밍 책은 게임 카테고리에도 속할 수 있음 (게임 개발 관련)
        gaming.addItem(programmingBook);
        
        em.persist(electronics);
        em.persist(computers);
        em.persist(laptops);
        em.persist(gaming);
        em.persist(programmingBook);
        em.persist(gameOST);
        em.flush();
        em.clear();

        // then
        Category foundElectronics = em.find(Category.class, electronics.getId());
        Category foundComputers = em.find(Category.class, computers.getId());
        Category foundGaming = em.find(Category.class, gaming.getId());
        Item foundBook = em.find(Item.class, programmingBook.getId());
        
        // 계층 구조 검증
        assertThat(foundElectronics.getChild()).hasSize(1);
        assertThat(foundComputers.getParent()).isEqualTo(foundElectronics);
        
        // 다중 카테고리 소속 검증
        assertThat(foundBook.getCategories()).hasSize(2); // computers + gaming
        assertThat(foundComputers.getItems()).hasSize(1);
        assertThat(foundGaming.getItems()).hasSize(2); // gameOST + programmingBook
        
        log.info("전자제품 하위 카테고리 수: {}", foundElectronics.getChild().size());
        log.info("프로그래밍 책이 속한 카테고리 수: {}", foundBook.getCategories().size());
        log.info("게임 카테고리의 아이템 수: {}", foundGaming.getItems().size());
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    @Rollback(value = false)
    void testDeleteCategory() {
        // given
        Category category = new Category();
        category.setName("임시 카테고리");
        
        em.persist(category);
        em.flush();
        
        Long categoryId = category.getId();
        assertThat(em.find(Category.class, categoryId)).isNotNull();
        
        // when
        em.remove(category);
        em.flush();
        em.clear();
        
        // then
        Category deletedCategory = em.find(Category.class, categoryId);
        assertThat(deletedCategory).isNull();
        
        log.info("카테고리가 성공적으로 삭제되었습니다.");
    }
}
