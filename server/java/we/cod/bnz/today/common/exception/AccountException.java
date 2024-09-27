package we.cod.bnz.today.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class AccountException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public AccountException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
