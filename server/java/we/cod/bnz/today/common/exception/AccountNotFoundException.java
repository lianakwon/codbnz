package we.cod.bnz.today.common.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}