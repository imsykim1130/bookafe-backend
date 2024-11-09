package study.back.dto.item;
import lombok.Getter;

import java.util.List;

@Getter
public class OriginBookItem {
    private Meta meta;
    private List<BookItem> documents;
}
