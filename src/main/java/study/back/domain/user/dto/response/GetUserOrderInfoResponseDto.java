package study.back.domain.user.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.ResponseDto;

@Getter
public class GetUserOrderInfoResponseDto extends ResponseDto {
    private String address;
    private String addressDetail;
    private String phoneNumber;

    public GetUserOrderInfoResponseDto(String code, String message, String address, String addressDetail, String phoneNumber) {
        super(code, message);
        this.address = address;
        this.addressDetail = addressDetail;
        this.phoneNumber = phoneNumber;
    }

    public static ResponseEntity<GetUserOrderInfoResponseDto> success(UserEntity userEntity) {
        GetUserOrderInfoResponseDto responseBody = new GetUserOrderInfoResponseDto(
                "SU",
                "유저 배송 정보 가져오기 성공",
                userEntity.getAddress(),
                userEntity.getAddressDetail(),
                userEntity.getPhoneNumber()
        );
        return ResponseEntity.ok(responseBody);
    }
}
