package app.magiavventure.user.operation;

import app.magiavventure.user.model.Category;
import app.magiavventure.user.model.CreateUser;
import app.magiavventure.user.model.UpdateUser;
import app.magiavventure.user.model.User;
import app.magiavventure.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("User operation tests")
class UserOperationTest {

    @InjectMocks
    private UserOperation userOperation;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Create user api test")
    void createUser_ok() {
        Category category = Category
                .builder()
                .name("name")
                .id(UUID.randomUUID())
                .background("background")
                .build();
        CreateUser createUser = CreateUser
                .builder()
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        User returnedUser = User
                .builder()
                .id(UUID.randomUUID())
                .name("name")
                .preferredCategories(List.of(category))
                .build();

        Mockito.when(userService.createUser(createUser))
                .thenReturn(returnedUser);

        User user = userOperation.createUser(createUser);

        Mockito.verify(userService).createUser(createUser);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(createUser.getName(), user.getName());
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(createUser.getPreferredCategories(), user.getPreferredCategories());
    }

    @Test
    @DisplayName("Find all users api test")
    void findAllUsers_ok() {
        Category category = Category
                .builder()
                .name("name")
                .id(UUID.randomUUID())
                .background("background")
                .build();
        User returnedUser = User
                .builder()
                .id(UUID.randomUUID())
                .name("name")
                .preferredCategories(List.of(category))
                .build();

        Mockito.when(userService.findAll())
                .thenReturn(List.of(returnedUser));

        List<User> users = userOperation.findAllUser();

        Mockito.verify(userService).findAll();

        Assertions.assertNotNull(users);
        users.stream().findFirst()
                        .ifPresent(user -> {
                            Assertions.assertEquals(returnedUser.getName(), user.getName());
                            Assertions.assertNotNull(user.getId());
                            Assertions.assertEquals(returnedUser.getPreferredCategories(),
                                    user.getPreferredCategories());
                        });
    }

    @Test
    @DisplayName("Find user by ID api test")
    void findUserById_ok() {
        UUID id = UUID.randomUUID();
        Category category = Category
                .builder()
                .name("name")
                .id(UUID.randomUUID())
                .background("background")
                .build();
        User returnedUser = User
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();

        Mockito.when(userService.findById(id))
                .thenReturn(returnedUser);

        User user = userOperation.findUser(id);

        Mockito.verify(userService).findById(id);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(returnedUser.getName(), user.getName());
        Assertions.assertEquals(id, user.getId());
        Assertions.assertEquals(returnedUser.getPreferredCategories(), user.getPreferredCategories());
    }

    @Test
    @DisplayName("Check name user api test")
    void checkNameUser_ok() {
        Mockito.doNothing().when(userService).checkIfUserExists("name");

        userOperation.checkName("name");

        Mockito.verify(userService).checkIfUserExists("name");
    }

    @Test
    @DisplayName("Update user api test")
    void updateUser_ok() {
        UUID id = UUID.randomUUID();
        Category category = Category
                .builder()
                .name("name")
                .id(UUID.randomUUID())
                .background("background")
                .build();
        UpdateUser updateUser = UpdateUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        User returnedUser = User
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();

        Mockito.when(userService.updateUser(updateUser))
                .thenReturn(returnedUser);

        User user = userOperation.updateUser(updateUser);

        Mockito.verify(userService).updateUser(updateUser);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(updateUser.getName(), user.getName());
        Assertions.assertEquals(updateUser.getId(), user.getId());
        Assertions.assertEquals(updateUser.getPreferredCategories(), user.getPreferredCategories());
    }

    @Test
    @DisplayName("Delete user api test")
    void deleteUser_ok() {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(userService).deleteById(id);

        userOperation.deleteUser(id);

        Mockito.verify(userService).deleteById(id);
    }
}
