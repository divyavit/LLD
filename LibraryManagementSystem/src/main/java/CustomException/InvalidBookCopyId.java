package CustomException;

public class InvalidBookCopyId extends Exception{

    public InvalidBookCopyId(String message) {
        super(message);
    }
}
