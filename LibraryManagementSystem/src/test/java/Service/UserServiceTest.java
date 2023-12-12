package Service;

import CustomException.UserAlreadyExistsException;
import Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private UserService userService;

    @BeforeAll
    public void setUp(){
        userService = new UserService();
    }

    @Test
    public void shouldAddUser() throws UserAlreadyExistsException {
        userService.addUser("user1", "Uone", "uone@gmail.com", 5);
        Assertions.assertEquals(1, UserRepository.userIdsMap.size());
        Assertions.assertEquals("Uone", UserRepository.userIdsMap.get("user1").getName());
    }
}
