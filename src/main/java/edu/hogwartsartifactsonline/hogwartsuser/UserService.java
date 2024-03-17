package edu.hogwartsartifactsonline.hogwartsuser;

import edu.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public HogwartsUser findById(Integer userId){
        return this.userRepository.findById(userId).orElseThrow(()-> new ObjectNotFoundException("user",userId));
    }

    public List<HogwartsUser> findAll(){
        return this.userRepository.findAll();
    }

    public HogwartsUser save(HogwartsUser newUser){
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }

    public HogwartsUser update(Integer userId, HogwartsUser updateUser){
        HogwartsUser oldUser = this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        oldUser.setUsername(updateUser.getUsername());
        oldUser.setEnabled(updateUser.isEnabled());
        oldUser.setRoles(updateUser.getRoles());

        return this.userRepository.save(oldUser);
    }

    void delete(Integer userId){
        HogwartsUser oldUser = this.userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                //.map(hogwartsUser -> new MyUserPrincipal(hogwartsUser))
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("username" + username +" is not found!"));
    }
}
