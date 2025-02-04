package study.back.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterCouponRequestDto{
	private Integer discountPercent;
	private String name;
}