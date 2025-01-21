package cat.institutmarianao.sailing.ws.service;

import java.util.List;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import jakarta.validation.constraints.Positive;

public interface TripTypeService {

	List<TripType> findAll();

	List<TripType> findAll(Category category);

	TripType getById(@Positive Long id);
}
