package ie.eoin.security;

import ie.eoin.dao.UserRepo;
import ie.eoin.entities.PersistentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("dev")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PersistentUser> optionalUser = userRepo.findById(username);
        if(optionalUser.isPresent()){
            PersistentUser persistentUser = optionalUser.get();

            return User.builder()
                    .username(username)
                    .password(persistentUser.getUserPassword())
                    .accountExpired(false)
                    .accountLocked(persistentUser.isLocked())
                    .disabled(persistentUser.isDisabled())
                    .roles(persistentUser.getUserRole())
                    .build();
        }
        else {
            throw new UsernameNotFoundException("user name " + username + " is not found.");
        }
    }
}
