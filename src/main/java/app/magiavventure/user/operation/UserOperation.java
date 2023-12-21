package app.magiavventure.user.operation;

import app.magiavventure.user.model.CreateUser;
import app.magiavventure.user.model.UpdateUser;
import app.magiavventure.user.model.User;
import app.magiavventure.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "User Operation", description = "Create, update, delete and search users")
public class UserOperation {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid CreateUser createUser) {
        return userService.createUser(createUser);
    }

    @GetMapping
    public List<User> findAllUser() {
        return userService.findAll();
    }
    @GetMapping("/{id}")
    public User findUser(@PathVariable(name = "id") UUID id) {
        return userService.findById(id);
    }

    @GetMapping("/{name}/check")
    public void checkName(@PathVariable(name = "name") String name) {
        userService.checkIfUserExists(name);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid UpdateUser updateUser) {
        return userService.updateUser(updateUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "id") UUID id) {
        userService.deleteById(id);
    }


}
