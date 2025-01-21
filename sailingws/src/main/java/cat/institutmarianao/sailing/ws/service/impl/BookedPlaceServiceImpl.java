package cat.institutmarianao.sailing.ws.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.model.BookedPlace;
import cat.institutmarianao.sailing.ws.repository.BookedPlaceRepository;
import cat.institutmarianao.sailing.ws.service.BookedPlaceService;

@Validated
@Service
public class BookedPlaceServiceImpl implements BookedPlaceService {
	@Autowired
	private BookedPlaceRepository bookedPlaceRepository;

	@Override
	public List<BookedPlace> findByIdTripTypeIdAndIdDate(Long tripTypeId, Date date) {
		return bookedPlaceRepository.findByIdTripTypeIdAndIdDate(tripTypeId, date);
	}

}
