package study.back.domain.point.entity;

import lombok.Getter;

@Getter
public enum PointType {
    ALL("전체"),
    USE("사용"),
    EARN("적립");

    private final String text;

    PointType(String text) {
        this.text = text;
    }

}
