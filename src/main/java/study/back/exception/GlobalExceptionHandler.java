package study.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import study.back.utils.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.back.exception.errors.*;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 Bad Request
    // default BR
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getMessage();
        String code = message.split(" ")[0];
        ResponseDto responseDto = ResponseDto.builder().code(code).message(message).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 401 Unauthorized
    // default UA
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = e.getMessage();
        String code = message.split(" ")[0];
        ResponseDto responseDto = ResponseDto.builder().code(code).message(message).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 403 Forbidden
    // default FB
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResponseDto> handleForbiddenException(ForbiddenException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String message = e.getMessage();
        String code = message.split(" ")[0];
        ResponseDto responseDto = ResponseDto.builder().code(code).message(message).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 404 Not Found
    // default NF
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handleNotFoundException(NotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = e.getMessage();
        String code = message.split(" ")[0];
        ResponseDto responseDto = ResponseDto.builder().code(code).message(message).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 409
    // default CF
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseDto> handleConflictException(ConflictException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        String message = e.getMessage();
        String code = message.split(" ")[0];
        ResponseDto responseDto = ResponseDto.builder().code(code).message(message).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 유효성 검증 실패
    // default MANV
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();

        String message;
        if(Objects.isNull(fieldError)) {
            message = "MANV 유효성 검증 실패";
        } else {
            message = fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "MANV 유효성 검증 실패" ; // validation 에서 설정한 message
        }

        String code = message.split(" ")[0];

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseDto responseDto = ResponseDto.builder().code(code).message(message).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // path variable 없음
    // MPV
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ResponseDto> handleMissingPathVariableException(MissingPathVariableException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseDto responseDto = ResponseDto.builder().code("MPV").message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 잘못된 요청 주소
    // NRF
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDto> handleNoResourceFoundException(NoResourceFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ResponseDto responseDto = ResponseDto.builder().code("NRF").message("요청 주소를 다시 확인해주세요. " + e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 500 Internal Server Error
    // IS
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ResponseDto> handleInternalServerErrorException(InternalServerErrorException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = ResponseDto.builder().code("IS").message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }
}
