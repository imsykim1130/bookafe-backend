package study.back.utils.item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Meta {
    private Boolean is_end;
    private Integer pageable_count;
    private Integer total_count;
}
