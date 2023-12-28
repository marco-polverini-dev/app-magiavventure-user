package app.magiavventure.user.mapper;

import app.magiavventure.user.configuration.UserProperties.ErrorProperties.ErrorMessage;
import app.magiavventure.user.model.HttpError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("User error mapping tests")
class UserErrorMapperTest {

    private final UserErrorMapper userErrorMapper = Mappers.getMapper(UserErrorMapper.class);

    @Test
    @DisplayName("Map ErrorMessage in HttpError object")
    void mapErrorMessage_asHttpError_ok() {
        ErrorMessage errorMessage = ErrorMessage
                .builder()
                .code("code")
                .status(400)
                .description("description")
                .message("message")
                .build();

        HttpError httpError = userErrorMapper.map(errorMessage);

        Assertions.assertNotNull(httpError);
        Assertions.assertEquals(errorMessage.getCode(), httpError.getCode());
        Assertions.assertEquals(errorMessage.getMessage(), httpError.getMessage());
        Assertions.assertEquals(errorMessage.getDescription(), httpError.getDescription());
        Assertions.assertEquals(errorMessage.getStatus(), httpError.getStatus());
    }

    @Test
    @DisplayName("Map ErrorMessage null in HttpError")
    void mapErrorMessage_asHttpError_null() {
        HttpError httpError = userErrorMapper.map(null);

        Assertions.assertNull(httpError);
    }
}
