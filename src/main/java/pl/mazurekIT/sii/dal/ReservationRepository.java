package pl.mazurekIT.sii.dal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mazurekIT.sii.model.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Reservation findByCode(String code);
    Reservation findById(Long id);
    List<Reservation> findAllByUserId(Long userId);
}
