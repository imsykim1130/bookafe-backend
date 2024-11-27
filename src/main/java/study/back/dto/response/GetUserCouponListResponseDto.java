package study.back.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;

@Getter
public class GetUserCouponListResponseDto extends ResponseDto {
    private List<UserCouponView> couponList;

    public static ResponseEntity<GetUserCouponListResponseDto> success(List<UserCouponView> couponList) {
        GetUserCouponListResponseDto responseBody = new GetUserCouponListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "유저의 쿠폰 리스트 받기 성공";
        responseBody.couponList = couponList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}



