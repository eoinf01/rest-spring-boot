package ie.eoin.dao;

import ie.eoin.entities.PersistentUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<PersistentUser,String> {
     Optional<PersistentUser> findById(String username);
}
