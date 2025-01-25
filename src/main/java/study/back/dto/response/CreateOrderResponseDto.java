package study.back.dto.response;

import lombok.*;

@Getter
public class CreateOrderResponseDto extends ResponseDto {
    private int price;

    @Builder
    public CreateOrderResponseDto(String code, String message, int price) {
        super(code, message);
        this.price = price;
    }
}
