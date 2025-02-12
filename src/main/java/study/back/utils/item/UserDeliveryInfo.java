package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeliveryInfo {
    private String name;
    private Boolean isDefault;
    private String receiver;
    private String receiverPhoneNumber;
    private String address;
    private String addressDetail;
}
