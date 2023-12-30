package app.magiavventure.user.error;

import app.magiavventure.user.configuration.UserProperties;
import app.magiavventure.user.error.handler.UserExceptionHandler;
import app.magiavventure.user.mapper.UserErrorMapper;
import app.magiavventure.user.model.HttpError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
@DisplayName("User exception handler tests")
class UserExceptionHandlerTest {
	
	
	@InjectMocks
	private UserExceptionHandler userExceptionHandler;
	
	@Spy
	private UserProperties userProperties = retrieveUserProperties();
	
	@Spy
	private UserErrorMapper userErrorMapper = Mappers.getMapper(UserErrorMapper.class);
	
	@ParameterizedTest
	@CsvSource({"unknown-error, unknown-error, errore sconosciuto, desc sconosciuta, 500, prova",
			"user-not-found, user-not-found, user non trovato, desc user non trovato, 404, prova",
			"user-exists, user-exists, il nome 'prova' non è disponibile, desc nome già esistente, 403, prova",
			"error-not-exists, unknown-error, errore sconosciuto, desc sconosciuta, 500, prova"})
	@DisplayName("Handle user exception and return ResponseEntity")
	void handleUserExceptionTest(String code, String expectedCode, String expectedMessage,
	                                 String expectedDescription, int expectedStatus,
	                                 String args) {
		
		var userException = UserException.of(code, args);
		
		ResponseEntity<HttpError> responseEntity = userExceptionHandler.userExceptionHandler(userException);
		
		Assertions.assertNotNull(responseEntity);
		Assertions.assertNotNull(responseEntity.getBody());
		Assertions.assertEquals(expectedStatus, responseEntity.getStatusCode().value());
		Assertions.assertEquals(expectedCode, responseEntity.getBody().getCode());
		Assertions.assertEquals(expectedMessage, responseEntity.getBody().getMessage());
		Assertions.assertEquals(expectedDescription, responseEntity.getBody().getDescription());
		Assertions.assertEquals(expectedStatus, responseEntity.getBody().getStatus());
	}
	
	@ParameterizedTest
	@CsvSource({"unknown-error, unknown-error, errore sconosciuto, desc sconosciuta, 500",
			"user-not-found, user-not-found, user non trovato, desc user non trovato, 404",
			"user-exists, user-exists, il nome '%s' non è disponibile, desc nome già esistente, 403",
			"error-not-exists, unknown-error, errore sconosciuto, desc sconosciuta, 500"})
	@DisplayName("Handle user exception and return ResponseEntity without arguments")
	void handleUserExceptionTest(String code, String expectedCode, String expectedMessage,
	                                 String expectedDescription, int expectedStatus) {
		
		var userException = UserException.of(code);
		
		ResponseEntity<HttpError> responseEntity = userExceptionHandler.userExceptionHandler(userException);
		
		Assertions.assertNotNull(responseEntity);
		Assertions.assertNotNull(responseEntity.getBody());
		Assertions.assertEquals(expectedStatus, responseEntity.getStatusCode().value());
		Assertions.assertEquals(expectedCode, responseEntity.getBody().getCode());
		Assertions.assertEquals(expectedMessage, responseEntity.getBody().getMessage());
		Assertions.assertEquals(expectedDescription, responseEntity.getBody().getDescription());
		Assertions.assertEquals(expectedStatus, responseEntity.getBody().getStatus());
	}
	
	private UserProperties retrieveUserProperties() {
		var userProperties = new UserProperties();
		var errorProperties = new UserProperties.ErrorProperties();
		var mapErrorMessages = new HashMap<String, UserProperties.ErrorProperties.ErrorMessage>();
		mapErrorMessages.put("unknown-error", UserProperties.ErrorProperties.ErrorMessage
				.builder()
				.code("unknown-error")
				.status(500)
				.message("errore sconosciuto")
				.description("desc sconosciuta")
				.build());
		mapErrorMessages.put("user-not-found", UserProperties.ErrorProperties.ErrorMessage
				.builder()
				.code("user-not-found")
				.status(404)
				.message("user non trovato")
				.description("desc user non trovato")
				.build());
		mapErrorMessages.put("user-exists", UserProperties.ErrorProperties.ErrorMessage
				.builder()
				.code("user-exists")
				.status(403)
				.message("il nome '%s' non è disponibile")
				.description("desc nome già esistente")
				.build());
		errorProperties.setErrorMessages(mapErrorMessages);
		userProperties.setErrors(errorProperties);
		return userProperties;
	}
	
	
}