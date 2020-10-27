package learn.mastery;

import learn.mastery.data.*;
import learn.mastery.domain.LocationService;
import learn.mastery.domain.ReservationService;
import learn.mastery.ui.ConsoleIO;
import learn.mastery.ui.Controller;
import learn.mastery.ui.View;

public class App {
    public static void main(String[] args) {
        LocationFileRepository locationRepository = new LocationFileRepository("dont-wreck-my-house-data/locations.csv");
        ReservationFileRepository reservationRepository = new ReservationFileRepository("dont-wreck-my-house-data/reservations");
        GuestFileRepository guestRepository = new GuestFileRepository("dont-wreck-my-house-data/guests.csv");

        ConsoleIO io = new ConsoleIO();
        View view = new View(io);
        ReservationService reservationService = new ReservationService(reservationRepository,locationRepository,guestRepository);
        LocationService locationService = new LocationService(locationRepository);

        Controller controller = new Controller(reservationService, locationService, view);
        controller.run();

    }

}
