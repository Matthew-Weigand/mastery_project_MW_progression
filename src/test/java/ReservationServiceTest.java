import learn.mastery.data.*;
import learn.mastery.domain.*;
import learn.mastery.models.Guest;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    ReservationService service = new ReservationService(new ReservationRepositoryDouble(), new LocationRepositoryDouble(), new GuestRepositoryDouble());

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setLocation(LocationRepositoryDouble.LOCATION);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2020,12,4));
        reservation.setEndDate(LocalDate.of(2020,12,8));
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldReturnLocationError() throws DataException {
        Location location = new Location();
        location.setLocationId("not a location");
        Reservation reservation = new Reservation();
        reservation.setLocation(location);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2020,2,4));
        reservation.setEndDate(LocalDate.of(2020,2,8));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldReturnGuestError() throws DataException {
        Guest guest = new Guest();
        guest.setId(200);
        Reservation reservation = new Reservation();
        reservation.setLocation(LocationRepositoryDouble.LOCATION);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2020,2,4));
        reservation.setEndDate(LocalDate.of(2020,2,8));
        Result<Reservation> result = service.add(reservation);
        System.out.println(result.getErrorMessages());
        assertFalse(result.isSuccess());
    }

    @Test
    void voidShouldReturnErrorMessageCancel() throws DataException {

        Reservation r = new Reservation();
        r.setReservationId(3);
        service.cancel(r);
    }


}
