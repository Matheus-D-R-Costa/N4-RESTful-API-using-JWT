package simple.security.twitter.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import simple.security.twitter.enums.RoleName;

@Entity(name = "tb_roles")
public class RoleModel implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "name")
    private RoleName name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name.toString();
    }
}
