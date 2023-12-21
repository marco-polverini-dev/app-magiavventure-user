package app.magiavventure.user.operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "User Operation", description = "Create, update, delete and search users")
public class UserOperation {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Void createUser() {
        return null;
    }

    @GetMapping
    public Void retrieveUsers() {
        return null;
    }

    @GetMapping("/{id}")
    public Void retrieveUser(@PathVariable(name = "id") UUID id) {
        return null;
    }

    @PutMapping
    public Void updateUser() {
        return null;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "id") UUID id) {
    }
}
