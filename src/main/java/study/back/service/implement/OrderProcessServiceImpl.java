package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.dto.item.PriceCountView;
import study.back.dto.request.CreateOrderRequestDto;
import study.back.entity.*;
import study.back.exception.NotValidTotalPriceException;
import study.back.exception.PointAndCouponConflictException;
import study.back.repository.origin.BookCartRepository;
import study.back.repository.origin.OrderRepository;
import study.back.service.OrderProcessService;
import study.back.service.PointService;
import study.back.service.UserCouponService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderProcessServiceImpl implements OrderProcessService {
    private final OrderServiceImpl orderService;
    private final OrderBookServiceImpl orderBookService;
    private final PointService pointService;
    private final UserCouponService userCouponService;

    private final OrderRepository orderRepository;
    private final BookCartRepository bookCartRepository;

    @Override
    public OrderEntity createOrder(UserEntity user, CreateOrderRequestDto request) {
        //// 검증
        // 총 가격 검증
        if(request.getTotalPrice() == 0) {
            throw new NotValidTotalPriceException("총 금액이 0 입니다");
        }

        // 포인트와 쿠폰 사용 동시에 불가능
        if(request.getUsedPoint() != null && request.getCouponId() != null) {
            throw new PointAndCouponConflictException("포인트와 쿠폰 동시 사용 불가능");
        }

        // 프론트에서 계산된 가격 검증
        List<PriceCountView> priceCountViews = validateTotalPrice(request.getCartBookIdList(), request.getTotalPrice());

        // 포인트 사용

        int discountedPrice = 0;
        LocalDateTime now = LocalDateTime.now();

        if(request.getUsedPoint() != null) {
            pointService.savePoint(user, -request.getUsedPoint(), now);
            discountedPrice = request.getUsedPoint();
        }

        // 쿠폰 사용
        if(request.getCouponId() != null) {
            UserCouponEntity userCoupon = userCouponService.getUserCoupon(request.getCouponId());
            discountedPrice = request.getTotalPrice() * userCoupon.getCoupon().getDiscountPercent() / 100;

            if(request.getDiscountPrice() != discountedPrice) {
                throw new NotValidTotalPriceException("할인가격을 다시 확인해주세요");
            }
            userCoupon.updatePending();
        }

        // 주문 저장
        boolean isDiscounted = discountedPrice > 0;

        OrderEntity savedOrder = orderService.saveOrder(
                user.getId(),
                request.getAddress(),
                request.getAddressDetail(),
                request.getPhoneNumber(),
                request.getTotalPrice() - discountedPrice,
                now,
                isDiscounted
                );

        // 주문 책 저장
        orderBookService.saveOrderBookList(savedOrder, priceCountViews);

        return savedOrder;
    }

    private List<PriceCountView> validateTotalPrice(List<Long> cartBookIdList, Integer totalPrice) {
        List<PriceCountView> priceCountDiscountPercentList =
                bookCartRepository.findPriceCountDiscountPercentByIdList(cartBookIdList);

        Integer calculatedPrice = priceCountDiscountPercentList.stream().map(priceCountView -> {
            Integer count = priceCountView.getCount();
            Integer price = priceCountView.getPrice();
            Integer discountPercent = priceCountView.getDiscountPercent();
            return price * count * (100 - discountPercent) / 100;
        }).reduce(0, Integer::sum);

        boolean isValidPrice = calculatedPrice.equals(totalPrice);

        if(!isValidPrice) {
            throw new NotValidTotalPriceException("총 가격을 다시 확인해주세요");
        }
        return priceCountDiscountPercentList;
    }

}
