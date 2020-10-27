import learn.mastery.data.DataException;
import learn.mastery.data.ReservationRepository;
import learn.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();


    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setLocation(LocationRepositoryDouble.LOCATION);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2020,8,12));
        reservation.setEndDate(LocalDate.of(2020,8,15));
        reservations.add(reservation);

    }
    @Override
    public List<Reservation> findByLocation(String locationId) throws DataException {

        return reservations.stream()
                .filter(i->i.getLocation().getLocationId().equals(locationId))
                .collect(Collectors.toList());

    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return null;
    }

    @Override
    public Reservation edit(Reservation reservation) throws DataException {
        return null;
    }

    @Override
    public Reservation cancel(Reservation reservation) throws DataException {
        return null;
    }
}
