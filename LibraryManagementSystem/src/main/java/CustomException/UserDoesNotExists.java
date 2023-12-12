package CustomException;

public class UserDoesNotExists extends Exception{
    public UserDoesNotExists(String message) {
        super(message);
    }
}
