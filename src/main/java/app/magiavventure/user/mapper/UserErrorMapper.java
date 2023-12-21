package app.magiavventure.user.mapper;

import app.magiavventure.user.configuration.UserProperties.ErrorProperties.ErrorMessage;
import app.magiavventure.user.model.HttpError;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserErrorMapper {
    HttpError map(ErrorMessage errorMessage);
}
