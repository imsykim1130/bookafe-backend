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

//    @ExceptionHandler(DeliveryAlreadyDoneException.class)
//    public ResponseEntity<ResponseDto> handleDeliveryAlreadyDoneException(DeliveryAlreadyDoneException e) {
//        ResponseDto responseDto = new ResponseDto("DAD", e.getMessage());
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
//    }
//
//    @ExceptionHandler(PointAndCouponConflictException.class)
//    public ResponseEntity<ResponseDto> handlePointAndCouponConflictException(PointAndCouponConflictException e) {
//        ResponseDto responseDto = new ResponseDto("PCC", e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
//    }
//
//    @ExceptionHandler(NoCommentContentException.class)
//    public ResponseEntity<ResponseDto> handleNoCommentContentException(NoCommentContentException e) {
//        ResponseDto responseDto = new ResponseDto("NCC", e.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
//    }
//
//    @ExceptionHandler(NotFoundBookException.class)
//    public ResponseEntity<ResponseDto> handleNotFoundBookException(NotFoundBookException e) {
//        log.error("NotFoundBookException: {}", e.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ResponseDto.builder()
//                        .code("NFB")
//                        .message(e.getMessage())
//                        .build());
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ResponseDto> CustomRuntimeException(RuntimeException e) {
//        ResponseDto responseDto = new ResponseDto("ISE", e.getMessage());
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
//    }

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

    // 500 Internal Server Error
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ResponseDto> handleInternalServerErrorException(InternalServerErrorException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseDto responseDto = ResponseDto.builder().code(String.valueOf(status)).message(e.getMessage()).build();
        return ResponseEntity.status(status).body(responseDto);
    }
}
