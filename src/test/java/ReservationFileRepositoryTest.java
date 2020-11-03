import learn.mastery.data.DataException;
import learn.mastery.data.LocationFileRepository;
import learn.mastery.data.ReservationFileRepository;
import learn.mastery.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH = "dont-wreck-my-house-data/reservations-seed-test-data.csv";
    static final String TEST_FILE_PATH = "./dont-wreck-my-house-data/reservations_data_test/test-data";
    static final String TEST_DIR_PATH = "./dont-wreck-my-house-data/reservations";

    static final int RESERVATION_COUNT = 17;
    static final String reservationPath = "6c32ba5f-39c0-40f1-bb96-0441df64eab2";

    ReservationFileRepository repo = new ReservationFileRepository(TEST_DIR_PATH);
    static final String TEST_PATH = "dont-wreck-my-house-data/locations.csv";


    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByLocationId() throws DataException {

        List<Reservation> reservations = repo.findByLocation(reservationPath);

        assertEquals(RESERVATION_COUNT, reservations.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        Guest guest = new Guest();
        Location location = new Location();
        location.setLocationId(reservationPath);
        reservation.setGuest(guest);
        reservation.setLocation(location);
        reservation.setStartDate(LocalDate.of(2020,8,12));
        reservation.setEndDate(LocalDate.of(2020,8,18));
        reservation.setReservationId(20);
        reservation.setTotalCost(reservation.calculateTotal());
        repo.add(reservation);
        assertEquals(20, reservation.getReservationId());
    }

}
