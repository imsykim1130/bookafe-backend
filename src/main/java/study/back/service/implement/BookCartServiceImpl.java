package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.dto.item.CartBookView;
import study.back.entity.BookCartEntity;
import study.back.entity.UserEntity;
import study.back.exception.NotExistBookException;
import study.back.repository.BookCartRepositoryInterface;
import study.back.service.BookCartService;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class BookCartServiceImpl implements BookCartService {
    private final BookCartRepositoryInterface bookCartRepository;

    // 장바구니 담기
    public void putBookToCart(UserEntity user, String isbn) {
        System.out.println("장바구니 담기");
        // 책 여부 검증
        bookCartRepository.getBookByIsbn(isbn).orElseThrow(() -> new NotExistBookException("책이 존재하지 않습니다"));

        // 장바구니 여부 검증
        boolean isAlreadyCartBook = bookCartRepository.existsByIsbn(isbn);
        if (isAlreadyCartBook) {
            throw new RuntimeException("이미 장바구니에 존재하는 책입니다");
        }
        BookCartEntity bookCart = BookCartEntity.createBookCart(user, isbn);
        bookCartRepository.save(bookCart);
    }

    // 장바구니 빼기
    public void deleteBookFromCart(String isbn) {
        System.out.println("장바구니 빼기");
        BookCartEntity bookCart = bookCartRepository.findByIsbn(isbn).orElseThrow(() -> new RuntimeException("이미 장바구니에 없는 책입니다"));
        bookCartRepository.delete(bookCart);
    }

    // 장바구니 여부
    @Override
    public boolean isCart(String isbn) {
        System.out.println("장바구니 여부");
        return bookCartRepository.existsByIsbn(isbn);
    }

    // 장바구니 책 리스트 가져오기
    @Override
    public List<CartBookView> getBookCartList(UserEntity user) {
        System.out.println("장바구니 책 리스트 가져오기");
        return bookCartRepository.findCartBookViewListByUser(user);
    }

    // 장바구니 책 여러개 동시 삭제
    @Override
    public void deleteList(List<Long> cartBookIdList) {
        System.out.println("장바구니 책 여러개 동시 삭제");
        bookCartRepository.deleteAllByIdList(cartBookIdList);
    }

    // 장바구니 책 수량 변경
    @Override
    public void changeCartBookCount(UserEntity user, String isbn, Integer count) {
        System.out.println("장바구니 책 수량 변경");
        if(count <= 0) {
            throw new RuntimeException("잘못된 수량입니다");
        }
        BookCartEntity bookCart = bookCartRepository.findByIsbnAndUser(isbn, user).orElseThrow(() -> new RuntimeException("장바구니에 존재하지 않습니다"));
        bookCart.changeCount(count);
//        bookCartRepository.save(bookCart);
    }
}
