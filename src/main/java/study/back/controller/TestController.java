package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;
import study.back.entity.RoleName;
import study.back.entity.UserEntity;
import study.back.repository.BookRepository;
import study.back.repository.CommentRepository;
import study.back.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


}
