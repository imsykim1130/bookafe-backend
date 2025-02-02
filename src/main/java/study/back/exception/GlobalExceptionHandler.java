package study.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import study.back.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.back.exception.errors.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseDto responseDto = ResponseDto.builder().code(String.valueOf(status)).message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 401 Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseDto responseDto = ResponseDto.builder().code(String.valueOf(status)).message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 403 Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto> handleForbiddenException(ForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ResponseDto responseDto = ResponseDto.builder().code(String.valueOf(status)).message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 404 Not Found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ResponseDto responseDto = ResponseDto.builder().code(String.valueOf(status)).message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 409
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseDto> handleConflictException(ConflictException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        ResponseDto responseDto = ResponseDto.builder().code(String.valueOf(status)).message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 500 Internal Server Error
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ResponseDto> handleInternalServerErrorException(InternalServerErrorException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = ResponseDto.builder().code(String.valueOf(status)).message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }
}
