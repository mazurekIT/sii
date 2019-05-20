package pl.mazurekIT.sii.service;

import pl.mazurekIT.sii.model.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation saveReservation(Reservation reservation);


    List<Reservation> getAllReservationsWhereUserId(Long userId);

    Long countReservationByCode(String codeName);

    void deleteReservation(Long id);


}
