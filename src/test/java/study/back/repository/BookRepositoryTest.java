package study.back.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.back.repository.origin.BookRepository;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

}