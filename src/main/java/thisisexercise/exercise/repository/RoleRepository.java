package thisisexercise.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thisisexercise.exercise.domain.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(String role);
}
