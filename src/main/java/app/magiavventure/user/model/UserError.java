package app.magiavventure.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserError {
    private String key;
    private Throwable throwable;
    private Object[] args;
}
