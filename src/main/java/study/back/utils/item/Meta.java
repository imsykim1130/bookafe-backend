package study.back.utils.item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private Boolean is_end;
    private Integer pageable_count;
    private Integer total_count;
}
