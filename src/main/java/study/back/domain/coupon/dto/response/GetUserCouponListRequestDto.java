package study.back.domain.coupon.dto.response;

import lombok.Getter;
import study.back.utils.ResponseDto;
import study.back.utils.item.UserCouponView;

import java.util.List;

@Getter
public class GetUserCouponListRequestDto extends ResponseDto {
    private List<UserCouponView> userCouponViewList;

    public GetUserCouponListRequestDto(String code, String message, List<UserCouponView> userCouponViewList) {
        super(code, message);
        this.userCouponViewList = userCouponViewList;
    }
}