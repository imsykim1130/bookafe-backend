package study.back.domain.point.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class StringToPointTypeConverter implements Converter<String, PointType> {
    @Override
    public PointType convert(@NonNull String value) {
        return switch (value) {
            case "전체" -> PointType.ALL;
            case "사용" -> PointType.USE;
            case "적립" -> PointType.EARN;
            default -> throw new IllegalArgumentException("잘못된 포인트 타입 값입니다.");
        };
    }
}
