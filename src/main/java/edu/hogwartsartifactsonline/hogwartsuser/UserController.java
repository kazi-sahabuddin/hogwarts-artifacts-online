package edu.hogwartsartifactsonline.hogwartsuser;

import edu.hogwartsartifactsonline.hogwartsuser.converter.UserDtoToUserConverter;
import edu.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import edu.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import edu.hogwartsartifactsonline.system.Result;
import edu.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId){
        HogwartsUser foundUser = this.userService.findById(userId);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", this.userToUserDtoConverter.convert(foundUser));
    }

    @GetMapping
    public Result findAllUser(){
        List<HogwartsUser> users = this.userService.findAll();
        List<UserDto> userDtos = users.stream().map(this.userToUserDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser newUser){
        HogwartsUser returnedUser = this.userService.save(newUser);

        return new Result(true, StatusCode.SUCCESS, "Add Success", this.userToUserDtoConverter.convert(returnedUser));
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto updateUserDto){
        HogwartsUser updatedUser = this.userService.update(userId, Objects.requireNonNull(this.userDtoToUserConverter.convert(updateUserDto)));

        return new Result(true, StatusCode.SUCCESS, "Update Success", this.userToUserDtoConverter.convert(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
