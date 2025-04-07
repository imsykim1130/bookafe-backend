package study.back.domain.book.service;

import study.back.dto.response.GetBookFavoriteInfoResponseDto;
import study.back.dto.response.GetFavoriteBookListResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.Top10View;

import java.util.List;

public interface BookFavoriteService {
    GetBookFavoriteInfoResponseDto getBookFavoriteInfo(UserEntity user, String isbn);
    void putBookToFavorite(UserEntity user, String isbn);
    void deleteBookFromFavorite(UserEntity user, String isbn);
    GetFavoriteBookListResponseDto getFavoriteBookList(UserEntity user, Integer page, Integer size);
    List<Top10View> getTop10BookList();
    int deleteBookListFromFavorite(UserEntity user, List<String> isbnList);
}
