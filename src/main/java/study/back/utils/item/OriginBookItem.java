package study.back.utils.item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OriginBookItem {
    private Meta meta;
    private List<BookItem> documents;
}
