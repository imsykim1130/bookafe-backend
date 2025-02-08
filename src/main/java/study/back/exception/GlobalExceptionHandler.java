package study.back.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import study.back.utils.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.back.exception.errors.*;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    // @requestBody validation 실패
    // default RBVF
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 모든 검증 메시지를 문자열로 변환하여 합치기
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage) // 각 필드의 검증 실패 메시지 가져오기
                .collect(Collectors.joining(", ")); // 콤마(,)로 메시지 합치기

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseDto responseDto = ResponseDto.builder().code("RBVF").message(message).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // @requestParam validation 실패
    // 스프링부트 3.x.x 부터 해당 예외로 변경됨. 2.x.x 는 ConstraintViolationException 예외 발생.
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ResponseDto> handleMethodValidationException(HandlerMethodValidationException e) {
        // 모든 검증 메시지를 문자열로 변환하여 합치기
        String message = e.getAllValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)  // 각 필드의 검증 실패 메시지 가져오기
                .collect(Collectors.joining(", "));  // 콤마(,)로 메시지 합치기

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseDto responseDto = ResponseDto.builder().code("RPVF").message(message).build();
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
    // ISE
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ResponseDto> handleInternalServerErrorException(InternalServerErrorException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = ResponseDto.builder().code("ISE").message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }

    // 그 외 런타임 오류
    // RT
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleRuntimeException(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = ResponseDto.builder().code("RT").message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }
}
