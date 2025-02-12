package study.back.domain.user.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import study.back.utils.ResponseDto;
import study.back.utils.item.UserDeliveryInfo;

@Getter
public class GetUserOrderInfoResponseDto extends ResponseDto {
    private UserDeliveryInfo userDeliveryInfo;

    public GetUserOrderInfoResponseDto(String code, String message, UserDeliveryInfo userDeliveryInfo) {
        super(code, message);
        this.userDeliveryInfo = userDeliveryInfo;
    }

    public static ResponseEntity<GetUserOrderInfoResponseDto> success(
            UserDeliveryInfo userDeliveryInfo
    ) {
        GetUserOrderInfoResponseDto responseBody = new GetUserOrderInfoResponseDto(
                "SU",
                "유저 배송 정보 가져오기 성공",
                userDeliveryInfo
        );
        return ResponseEntity.ok(responseBody);
    }
}
