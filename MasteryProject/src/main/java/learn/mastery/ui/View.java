package learn.mastery.ui;


import learn.mastery.domain.LocationService;
import learn.mastery.models.Guest;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MenuOptions selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MenuOptions option : MenuOptions.values()) {
            if (!option.isHidden()) {
                io.printf("%s. %s%n", option.getValue(), option.getMessage());
            }
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MenuOptions.fromValue(io.readInt(message, min, max));
    }

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public String getHostEmail() {
        return io.readRequiredString("Enter Host Email: ");
    }

    public void displayReservations(List<Reservation> reservations) {
        displayHeader(MenuOptions.VIEW_RESERVATIONS_BY_HOST.getMessage());
        if (reservations == null || reservations.isEmpty()) {
            io.println("No hosts found.");
            return;
        }
        for (Reservation reservation : reservations) {
            io.printf("Reservation ID: %s, %s - %s,  Guest ID: %s,  Total Cost of Stay: $%.2f%n",
                    reservation.getReservationId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getGuest().getId(),
                    reservation.getTotalCost()
            );
        }
    }

    public Reservation makeReservation() {
        displayHeader(MenuOptions.MAKE_RESERVATION.getMessage());
        Guest guest = new Guest();
        guest.setId(io.readInt("Enter guest id: "));
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setReservationId(io.readInt("Enter reservation ID: "));
        reservation.setStartDate(io.readLocalDate("Enter start date: (yyyy-mm-dd) "));
        reservation.setEndDate(io.readLocalDate("Enter end date: (yyyy-mm-dd) "));
        return reservation;
    }

    public void getReservationSummary(Reservation reservation){
        reservation.setTotalCost(reservation.calculateTotal());
        io.println("Reservation Details: ");
        io.println("Start Date: " + reservation.getStartDate());
        io.println("End Date: " + reservation.getEndDate());
        io.readString("Total cost: " + reservation.getTotalCost());
    }


    public Reservation cancelReservation(List<Reservation> reservations) {
        displayHeader(MenuOptions.CANCEL_RESERVATION.getMessage());
        displayReservations(reservations);
        int reservationId = io.readInt("Enter reservation ID to cancel");
        for(Reservation reservation : reservations) {
            if(reservationId == reservation.getReservationId()) {
                return reservation;
            }
        }

        return null;
    }

    public Reservation editReservation(List<Reservation> reservations) {
        displayHeader(MenuOptions.EDIT_RESERVATION.getMessage());
        displayReservations(reservations);
        int reservationId = io.readInt("Enter reservation ID to update: ");
        for (Reservation reservation : reservations) {
            if (reservationId == reservation.getReservationId()) {
                reservation.setStartDate(io.readLocalDate("Enter start date to update: (yyyy-mm-dd) "));
                reservation.setEndDate(io.readLocalDate("Enter end date to update: (yyyy-mm-dd) "));
                return reservation;
            }

        }
        return null;
    }

    public boolean confirmReservation() {
        String decision = io.readRequiredString("Would you like to place reservation? (yes/no) ");
        if (decision.equalsIgnoreCase("yes")) {
            return true;
        } else
            return false;
    }

    public boolean confirmCancellation() {
        String decision = io.readRequiredString("Are you sure you want to cancel the reservation? (yes/no) ");
        if (decision.equalsIgnoreCase("yes")) {
            return true;
        } else
            return false;
    }

    public boolean confirmEdit() {
        String decision = io.readRequiredString("Are you sure you want to update reservation? (yes/no) ");

        if(decision.equalsIgnoreCase("yes")) {
            return true;
        }
        else {
            return false;
        }
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

}
