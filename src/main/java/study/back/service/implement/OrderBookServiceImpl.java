package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.back.dto.item.PriceCountView;
import study.back.entity.OrderBookEntity;
import study.back.entity.OrderEntity;
import study.back.entity.OrderStatus;
import study.back.exception.DeliveryAlreadyDoneException;
import study.back.exception.DeliveryAlreadyStartedException;
import study.back.exception.OrderNotFoundException;
import study.back.repository.BookRepository;
import study.back.repository.OrderBookRepository;
import study.back.service.OrderBookService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderBookServiceImpl implements OrderBookService {
    private final OrderBookRepository orderBookRepository;
    private final BookRepository bookRepository;

    @Override
    public List<OrderBookEntity> saveOrderBookList(OrderEntity order, List<PriceCountView> priceCountViewList) {

        List<OrderBookEntity> orderBookList = priceCountViewList
                .stream().map(priceCountView -> OrderBookEntity.builder()
                .order(order)
                .count(priceCountView.getCount())
                .book(priceCountView.getBook())
                .build()).toList();

        return orderBookRepository.saveAll(orderBookList);
    }

    @Override
    public int getTotalPrice(OrderEntity order) {
        if(order == null) {
            throw new OrderNotFoundException("해당 주문이 존재하지 않습니다");
        }
        return orderBookRepository.getTotalPrice(order);
    }

    @Override
    public List<OrderBookEntity> changeOrderBookCount(OrderEntity order, Map<String, Integer> orderBookMap) {
        // order 상태 검증
        if(order == null) {
            throw new OrderNotFoundException("해당 주문이 존재하지 않습니다");
        }

        if(order.getOrderStatus().equals(OrderStatus.DELIVERING)){
            throw new DeliveryAlreadyStartedException("이미 배송이 시작되어 수량 변경이 불가능합니다");
        }

        if(order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            throw new DeliveryAlreadyDoneException("이미 배송이 완료되어 수량 변경이 불가능합니다");
        }

        orderBookRepository.findAllByOrder(order).stream().forEach(orderBook -> {
            String isbn = orderBook.getBook().getIsbn();
            int count = orderBookMap.get(isbn);
            if(count == 0) {
                orderBookRepository.delete(orderBook);
            }
            orderBook.changeCount(count);
        });

       return orderBookRepository.findAllByOrder(order);
    }
}
