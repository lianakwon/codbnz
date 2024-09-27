package we.cod.bnz.today.common.exception;

public class BoardNotFoundException extends RuntimeException {

    public BoardNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}