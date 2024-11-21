package study.back.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import study.back.entity.UserEntity;

@Getter
public class GetUserOrderInfoResponseDto extends ResponseDto {
    private String address;
    private String addressDetail;
    private String phoneNumber;

    public static ResponseEntity<GetUserOrderInfoResponseDto> success(UserEntity userEntity) {
        GetUserOrderInfoResponseDto responseBody = new GetUserOrderInfoResponseDto();
        responseBody.code = "SU";
        responseBody.message = "유저 배송 정보 가져오기 성공";
        responseBody.address = userEntity.getAddress();
        responseBody.addressDetail = userEntity.getAddressDetail();
        responseBody.phoneNumber = userEntity.getPhoneNumber();
        return ResponseEntity.ok(responseBody);
    }
}
