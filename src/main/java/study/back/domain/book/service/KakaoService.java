package study.back.domain.book.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import study.back.exception.InternalServerError.KakaoAuthorizationException;
import study.back.utils.item.OriginBookItem;


@Service
@Transactional
public class KakaoService {
    private final String kakaoAuthorization;

    public KakaoService(@Value("${kakao-authorization}") String kakaoAuthorization) {
        this.kakaoAuthorization = kakaoAuthorization;
    }

    // 카카오 api 에서 책 정보 가져오기
    public OriginBookItem getBookDataFromKakaoApi(String query, String sort, String page, String size, String target) throws KakaoAuthorizationException {
        OriginBookItem result;
        RestClient restClient = RestClient.builder()
                .baseUrl("https://dapi.kakao.com/v3/search/book")
                .defaultHeader("Authorization", kakaoAuthorization)
                .build();

        result = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", query)
                        .queryParam("sort", sort)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("target", target)
                        .build())
                .retrieve()
                .body(OriginBookItem.class);
        return result;
    }
}
