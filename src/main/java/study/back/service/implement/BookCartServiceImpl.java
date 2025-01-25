package study.back.service.implement;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.back.dto.item.CartBookView;
import study.back.entity.BookCartEntity;
import study.back.exception.NotFoundBookException;
import study.back.user.entity.UserEntity;
import study.back.repository.BookCartRepositoryInterface;
import study.back.repository.impl.BookCartRepoImpl;
import study.back.repository.origin.BookCartRepository;
import study.back.repository.origin.BookRepository;
import study.back.service.BookCartService;

import java.util.List;


@Service
@Transactional
public class BookCartServiceImpl implements BookCartService {
    private BookCartRepositoryInterface repository;

    @Autowired
    public BookCartServiceImpl(BookRepository bookRepository, BookCartRepository bookCartRepository, EntityManager em) {
        this.repository = new BookCartRepoImpl(bookRepository, bookCartRepository, em);
    }

    // 장바구니 담기
    public void putBookToCart(UserEntity user, String isbn) {
        System.out.println("장바구니 담기");
        // 책 여부 검증
        repository.getBookByIsbn(isbn).orElseThrow(() -> new NotFoundBookException("책이 존재하지 않습니다"));

        // 장바구니 여부 검증
        boolean isAlreadyCartBook = repository.existsByIsbn(isbn);
        if (isAlreadyCartBook) {
            throw new RuntimeException("이미 장바구니에 존재하는 책입니다");
        }
        BookCartEntity bookCart = BookCartEntity.createBookCart(user, isbn);
        repository.save(bookCart);
    }

    // 장바구니 빼기
    public void deleteBookFromCart(String isbn) {
        System.out.println("장바구니 빼기");
        BookCartEntity bookCart = repository.findByIsbn(isbn).orElseThrow(() -> new RuntimeException("이미 장바구니에 없는 책입니다"));
        repository.delete(bookCart);
    }

    // 장바구니 여부
    @Override
    public boolean isCart(String isbn, UserEntity user) {
        System.out.println("장바구니 여부");
        return repository.existsBookCart(isbn, user);
    }

    // 장바구니 책 리스트 가져오기
    @Override
    public List<CartBookView> getBookCartList(UserEntity user) {
        System.out.println("장바구니 책 리스트 가져오기");
        return repository.findCartBookViewListByUser(user);
    }

    // 장바구니 책 여러개 동시 삭제
    @Override
    public void deleteList(List<Long> cartBookIdList) {
        System.out.println("장바구니 책 여러개 동시 삭제");
        repository.deleteAllByIdList(cartBookIdList);
    }

    // 장바구니 책 수량 변경
    @Override
    public void changeCartBookCount(UserEntity user, String isbn, Integer count) {
        System.out.println("장바구니 책 수량 변경");
        if(count <= 0) {
            throw new RuntimeException("잘못된 수량입니다");
        }
        BookCartEntity bookCart = repository.findByIsbnAndUser(isbn, user).orElseThrow(() -> new RuntimeException("장바구니에 존재하지 않습니다"));
        bookCart.changeCount(count);
    }
}
