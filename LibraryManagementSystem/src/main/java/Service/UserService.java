package Service;

import CustomException.UserAlreadyExistsException;
import CustomException.UserDoesNotExists;
import Model.User;
import Repository.UserRepository;
import org.apache.commons.lang3.StringUtils;

public class UserService {

    private static final int MAX_BOOKS_TO_BORROW = 5;

    public void addUser(String user_id, String name, String emailId, int maxBooksToBorrow) throws UserAlreadyExistsException {
        User user = new User(user_id, name, emailId, maxBooksToBorrow, 0);
        UserRepository.addUser(user);
    }

    public void addUser(String user_id, String name, String emailId) throws UserAlreadyExistsException {
        User user = new User(user_id, name, emailId, MAX_BOOKS_TO_BORROW, 0);
        UserRepository.addUser(user);
    }

    public void updateBorrowCount(String userId){
        if(UserRepository.userIdsMap.containsKey(userId)){
            User user = UserRepository.userIdsMap.get(userId);
            int currCount = user.getBooksBorrowed();
            user.setBooksBorrowed(currCount+1);
        }
    }

    public boolean canUserBorrowBook(String userId) throws UserDoesNotExists {
        User user = getUser(userId);
        int max = user.getMaxBooksToBorrow();
        int curr = user.getBooksBorrowed();
        if(curr +1 <= max) return true;
        else return false;
    }

    public User getUser(String userId) throws UserDoesNotExists {
        User user = null;
        if(UserRepository.userIdsMap.containsKey(userId)){
            user = UserRepository.userIdsMap.get(userId);
        }
        else throw new UserDoesNotExists("User Does not exists");
        return user;
    }
}
