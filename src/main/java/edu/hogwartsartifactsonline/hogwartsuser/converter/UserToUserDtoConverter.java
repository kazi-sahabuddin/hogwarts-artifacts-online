package edu.hogwartsartifactsonline.hogwartsuser.converter;

import edu.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import edu.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        //UserDto userDto =
        return new UserDto(source.getId(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles());
    }
}
