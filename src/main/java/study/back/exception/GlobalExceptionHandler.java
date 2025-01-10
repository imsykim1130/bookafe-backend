package study.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import study.back.dto.response.ResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeliveryAlreadyDoneException.class)
    public ResponseEntity<ResponseDto> handleDeliveryAlreadyDoneException(DeliveryAlreadyDoneException e) {
        ResponseDto responseDto = new ResponseDto("DAD", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
    }

    @ExceptionHandler(DeliveryAlreadyStartedException.class)
    public ResponseEntity<ResponseDto> handleDeliveryAlreadyStartedException(DeliveryAlreadyStartedException e) {
        ResponseDto responseDto = new ResponseDto("DAS", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
    }

    @ExceptionHandler(NotValidTotalPriceException.class)
    public ResponseEntity<ResponseDto> handleNotValidTotalPriceException(NotValidTotalPriceException e) {
        ResponseDto responseDto = new ResponseDto("NT", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(PointAndCouponConflictException.class)
    public ResponseEntity<ResponseDto> handlePointAndCouponConflictException(PointAndCouponConflictException e) {
        ResponseDto responseDto = new ResponseDto("PCC", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> CustomRuntimeException(RuntimeException e) {
        ResponseDto responseDto = new ResponseDto("ERR", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }
}
