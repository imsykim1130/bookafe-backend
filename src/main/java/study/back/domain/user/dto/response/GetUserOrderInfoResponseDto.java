package study.back.domain.user.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import study.back.utils.ResponseDto;
import study.back.utils.item.UserOrderInfo;

@Getter
public class GetUserOrderInfoResponseDto extends ResponseDto {
    private UserOrderInfo userOrderInfo;

    public GetUserOrderInfoResponseDto(String code, String message, UserOrderInfo userOrderInfo) {
        super(code, message);
        this.userOrderInfo = userOrderInfo;
    }

    public static ResponseEntity<GetUserOrderInfoResponseDto> success(
            UserOrderInfo userOrderInfo
    ) {
        GetUserOrderInfoResponseDto responseBody = new GetUserOrderInfoResponseDto(
                "SU",
                "유저 배송 정보 가져오기 성공",
                userOrderInfo
        );
        return ResponseEntity.ok(responseBody);
    }
}
