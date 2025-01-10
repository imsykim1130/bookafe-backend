package study.back.service;

import study.back.dto.item.PriceCountView;
import study.back.entity.OrderBookEntity;
import study.back.entity.OrderEntity;

import java.util.List;
import java.util.Map;

public interface OrderBookService {
    List<OrderBookEntity>  saveOrderBookList(OrderEntity order, List<PriceCountView> priceCountViewList);
    int getTotalPrice(OrderEntity order);
    List<OrderBookEntity> changeOrderBookCount(OrderEntity order, Map<String, Integer> orderBookMap);

}
