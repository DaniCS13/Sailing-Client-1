package cat.institutmarianao.sailing.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;

public interface TripTypeRepository extends JpaRepository<TripType, Long>, JpaSpecificationExecutor<TripType> {

	List<TripType> findAllByCategory(Category category);
}
