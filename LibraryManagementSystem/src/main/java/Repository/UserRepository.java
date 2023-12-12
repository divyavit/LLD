package Repository;

import CustomException.UserAlreadyExistsException;
import Model.User;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    public static Map<String, User> userIdsMap = new HashMap<>();

    public static void addUser(User user) throws UserAlreadyExistsException{
        if(!ObjectUtils.isEmpty(user) && !userIdsMap.containsKey(user.getId()))
            userIdsMap.put(user.getId(), user);
        else throw new UserAlreadyExistsException("User Already exists");
    }
}
