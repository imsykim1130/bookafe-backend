package study.back.domain.book.service;

import study.back.global.dto.response.GetBookFavoriteInfoResponseDto;
import study.back.global.dto.response.GetFavoriteBookListResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.query.Top10BookQueryDto;

import java.util.List;

public interface BookFavoriteService {
    GetBookFavoriteInfoResponseDto getBookFavoriteInfo(UserEntity user, String isbn);
    void putBookToFavorite(UserEntity user, String isbn);
    void deleteBookFromFavorite(UserEntity user, String isbn);
    GetFavoriteBookListResponseDto getFavoriteBookList(UserEntity user, Integer page, Integer size);
    List<Top10BookQueryDto> getTop10BookList();
    int deleteBookListFromFavorite(UserEntity user, List<String> isbnList);
}
