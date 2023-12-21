package app.magiavventure.user.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Category {

    @NotNull
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String background;

}
