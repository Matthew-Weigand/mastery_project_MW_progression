package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.GuestService;
import learn.mastery.domain.LocationService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Guest;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.List;

@Component
public class Controller {

    private final ReservationService reservationService;
    private final LocationService locationService;
    private final View view;
    private final GuestService guestService;

    @Autowired
    public Controller(ReservationService reservationService, LocationService locationService, GuestService guestService, View view) {
        this.reservationService = reservationService;
        this.locationService = locationService;
        this.guestService = guestService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Don't Wreck My House");
        try {
            runAppLoop();
        } catch (DataException | FileNotFoundException e) {

        }
        view.displayHeader("Hope you enjoy your stay!");
    }


    private void runAppLoop() throws DataException, FileNotFoundException {
        MenuOptions option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_BY_HOST:
                    viewByHost();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MenuOptions.EXIT);
    }

    //responsible for driving the  view existing reservations for a host,
    //needs a host, which has-A list of reservations
    //                             creating reservations,
    //                             editing reservations,
    //                             and cancelling future reservations


    private void viewByHost() throws DataException, FileNotFoundException {
        view.displayHeader(MenuOptions.VIEW_RESERVATIONS_BY_HOST.getMessage());
        String email = view.getHostEmail();
        Location location = locationService.findByEmail(email);
        view.getHostDetails(location);
        List<Reservation> reservations = reservationService.findByEmail(email);
        view.displayReservations(reservations);
        view.enterToContinue();
    }

    private void makeReservation() throws DataException, FileNotFoundException {
        String email = view.getHostEmail();
        Location location = locationService.findByEmail(email);
        String guestEmail = view.getGuestEmail();
        Guest guest = guestService.findByEmail(guestEmail);

        List<Reservation> reservations = reservationService.findByLocation(location.getLocationId());
        view.displayReservations(reservations);


        Reservation reservation = view.makeReservation();
        reservation.setLocation(location);
        reservation.setGuest(guest);
        view.getReservationSummary(reservation);

        if (view.confirmReservation()) {

            Result<Reservation> result = reservationService.add(reservation);

            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s was created", result.getPayload().getReservationId());
                view.displayStatus(true, successMessage);
            }
        } else {
            System.out.println("Reservation not placed. ");
        }

    }

    private void cancelReservation() throws DataException, FileNotFoundException {
        String email = view.getHostEmail();
        Location location = locationService.findByEmail(email);
        String emailGuest = view.getGuestEmail();
        Guest guest = guestService.findByEmail(emailGuest);
        Reservation reservation = new Reservation();
        reservation.setLocation(location);
        reservation.setGuest(guest);



        List<Reservation> reservations = reservationService.findByEmail(location.getEmail());
        view.displayGuestDetails(reservations,guest);
        int reservationId = view.cancelReservation();
        reservation.setReservationId(reservationId);
        Result<Reservation> result = reservationService.cancel(reservation);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {

            if (view.confirmCancellation()) {
                String successMessage = String.format("Reservation %s was cancelled", reservation.getReservationId());
                view.displayStatus(true, successMessage);
            } else {
                String failMessage = String.format("Reservation was not cancelled");
                view.displayStatus(false, failMessage);
            }
        }
    }


    private void editReservation() throws DataException, FileNotFoundException {
        String email = view.getHostEmail();
        Location location = locationService.findByEmail(email);
        String guestEmail = view.getGuestEmail();
        Guest guest = guestService.findByEmail(guestEmail);

        Reservation reservation = new Reservation();
        reservation.setLocation(location);
        reservation.setGuest(guest);

        List<Reservation> reservations = reservationService.findByLocation(location.getLocationId());
        view.displayGuestDetails(reservations, guest);

        reservation = view.editReservation(reservation);
        reservation.setTotalCost(reservation.calculateTotal());
        Result<Reservation> result = reservationService.edit(reservation);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            view.getReservationSummary(reservation);

            if (view.confirmEdit()) {
                String successMessage = String.format("Reservation %s was updated", reservation.getReservationId());
                view.displayStatus(true, successMessage);
            } else {
                String failMessage = String.format("Reservation update cancelled");
                view.displayStatus(false, failMessage);
            }
        }


    }

}