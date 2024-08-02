package simple.security.twitter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.security.twitter.enums.RoleName;
import simple.security.twitter.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    RoleModel findByName(RoleName name);

}
