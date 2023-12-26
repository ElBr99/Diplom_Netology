package netology.diplom.filestorage.repository;

import netology.diplom.filestorage.entity.UserRole;
import netology.diplom.filestorage.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(Role role);
}
