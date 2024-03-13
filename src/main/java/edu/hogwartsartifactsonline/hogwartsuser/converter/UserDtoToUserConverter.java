package edu.hogwartsartifactsonline.hogwartsuser.converter;

import edu.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import edu.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDto source) {
        HogwartsUser hogwartsUser = new HogwartsUser();
        //hogwartsUser.setId(source.id());
        hogwartsUser.setUsername(source.username());
        hogwartsUser.setEnabled(source.enabled());
        hogwartsUser.setRoles(source.roles());

        return hogwartsUser;
    }
}
