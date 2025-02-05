package study.back.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterCouponRequestDto {
	@NotNull(message = "IVDP 할인율은 필수 입력사항입니다.")
	@Min(value = 1, message = "IVDP 할인율은 1 이상이어야 합니다.")
	@Max(value = 100, message = "IVDP 할인율은 100 이하여야 합니다.")
	private Integer discountPercent;

	@NotBlank(message = "ICN 쿠폰 이름을 입력해주세요.")
	private String name;
}