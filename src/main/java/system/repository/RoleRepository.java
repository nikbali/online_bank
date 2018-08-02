package system.repository;

import org.springframework.data.repository.CrudRepository;
import system.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByName(String name);
}
