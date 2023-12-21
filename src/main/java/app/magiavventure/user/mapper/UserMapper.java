package app.magiavventure.user.mapper;

import app.magiavventure.user.entity.EUser;
import app.magiavventure.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User map(EUser user);
}
