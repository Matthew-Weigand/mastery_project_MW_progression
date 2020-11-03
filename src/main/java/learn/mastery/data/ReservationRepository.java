package learn.mastery.data;

import learn.mastery.models.Reservation;

import java.util.List;


public interface ReservationRepository {

    List<Reservation> findByLocation(String locationId) throws DataException;

    Reservation add(Reservation reservation) throws DataException;

    Reservation edit(Reservation reservation) throws DataException;

    Reservation cancel(Reservation reservation) throws DataException;
}
