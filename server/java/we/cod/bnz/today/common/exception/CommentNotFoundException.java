package we.cod.bnz.today.common.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
