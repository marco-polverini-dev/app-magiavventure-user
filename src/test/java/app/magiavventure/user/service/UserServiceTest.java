package app.magiavventure.user.service;

import app.magiavventure.user.entity.EUser;
import app.magiavventure.user.error.UserException;
import app.magiavventure.user.mapper.UserMapper;
import app.magiavventure.user.model.Category;
import app.magiavventure.user.model.CreateUser;
import app.magiavventure.user.model.UpdateUser;
import app.magiavventure.user.model.User;
import app.magiavventure.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@DisplayName("User service tests")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Captor
    private ArgumentCaptor<Example<EUser>> exampleArgumentCaptor;
    @Captor
    private ArgumentCaptor<EUser> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<Sort> sortArgumentCaptor;
    
    @Test
    @DisplayName("Create user with correct info")
    void createUser_withCorrectInfo_ok() {

        Category category = Category
                .builder()
                .id(UUID.randomUUID())
                .background("background")
                .name("name")
                .build();
        CreateUser createUser = CreateUser
                .builder()
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        EUser savedUser = EUser
                .builder()
                .id(UUID.randomUUID())
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        
        Mockito.when(userRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(false);
        Mockito.when(userRepository.save(userArgumentCaptor.capture()))
                        .thenReturn(savedUser);
        
        User user = userService.createUser(createUser);
        
        Mockito.verify(userRepository).exists(exampleArgumentCaptor.capture());
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        
        Assertions.assertNotNull(user);
        Assertions.assertEquals(savedUser.getId(), user.getId());
        Assertions.assertEquals(savedUser.getName(), user.getName());
        Assertions.assertEquals(savedUser.getPreferredCategories(), user.getPreferredCategories());
        Example<EUser> example = exampleArgumentCaptor.getValue();
        Assertions.assertNotNull(example);
        Assertions.assertEquals(example.getProbe().getName(), createUser.getName());
        Assertions.assertEquals(example.getMatcher(), ExampleMatcher.matchingAny());
        EUser userToSave = userArgumentCaptor.getValue();
        Assertions.assertNotNull(userToSave);
        Assertions.assertNotNull(userToSave.getId());
        Assertions.assertEquals(createUser.getName(), userToSave.getName());
        Assertions.assertEquals(createUser.getPreferredCategories(), userToSave.getPreferredCategories());
        Assertions.assertTrue(userToSave.isActive());
    }
    
    @Test
    @DisplayName("Create user but the name already exists")
    void createUser_nameAlreadyExists_throwUserException() {
        
        Category category = Category
                .builder()
                .id(UUID.randomUUID())
                .background("background")
                .name("name")
                .build();
        CreateUser createUser = CreateUser
                .builder()
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        
        Mockito.when(userRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(true);
        
        UserException userException = Assertions.assertThrows(UserException.class,
                () -> userService.createUser(createUser));
        
        Mockito.verify(userRepository).exists(exampleArgumentCaptor.capture());
        
        Assertions.assertNotNull(userException);
        Assertions.assertEquals("user-exists", userException.getUserError().getKey());
        Assertions.assertArrayEquals(new Object[]{createUser.getName()}, userException.getUserError().getArgs());
        Example<EUser> example = exampleArgumentCaptor.getValue();
        Assertions.assertNotNull(example);
        Assertions.assertEquals(example.getProbe().getName(), createUser.getName());
        Assertions.assertEquals(example.getMatcher(), ExampleMatcher.matchingAny());
    }
    
    @Test
    @DisplayName("Find by id with correct id")
    void findById_withCorrectId_ok() {
        UUID id = UUID.randomUUID();
        
        EUser foundUser = EUser
                .builder()
                .id(id)
                .name("name")
                .build();
        
        Mockito.when(userRepository.findById(id))
                        .thenReturn(Optional.of(foundUser));
        
        User user = userService.findById(id);
        
        Mockito.verify(userRepository).findById(id);
        
        Assertions.assertNotNull(user);
        Assertions.assertEquals(id, user.getId());
        Assertions.assertEquals(foundUser.getName(), user.getName());
        
    }
    
    @Test
    @DisplayName("Find by id with wrong id")
    void findById_withWrongId_throwUserException() {
        UUID id = UUID.randomUUID();
        
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        
        UserException userException = Assertions.assertThrows(UserException.class,
                () -> userService.findById(id));
        
        Mockito.verify(userRepository).findById(id);
        
        Assertions.assertNotNull(userException);
        Assertions.assertEquals("user-not-found", userException.getUserError().getKey());
        Assertions.assertArrayEquals(new Object[]{id.toString()}, userException.getUserError().getArgs());
    }
    
    @Test
    @DisplayName("Delete by id with correct id")
    void deleteById_withCorrectId_ok() {
        UUID id = UUID.randomUUID();
        EUser foundUser = EUser
                .builder()
                .id(id)
                .name("name")
                .build();
        
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(foundUser));
        Mockito.doNothing().when(userRepository).deleteById(id);
        
        userService.deleteById(id);
        
        Mockito.verify(userRepository).findById(id);
        Mockito.verify(userRepository).deleteById(id);
    }
    
    @Test
    @DisplayName("Delete by id with wrong id")
    void deleteById_withWrongId_throwUserException() {
        UUID id = UUID.randomUUID();
        
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        
        UserException userException = Assertions.assertThrows(UserException.class,
                () -> userService.findById(id));
        
        Mockito.verify(userRepository).findById(id);
        
        Assertions.assertNotNull(userException);
        Assertions.assertEquals("user-not-found", userException.getUserError().getKey());
        Assertions.assertArrayEquals(new Object[]{id.toString()}, userException.getUserError().getArgs());
    }
    
    @Test
    @DisplayName("Find all users sorted by name property")
    void findAllUsers_sortedByName_ok() {
        EUser foundUser = EUser
                .builder()
                .id(UUID.randomUUID())
                .name("name")
                .build();
        
        Mockito.when(userRepository.findAll(sortArgumentCaptor.capture()))
                .thenReturn(List.of(foundUser));
        
        List<User> users = userService.findAll();
        
        Mockito.verify(userRepository).findAll(sortArgumentCaptor.capture());
        
        Sort sort = sortArgumentCaptor.getValue();
        Assertions.assertNotNull(sort);
        Assertions.assertEquals(Sort.Direction.ASC,
                Objects.requireNonNull(sort.getOrderFor("name")).getDirection());
        users.stream().findFirst().ifPresent(user -> {
            Assertions.assertNotNull(user);
            Assertions.assertEquals(foundUser.getId(), user.getId());
            Assertions.assertEquals(foundUser.getName(), user.getName());
        });
    }
    
    @Test
    @DisplayName("Update user with correct info")
    void updateUser_withCorrectInfo_ok() {
        UUID id = UUID.randomUUID();
        Category category = Category
                .builder()
                .id(UUID.randomUUID())
                .background("background")
                .name("name")
                .build();
        UpdateUser updateUser = UpdateUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        EUser foundUser = EUser
                .builder()
                .id(id)
                .name("name 2")
                .preferredCategories(List.of(category))
                .build();
        EUser updatedUser = EUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(foundUser));
        Mockito.when(userRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(false);
        Mockito.when(userRepository.save(userArgumentCaptor.capture()))
                .thenReturn(updatedUser);
        
        User user = userService.updateUser(updateUser);
        
        Mockito.verify(userRepository).findById(id);
        Mockito.verify(userRepository).exists(exampleArgumentCaptor.capture());
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        
        Assertions.assertNotNull(user);
        Assertions.assertEquals(updatedUser.getId(), user.getId());
        Assertions.assertEquals(updatedUser.getName(), user.getName());
        Assertions.assertEquals(updatedUser.getPreferredCategories(), user.getPreferredCategories());
        Example<EUser> example = exampleArgumentCaptor.getValue();
        Assertions.assertNotNull(example);
        Assertions.assertEquals(example.getProbe().getName(), updateUser.getName());
        Assertions.assertEquals(example.getMatcher(), ExampleMatcher.matchingAny());
        EUser userToUpdate = userArgumentCaptor.getValue();
        Assertions.assertNotNull(userToUpdate);
        Assertions.assertNotNull(userToUpdate.getId());
        Assertions.assertEquals(updateUser.getName(), userToUpdate.getName());
        Assertions.assertEquals(updateUser.getPreferredCategories(), userToUpdate.getPreferredCategories());
    }
    
    @Test
    @DisplayName("Update user with correct info skipping name check")
    void updateUser_withCorrectInfo_skippingNameCheck() {
        UUID id = UUID.randomUUID();
        Category category = Category
                .builder()
                .id(UUID.randomUUID())
                .background("background")
                .name("name")
                .build();
        UpdateUser updateUser = UpdateUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        EUser foundUser = EUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        EUser updatedUser = EUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(foundUser));
        Mockito.when(userRepository.save(userArgumentCaptor.capture()))
                .thenReturn(updatedUser);
        
        User user = userService.updateUser(updateUser);
        
        Mockito.verify(userRepository).findById(id);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        
        Assertions.assertNotNull(user);
        Assertions.assertEquals(updatedUser.getId(), user.getId());
        Assertions.assertEquals(updatedUser.getName(), user.getName());
        Assertions.assertEquals(updatedUser.getPreferredCategories(), user.getPreferredCategories());
        EUser userToUpdate = userArgumentCaptor.getValue();
        Assertions.assertNotNull(userToUpdate);
        Assertions.assertNotNull(userToUpdate.getId());
        Assertions.assertEquals(updateUser.getName(), userToUpdate.getName());
        Assertions.assertEquals(updateUser.getPreferredCategories(), userToUpdate.getPreferredCategories());
    }
    
    @Test
    @DisplayName("Update user but the name already exists")
    void updateUser_nameAlreadyExists_throwUserException() {
        UUID id = UUID.randomUUID();
        Category category = Category
                .builder()
                .id(UUID.randomUUID())
                .background("background")
                .name("name")
                .build();
        UpdateUser updateUser = UpdateUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        EUser foundUser = EUser
                .builder()
                .id(UUID.randomUUID())
                .name("name 2")
                .preferredCategories(List.of(category))
                .build();
        
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(foundUser));
        Mockito.when(userRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(true);
        
        UserException userException = Assertions.assertThrows(UserException.class,
                () -> userService.updateUser(updateUser));
        
        Mockito.verify(userRepository).findById(id);
        Mockito.verify(userRepository).exists(exampleArgumentCaptor.capture());
        
        Assertions.assertNotNull(userException);
        Assertions.assertEquals("user-exists", userException.getUserError().getKey());
        Assertions.assertArrayEquals(new Object[]{updateUser.getName()}, userException.getUserError().getArgs());
        Example<EUser> example = exampleArgumentCaptor.getValue();
        Assertions.assertNotNull(example);
        Assertions.assertEquals(example.getProbe().getName(), updateUser.getName());
        Assertions.assertEquals(example.getMatcher(), ExampleMatcher.matchingAny());
    }
    
    @Test
    @DisplayName("Update user but the user not found")
    void updateUser_userNotFound_throwUserException() {
        UUID id = UUID.randomUUID();
        Category category = Category
                .builder()
                .id(UUID.randomUUID())
                .background("background")
                .name("name")
                .build();
        UpdateUser updateUser = UpdateUser
                .builder()
                .id(id)
                .name("name")
                .preferredCategories(List.of(category))
                .build();
        
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        
        UserException userException = Assertions.assertThrows(UserException.class,
                () -> userService.updateUser(updateUser));
        
        Mockito.verify(userRepository).findById(id);
        
        Assertions.assertNotNull(userException);
        Assertions.assertEquals("user-not-found", userException.getUserError().getKey());
        Assertions.assertArrayEquals(new Object[]{updateUser.getId().toString()}, userException.getUserError().getArgs());
    }

}
