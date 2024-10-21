package exceptions;

public class ResponseException extends RuntimeException {
    final private int code;
    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getStatusCode(){
        return code;
    }
}
