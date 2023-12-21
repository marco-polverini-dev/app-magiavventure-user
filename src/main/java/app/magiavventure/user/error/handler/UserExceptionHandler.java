package app.magiavventure.user.error.handler;

import app.magiavventure.user.configuration.UserProperties;
import app.magiavventure.user.configuration.UserProperties.ErrorProperties.ErrorMessage;
import app.magiavventure.user.error.UserException;
import app.magiavventure.user.mapper.UserErrorMapper;
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
public class UserExceptionHandler {
    private final UserProperties userProperties;
    private final UserErrorMapper userErrorMapper;

    @ExceptionHandler({UserException.class})
    public ResponseEntity<HttpError> userExceptionHandler(UserException userException) {

        UserError userError = userException.getUserError();
        ErrorMessage errorMessage = retrieveError(userError.getKey());

        HttpError httpError = userErrorMapper.map(errorMessage);
        httpError.setMessage(formatMessage(errorMessage.getMessage(), userError.getArgs()));

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    private ErrorMessage retrieveError(@NotNull String key) {
        var errorMessage = userProperties
                .getErrors()
                .getErrorMessages()
                .get(key);

        if(Objects.isNull(errorMessage)) return retrieveError(UserException.UNKNOWN_ERROR);


        return errorMessage;
    }

    private String formatMessage(String message, Object... args) {
        if(args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }

}
