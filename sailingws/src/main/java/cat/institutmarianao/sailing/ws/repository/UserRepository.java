package cat.institutmarianao.sailing.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cat.institutmarianao.sailing.ws.model.User;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
}
