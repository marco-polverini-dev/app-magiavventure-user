package app.magiavventure.user.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class User {
    @NotNull
    private UUID id;
    @NotNull
    private String name;
    private List<Category> preferredCategories;
}
