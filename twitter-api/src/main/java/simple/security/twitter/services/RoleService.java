package simple.security.twitter.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simple.security.twitter.enums.RoleName;
import simple.security.twitter.models.RoleModel;
import simple.security.twitter.repositories.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public RoleModel findByName(RoleName name) {
        Optional<RoleModel> roleModel = roleRepository.findByName(name);

        if (roleModel.isEmpty()) throw new IllegalArgumentException("This role does not exist!");

        return roleModel.get();
    }
}
