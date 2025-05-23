package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserManagementInfo {
    private Long id;
    private String email;
    private String datetime;
    private Long commentCount;
}
