package edu.hogwartsartifactsonline.hogwartsuser;

import edu.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<HogwartsUser> users;

    @BeforeEach
    void setUp() {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        users = new ArrayList<>();
        this.users.add(u1);
        this.users.add(u2);
        this.users.add(u3);

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void TestFindByIdSuccess() {
        //Given
        HogwartsUser h = new HogwartsUser();
        h.setId(1);
        h.setUsername("eric");
        h.setEnabled(true);
        h.setRoles("user");
        given(this.userRepository.findById(1)).willReturn(Optional.of(h));

        //When
        HogwartsUser returnedUser = this.userService.findById(1);

        //Then
        assertThat(returnedUser.getId()).isEqualTo(1);
        assertThat(returnedUser.getUsername()).isEqualTo(h.getUsername());
        assertThat(returnedUser.isEnabled()).isEqualTo(h.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(h.getRoles());
        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void TestFindByIdNotFound() {
        //Given

        given(this.userRepository.findById(6)).willReturn(Optional.empty());

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () ->
           this.userService.findById(6)
       );
        //Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with Id 6 :(");
        verify(this.userRepository,times(1)).findById(6);

    }

    @Test
    void testFindAllSuccess(){
        //Given
        given(this.userRepository.findAll()).willReturn(this.users);
        given(this.userRepository.findAll()).willReturn(this.users);

        //When
        List<HogwartsUser> actualUsers = this.userService.findAll();
        //Then
        assertThat(actualUsers.size()).isEqualTo(users.size());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess(){
        //Given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setUsername("Tomcat");
        newUser.setRoles("user");
        newUser.setEnabled(true);
        newUser.setPassword("123456");
        given(this.userService.save(newUser)).willReturn(newUser);
        //When

        HogwartsUser returnedUser = this.userRepository.save(newUser);
        //Then
        assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
        verify(this.userRepository, times(1)).save(newUser);
    }

    @Test
    void testUpdateUserSuccess(){
        // Given
        HogwartsUser oldUser = new HogwartsUser();
        oldUser.setId(1);
        oldUser.setUsername("john");
        oldUser.setPassword("123456");
        oldUser.setEnabled(true);
        oldUser.setRoles("admin user");

        HogwartsUser update = new HogwartsUser();
        update.setUsername("john - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(oldUser));
        given(this.userRepository.save(oldUser)).willReturn(oldUser);

        // When
        HogwartsUser updatedUser = this.userService.update(1, update);

        // Then
        assertThat(updatedUser.getId()).isEqualTo(1);
        assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
        verify(this.userRepository, times(1)).findById(1);
        verify(this.userRepository, times(1)).save(oldUser);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        HogwartsUser update = new HogwartsUser();
        update.setUsername("john - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.update(1, update);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");
        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        HogwartsUser user = new HogwartsUser();
        user.setId(1);
        user.setUsername("john");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(this.userRepository).deleteById(1);

        // When
        this.userService.delete(1);

        // Then
        verify(this.userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete(1);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with Id 1 :(");
        verify(this.userRepository, times(1)).findById(1);
    }
}