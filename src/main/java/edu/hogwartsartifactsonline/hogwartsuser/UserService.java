package edu.hogwartsartifactsonline.hogwartsuser;

import edu.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public HogwartsUser findById(Integer userId){
        return this.userRepository.findById(userId).orElseThrow(()-> new ObjectNotFoundException("user",userId));
    }

    public List<HogwartsUser> findAll(){
        return this.userRepository.findAll();
    }

    public HogwartsUser save(HogwartsUser newUser){
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
}
