package cat.institutmarianao.sailing.ws.repository;
/*JPA*/

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/*
 * Avoid to expose save and delete methods
 */
@NoRepositoryBean
public interface ViewRepository<T, K> extends Repository<T, K> {
	long count();

	boolean existsById(K id);

	List<T> findAll();

	List<T> findAllById(Iterable<K> ids);

	Optional<T> findById(K id);
}