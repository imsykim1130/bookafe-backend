package study.back.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.repository.resultSet.PointLogView;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetPointLogResponse {
    private boolean isFirst;
    private boolean isLast;
    private List<PointLogView> pointLogList;
}
