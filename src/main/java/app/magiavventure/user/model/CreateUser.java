package app.magiavventure.user.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CreateUser {
    @NotNull
    private String name;
    private List<Category> preferredCategories;
}
