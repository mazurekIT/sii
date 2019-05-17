package pl.mazurekIT.sii.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mazurekIT.sii.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
