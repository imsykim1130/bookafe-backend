package study.back.domain.point.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.utils.ResponseDto;
import study.back.utils.item.PointLogView;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetPointLogResponse extends ResponseDto {
    private boolean isFirst;
    private boolean isLast;
    private List<PointLogView> pointLogList;

    public GetPointLogResponse(boolean isFirst, boolean isLast, List<PointLogView> pointLogList) {
        this("SU", "포인트 히스토리 가져오기 성공", isFirst, isLast, pointLogList);
    }

    public GetPointLogResponse(String code, String message, boolean isFirst, boolean isLast, List<PointLogView> pointLogList) {
        super(code, message);
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.pointLogList = pointLogList;
    }
}
