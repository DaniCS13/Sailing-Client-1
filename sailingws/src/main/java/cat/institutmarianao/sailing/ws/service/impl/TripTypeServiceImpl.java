package cat.institutmarianao.sailing.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.repository.TripTypeRepository;
import cat.institutmarianao.sailing.ws.service.TripTypeService;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class TripTypeServiceImpl implements TripTypeService {
	@Autowired
	private TripTypeRepository tripTypeRepository;

	@Override
	public List<TripType> findAll() {
		return tripTypeRepository.findAll();
	}

	@Override
	public List<TripType> findAll(Category category) {
		return tripTypeRepository.findAllByCategory(category);
	}

	@Override
	public TripType getById(@Positive Long id) {
		return tripTypeRepository.findById(id).orElseThrow(NotFoundException::new);
	}

}
