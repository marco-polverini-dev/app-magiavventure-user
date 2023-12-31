package app.magiavventure.user.error;

import app.magiavventure.user.model.UserError;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    public static final String UNKNOWN_ERROR = "unknown-error";
    public static final String USER_EXISTS = "user-exists";
    public static final String USER_NOT_FOUND = "user-not-found";

    private final transient UserError userError;

    private UserException(UserError userError) {
        super(userError.getKey(), userError.getThrowable());
        this.userError = userError;
    }

    public static UserException of(String key, String... args) {
        final var userError = UserError
                .builder()
                .key(key)
                .args(args)
                .build();
        return new UserException(userError);
    }

}
