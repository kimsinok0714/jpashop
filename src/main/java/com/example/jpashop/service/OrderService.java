package com.example.jpashop.service;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.jpashop.domain.Delivery;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderItem;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.repository.ItemRepository;
import com.example.jpashop.repository.MemberRepository;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    //DI
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    //주문 생성
    // Order 클래스에 OrderItem, Delivery에 대해 cascade = CascadeType.PERSIST 옵션을 설정하였기 때문에
    // 주문 정보가 DB에 저장될 때 주문 항목과 배송지 정보도 함께 등록됩니다.
    @Transactional(readOnly = false)
    public Long order(OrderRequestDto orderRequestDto) {

        Member member = memberRepository.findOne(orderRequestDto.getMemberId());

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        List<OrderItem> orderItems = new ArrayList<>();        
        orderRequestDto.getOrderItems().forEach(orderItem -> {
            Item item = itemRepository.findOne(orderItem.getItemId());           
            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), orderItem.getCount()));
        });

        Order order = Order.createOrder(member, delivery, orderItems);    
        
        orderRepository.save(order);

        return order.getId();        
    }


    
    // 주문 생성
    @Transactional(readOnly = false)
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);

        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        
        List<OrderItem> orderItems = new ArrayList<>();        
        orderRequestDto.getOrderItems().forEach(orderItem -> {
            Item item = itemRepository.findOne(orderItem.getItemId());            
            orderItems.add(OrderItem.createOrderItem(item, item.getPrice(), orderItem.getCount()));
        });
        Order order = Order.createOrder(member, delivery, orderItems);

        // 주문 저장
        // Order 클래스에 OrderItem, Delivery에 대해 cascade = CascadeType.PERSIST 옵션을 설정하였기 때문에
        // 주문 정보가 DB에 저장될 때 주문 항목과 배송지 정보도 함께 등록됩니다.
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    // 주문 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
