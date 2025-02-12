package study.back.domain.user.dto.response;

import lombok.*;
import study.back.utils.ResponseDto;
import study.back.utils.item.UserDeliveryInfo;

import java.util.List;

@Getter
public class GetAllUserDeliveryInfoResponseDto extends ResponseDto {
    private List<UserDeliveryInfo> userDeliveryInfoList;

    public GetAllUserDeliveryInfoResponseDto(String code, String message, List<UserDeliveryInfo> userDeliveryInfoList) {
        super(code, message);
        this.userDeliveryInfoList = userDeliveryInfoList;
    }

    public GetAllUserDeliveryInfoResponseDto(List<UserDeliveryInfo> userDeliveryInfoList) {
        this("SU", "모든 배송 정보 가져오기 성공.", userDeliveryInfoList);
    }
}
