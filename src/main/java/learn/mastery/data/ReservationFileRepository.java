package learn.mastery.data;

import learn.mastery.models.Guest;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(@Value("dont-wreck-my-house-data/reservations") String directory) {
        this.directory = "dont-wreck-my-house-data/reservations";
    }

    //returns a result of all reservations contained in specified "location" file path
    @Override
    public List<Reservation> findByLocation(String locationId) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(locationId)))) {

            //skip header
            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);

                if (fields.length == 5) {
                    result.add(deserialize(fields));
                }
            }

        } catch (IOException e) {
            //don't throw
        }

        return result;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByLocation(reservation.getLocation().getLocationId());

        int newId = all.stream()
                .mapToInt(Reservation::getReservationId)
                .max()
                .orElse(0) + 1;

        reservation.setReservationId(newId);
        all.add(reservation);
        writeAll(all, reservation.getLocation().getLocationId());
        return reservation;
    }


    //Find a reservation.
    //-
    //Start and end date can be edited. No other data can be edited.
    //-
    //Recalculate the total, display a summary, and ask the user to confirm.
    @Override
    public Reservation edit(Reservation reservation) throws DataException {
        List<Reservation> all = findByLocation(reservation.getLocation().getLocationId());

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservation.getReservationId()) {
                all.set(i, reservation);
                writeAll(all, reservation.getLocation().getLocationId());
                return reservation;
            }
        }
        return null;
    }




    @Override
    public Reservation cancel(Reservation reservation) throws DataException {
        List<Reservation> all = findByLocation(reservation.getLocation().getLocationId());

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservation.getReservationId()) {
                all.remove(i);
                writeAll(all, reservation.getLocation().getLocationId());
                return reservation;
            }
        }
        return null;
    }


    private void writeAll(List<Reservation> reservations, String locationId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(locationId))) {

            writer.println(HEADER);

            for (Reservation guest : reservations) {
                writer.println(serialize(guest));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);

        }
    }

    private Reservation deserialize(String[] fields) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Reservation result = new Reservation();
        result.setReservationId(Integer.parseInt(fields[0]));
        result.setStartDate(LocalDate.parse(fields[1], formatter));
        result.setEndDate(LocalDate.parse(fields[2], formatter));
        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[3]));
        result.setGuest(guest);
        result.setTotalCost(new BigDecimal(fields[4]));

        return result;
    }

    private String serialize(Reservation reservationInfo) {
        return String.format("%s,%s,%s,%s,%s",
                reservationInfo.getReservationId(),
                reservationInfo.getStartDate(),
                reservationInfo.getEndDate(),
                reservationInfo.getGuest().getId(),
                reservationInfo.getTotalCost());

    }

    private String getFilePath(String locationId) {
        return Paths.get(directory, locationId + ".csv").toString();
    }
}
