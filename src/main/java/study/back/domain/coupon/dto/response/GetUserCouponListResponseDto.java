package study.back.domain.coupon.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.utils.ResponseDto;
import study.back.utils.item.UserCouponView;

import java.util.List;

@Getter
public class GetUserCouponListResponseDto extends ResponseDto {
    private List<UserCouponView> couponList;

    public GetUserCouponListResponseDto(String code, String message, List<UserCouponView> couponList) {
        super(code, message);
        this.couponList = couponList;
    }

    public static ResponseEntity<GetUserCouponListResponseDto> success(List<UserCouponView> couponList) {
        GetUserCouponListResponseDto responseBody = new GetUserCouponListResponseDto("SU", "유저 쿠폰 리스트 받기 성공", couponList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}



