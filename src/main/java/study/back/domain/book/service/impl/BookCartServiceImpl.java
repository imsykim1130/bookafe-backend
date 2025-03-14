package study.back.domain.book.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import study.back.utils.item.CartBookView;
import study.back.domain.book.entity.BookCartEntity;
import study.back.domain.book.entity.BookEntity;
import study.back.exception.NotFound.NotFoundBookException;
import study.back.domain.book.service.BookService;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.repository.BookCartRepository;
import study.back.domain.book.repository.impl.BookCartRepoImpl;
import study.back.domain.book.repository.jpa.BookCartJpaRepository;
import study.back.domain.book.repository.jpa.BookJpaRepository;
import study.back.domain.book.service.BookCartService;

import java.util.List;


@Service
@Transactional
public class BookCartServiceImpl implements BookCartService {
    private final BookCartRepository repository;
    private final BookService bookService;

    public BookCartServiceImpl(BookJpaRepository bookJpaRepository, BookCartJpaRepository bookCartJpaRepository, EntityManager em, BookServiceImpl bookService) {
        this.repository = new BookCartRepoImpl(bookJpaRepository, bookCartJpaRepository, em);
        this.bookService = bookService;
    }

    // 장바구니 담기
    public void putBookToCart(UserEntity user, String isbn) {
        System.out.println("장바구니 담기");

        // 책 여부 검증
        BookEntity book = bookService.getBookIfExistOrElseNull(isbn);
        if(book == null) {
            throw new NotFoundBookException();
        }

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
