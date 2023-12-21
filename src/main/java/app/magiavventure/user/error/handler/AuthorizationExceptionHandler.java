package app.magiavventure.user.error.handler;

import app.magiavventure.user.configuration.UserProperties;
import app.magiavventure.user.configuration.UserProperties.ErrorProperties.ErrorMessage;
import app.magiavventure.user.error.UserException;
import app.magiavventure.user.model.HttpError;
import app.magiavventure.user.model.UserError;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class AuthorizationExceptionHandler {
    private final UserProperties userProperties;

    @ExceptionHandler({UserException.class})
    public ResponseEntity<HttpError> categoryExceptionHandler(UserException userException) {

        UserError userError = userException.getUserError();
        ErrorMessage errorMessage = retrieveError(userError.getKey(), userError.getArgs());

        if(Objects.isNull(errorMessage))
            errorMessage = retrieveError(UserException.UNKNOWN_ERROR);

        HttpError httpError = HttpError.builder().build();//categoryErrorMapper.map(errorMessage);

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    private ErrorMessage retrieveError(@NotNull String key, Object... args) {
        var errorMessage = userProperties
                .getErrors()
                .getErrorMessages()
                .get(key);
        if(Objects.isNull(errorMessage)) return null;

        if(args.length > 0) {
            String message = errorMessage.getMessage();
            errorMessage.setMessage(String.format(message, args));
        }
        return errorMessage;
    }

}
