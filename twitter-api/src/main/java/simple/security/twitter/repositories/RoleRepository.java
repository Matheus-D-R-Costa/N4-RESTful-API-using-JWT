package simple.security.twitter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.security.twitter.enums.RoleName;
import simple.security.twitter.models.RoleModel;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

    Optional<RoleModel> findByName(RoleName name);

}
