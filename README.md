# Magiavventure - User

This service allows to create/update/delete/find users used by Magiavventure App.

## Configuration

The properties exposed to configure this project are:

```properties
spring.data.mongodb.uri="string"                                    # Uri for mongo connection
spring.data.mongodb.uuid-representation="string"                    # Format representation for uuid - use default
spring.data.mongodb.auto-index-creation=boolean                     # Create index automatically
logging.level.app.magiavventure="string"                            # Logging level package magiavventure
user.errors.error-messages.{error-key}.code="string"                # The exception key error code
user.errors.error-messages.{error-key}.message="string"             # The exception key error message
user.errors.error-messages.{error-key}.description="string"         # The exception key error description
user.errors.error-messages.{error-key}.status=integer               # The exception key error status
```


## Error message map
The error message map is a basic system for return the specific message in the error response,
the configuration path at the moment is only for one branch **error-messages**.
This branch setting a specific error message to **app.magiavventure.user.error.UserException**

## Running local
For run the service in local environment need to execute following actions

### Running service
Run the service with the following profile:
1. "local" for local environment configuration