package learn.mastery.ui;

import learn.mastery.data.DataException;
import learn.mastery.domain.LocationService;
import learn.mastery.domain.ReservationService;
import learn.mastery.domain.Result;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;

import javax.sound.midi.Soundbank;
import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.util.List;

public class Controller {

    private final ReservationService reservationService;
    private final LocationService locationService;
    private final View view;

    public Controller(ReservationService reservationService, LocationService locationService, View view) {
        this.reservationService = reservationService;
        this.locationService = locationService;
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
        String email = view.getHostEmail();
        List<Reservation> reservations = reservationService.findByEmail(email);
        view.displayReservations(reservations);
        view.enterToContinue();
    }

    private void makeReservation() throws DataException, FileNotFoundException {
        String email = view.getHostEmail();
        Location location = locationService.findByEmail(email);
        Reservation reservation = view.makeReservation();
        reservation.setLocation(location);
        view.getReservationSummary(reservation);

        if(view.confirmReservation()){

            Result<Reservation> result = reservationService.add(reservation);

            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s was created", result.getPayload().getReservationId());
                view.displayStatus(true, successMessage);
            }
        }
        else{
            System.out.println("Reservation not placed. ");
        }

    }

    private void cancelReservation() throws DataException, FileNotFoundException {
        String email = view.getHostEmail();
        Location location = locationService.findByEmail(email);
        List<Reservation> reservations = reservationService.findByEmail(location.getEmail());
        Reservation reservation = view.cancelReservation(reservations);

        if(view.confirmCancellation()){
            Result<Reservation> result = reservationService.cancel(reservation);
            String successMessage = String.format("Reservation %s was cancelled", result.getPayload().getReservationId());
            view.displayStatus(true, successMessage);
        }
        else{
            String failMessage = String.format("Reservation" + reservation.getReservationId() + " was not cancelled");
            view.displayStatus(false, failMessage);
        }
    }

    private void editReservation() throws DataException, FileNotFoundException {
        String email = view.getHostEmail();
        Location location = locationService.findByEmail(email);
        List<Reservation> reservations = reservationService.findByEmail(location.getEmail());
        Reservation reservation = view.editReservation(reservations);
        reservation.setLocation(location);
        view.getReservationSummary(reservation);

        if(view.confirmEdit()) {
            Result<Reservation> result = reservationService.edit(reservation);
            String successMessage = String.format("Reservation %s was updated", result.getPayload().getReservationId());
            view.displayStatus(true, successMessage);
        }
        else {
            String failMessage = String.format("Reservation" + reservation.getReservationId() +  "was not updated");
            view.displayStatus(false, failMessage);
        }
    }

}

