package learn.mastery.domain;

import learn.mastery.data.*;
import learn.mastery.models.Guest;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {


    private final ReservationRepository reservationRepository;
    private final LocationRepository locationRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, LocationRepository locationRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.locationRepository = locationRepository;
        this.guestRepository = guestRepository;
    }

    //returns a list of all reservations for specified location.
    public List<Reservation> findByLocation(String locationId) throws DataException {

        Location location = locationRepository.findById(locationId);
        List<Reservation> result = reservationRepository.findByLocation(locationId);

        for (Reservation reservation : result) {
            reservation.setLocation(location);
            reservation.setTotalCost(reservation.calculateTotal());
        }
        return result;
    }

    public List<Reservation> findByEmail(String email) throws DataException, FileNotFoundException {

        Location location = locationRepository.findByEmail(email);

        List<Reservation> result = reservationRepository.findByLocation(location.getLocationId());

        for (Reservation reservation : result) {
            reservation.setLocation(location);
            reservation.setGuest(reservation.getGuest());
        }

        return result;
    }

    public Result<Reservation> add(Reservation r) throws DataException {


        Result<Reservation> result = validateForCreate(r);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.add(r));
        return result;
    }

    public Result<Reservation> cancel(Reservation r) throws DataException {

        Result<Reservation> result = validateForCancel(r);

        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.cancel(r));
        return result;
    }


    public Result<Reservation> edit(Reservation r) throws DataException {

        Result<Reservation> result = validateForEdit(r);

        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.edit(r));
        return result;
    }

    private Result<Reservation> validateForCancel(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();
        List<Reservation> reservations = reservationRepository.findByLocation(reservation.getLocation().getLocationId());

        if(reservations != null && !reservations.isEmpty()) {
        int count = 0;
        int maxId = reservations.get(reservations.size() - 1).getReservationId();

            for(Reservation r : reservations) {
                if(r.getGuest().getId() == reservation.getGuest().getId()) {
                    int currentId = r.getReservationId();
                    if(currentId == reservation.getReservationId()){
                        count++;
                    }
                }
            }
            if(count == 0) {
                result.addErrorMessage("Guest/Location combination does not exist");
            }

            //reservation required
            if(reservation.getReservationId() > maxId || reservation.getReservationId() < 1){
                result.addErrorMessage("Reservation does not exist");
            }
//        //guest id required
        if (reservation.getGuest() == null ||
                guestRepository.findByEmail(reservation.getGuest().getGuestEmail()) == null) {
            result.addErrorMessage("Guest does not exist");
        }
            //location id required
            if (reservation.getLocation() == null ||
                    locationRepository.findById(reservation.getLocation().getLocationId()) == null) {
                result.addErrorMessage("Location does not exist");
            }
        }
        else {
            result.addErrorMessage("Problem with host");
        }

        return result;

    }

    private Result<Reservation> validateForCreate(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();

        List<Reservation> reservations = reservationRepository.findByLocation(reservation.getLocation().getLocationId());

        if(reservations != null) {
            //guest id required
            if (reservation.getGuest() == null ||
                    guestRepository.findByEmail(reservation.getGuest().getGuestEmail()) == null) {
                result.addErrorMessage("Guest does not exist");
            }
            //location id required
            if (reservation.getLocation() == null ||
                    locationRepository.findById(reservation.getLocation().getLocationId()) == null) {
                result.addErrorMessage("Location does not exist");
            }
            //date must be in future
            if (reservation.getStartDate().isBefore(LocalDate.now())) {
                result.addErrorMessage("Date must be in the future");
            }
            //cannot overlap
            if (validateDateRange(reservation.getStartDate(), reservation.getEndDate(), reservations) == false) {
                result.addErrorMessage("Date not available");
            }
            //start date cannot be after end date
            if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
                result.addErrorMessage("Start date must be before end date");
            }
            //cannot be on the same day
            if (reservation.getStartDate().isEqual(reservation.getEndDate())) {
                result.addErrorMessage("End date must be at least one day after start date");
            }
        }
        else {
            result.addErrorMessage("Problem with host");
        }

        return result;
    }

    private Result<Reservation> validateForEdit(Reservation reservation) throws DataException {

        Result<Reservation> result = new Result<>();
        List<Reservation> reservations = reservationRepository.findByLocation(reservation.getLocation().getLocationId());
        if(reservations != null && !reservations.isEmpty() ) {

            int count = 0;
            int maxId = reservations.get(reservations.size() - 1).getReservationId();

            for (Reservation r : reservations) {
                if (r.getGuest().getId() == reservation.getGuest().getId()) {
                    int currentId = r.getReservationId();
                    if (currentId == reservation.getReservationId()) {
                        count++;
                    }
                }
            }
            if (count == 0) {
                result.addErrorMessage("Guest/Location combination does not exist");
            }

            //saftey check in-case something falls through in above check
            if (reservation.getReservationId() > maxId || reservation.getReservationId() < 1) {
                result.addErrorMessage("Reservation does not exist");
            }
            //guest id required
            if (reservation.getGuest() == null ||
                    guestRepository.findByEmail(reservation.getGuest().getGuestEmail()) == null) {
                result.addErrorMessage("Guest does not exist");
            }
            //location id required
            if (reservation.getLocation() == null ||
                    locationRepository.findById(reservation.getLocation().getLocationId()) == null) {
                result.addErrorMessage("Location does not exist");
            }
            //date must be in future
            if (reservation.getStartDate().isBefore(LocalDate.now())) {
                result.addErrorMessage("Date must be in the future");
            }
            //cannot overlap
            if (validateDateRange(reservation.getStartDate(), reservation.getEndDate(), reservations) == false) {
                result.addErrorMessage("Date not available");
            }
            //start date cannot be after end date
            if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
                result.addErrorMessage("Start date must be before end date");
            }
            //cannot be on the same day
            if (reservation.getStartDate().isEqual(reservation.getEndDate())) {
                result.addErrorMessage("End date must be at least one day after start date");
            }
        }
        else {
            result.addErrorMessage("Either host does not exist, or host has no reservations");
        }


        return result;
    }


    private static boolean validateDateRange(LocalDate startDate, LocalDate endDate, List<Reservation> reservations) {

        ArrayList<Reservation> toRemove = new ArrayList<>();
        for (Reservation reservation : reservations) {

            //remove all dates before start date, if true add to toRemove list
            if (reservation.getEndDate().isBefore(startDate)) {
                toRemove.add(reservation);
            }
            //remove all dates after end date, add to toRemove list
            if (reservation.getStartDate().isAfter(endDate)) {
                toRemove.add(reservation);
            }

        }

        //if to remove list is == to reservations size list, date is available
        if (toRemove.size() == reservations.size()) {
            return true;
        }

        return false;
    }


}
