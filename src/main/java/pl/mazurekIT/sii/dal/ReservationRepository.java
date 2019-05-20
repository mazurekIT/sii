package pl.mazurekIT.sii.dal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mazurekIT.sii.model.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);
    Long countReservationByCode(String codeName);
    List<Reservation> findAllByCodeStartingWith(String  codeLike);
}
