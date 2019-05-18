package pl.mazurekIT.sii.dal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mazurekIT.sii.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
        User findByName(String name);
        User findById(Integer id);

}
