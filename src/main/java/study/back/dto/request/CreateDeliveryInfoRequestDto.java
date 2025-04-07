package study.back.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateDeliveryInfoRequestDto {
    @NotBlank(message = "이름은 필수 입력사항입니다")
    private String name;
    @NotBlank(message = "주소는 필수 입력사항입니다")
    private String address;
    private String addressDetail;
    @NotBlank(message = "수신자는 필수 입력사항입니다")
    private String receiver;
    @NotBlank(message = "수신자 휴대폰 번호는 필수 입력사항입니다")
    private String receiverPhoneNumber;
    private Boolean isDefault;

    @AssertTrue(message = "상세주소를 정확히 입력하세요")
    public boolean isValidAddressDetail() {
        return addressDetail == null || (addressDetail != null && !addressDetail.isBlank());
    }
}
