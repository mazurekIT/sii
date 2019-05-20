package pl.mazurekIT.sii.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.mazurekIT.sii.dal.ReservationRepository;
import pl.mazurekIT.sii.model.Reservation;

import java.util.List;

@Service
public class ReservationServiceImp implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Reservation getReservationByCode(String code) {
        return reservationRepository.findByCode(code);
    }


    @Override
    public List<Reservation> getAllReservations() {
        return (List<Reservation>) reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getAllReservationsWhereUserId(Long userId) {
        return reservationRepository.findAllByUserId(userId);
    }


    @Override
    public long countReservations() {
        return reservationRepository.count();
    }
}
