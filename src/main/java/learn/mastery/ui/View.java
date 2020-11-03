package learn.mastery.ui;

import learn.mastery.models.Guest;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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
        return io.readRequiredString("Host Email: ");
    }

    public void getHostDetails(Location location){
        io.printf("%s: %s, %s %n%n", location.getLastName(), location.getCity(), location.getState());
    }

    public String getGuestEmail() {
        return  io.readRequiredString("Guest Email: ");
    }

    public void displayReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found at host location.");
            return;
        }
        for (Reservation reservation : reservations) {
            Guest guest = new Guest();
            guest.setId(reservation.getGuest().getId());
            guest.setGuestEmail(reservation.getGuest().getGuestEmail());
            io.printf("Reservation ID: %s, %s - %s, Guest ID: %s, Total Cost: %s %n",
                    reservation.getReservationId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    guest.getId(),
                    reservation.getTotalCost()
            );
        }
    }

    public Reservation makeReservation() {
        displayHeader(MenuOptions.MAKE_RESERVATION.getMessage());
        Reservation reservation = new Reservation();
        reservation.setStartDate(io.readLocalDate("Enter start date: (yyyy-mm-dd) "));
        reservation.setEndDate(io.readLocalDate("Enter end date: (yyyy-mm-dd) "));
        return reservation;
    }



    public int cancelReservation() {
        displayHeader(MenuOptions.CANCEL_RESERVATION.getMessage());
        int id = io.readInt("Enter reservation ID to cancel: ");

        return id;
    }

    public Reservation editReservation(Reservation reservation) {
        displayHeader(MenuOptions.EDIT_RESERVATION.getMessage());
        reservation.setReservationId((io.readInt("Enter reservation ID to update: ")));
        reservation.setStartDate(io.readLocalDate("Enter start date to update: (yyyy-mm-dd) "));
        reservation.setEndDate(io.readLocalDate("Enter end date to update: (yyyy-mm-dd) "));

        return reservation;

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

    public void displayGuestDetails(List<Reservation> reservations, Guest g) {

        List<String> guestReservations = new ArrayList<>();

        for(Reservation reservation : reservations) {
            if(reservation.getGuest().getId() == g.getId()){
                String toAdd = (String.format("ID: %s , %s - %s , Guest: %s:, %s, Email: %s", reservation.getReservationId(),
                        reservation.getStartDate(), reservation.getEndDate(), g.getLastName(),
                        g.getFirstName(), g.getGuestEmail()));
                guestReservations.add(toAdd);
            }
        }
        for(String guest: guestReservations){
            io.println(guest);
        }

    }

    public void getReservationSummary(Reservation reservation){
        reservation.setTotalCost(reservation.calculateTotal());
        io.println("Reservation Details: ");
        io.println("Start Date: " + reservation.getStartDate());
        io.println("End Date: " + reservation.getEndDate());
        io.readString("Total cost: " + reservation.getTotalCost());
    }


}
