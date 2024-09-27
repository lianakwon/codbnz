package we.cod.bnz.today.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import we.cod.bnz.today.common.exception.AccountException;
import we.cod.bnz.today.common.exception.AccountNotFoundException;
import we.cod.bnz.today.common.exception.BoardNotFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<String> handleAccountException(AccountException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<String> handleBoardNotFoundException(BoardNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
