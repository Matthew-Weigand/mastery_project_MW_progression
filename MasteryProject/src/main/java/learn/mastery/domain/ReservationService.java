package learn.mastery.domain;

import learn.mastery.data.*;
import learn.mastery.models.Location;
import learn.mastery.models.Reservation;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        for(Reservation reservation : result) {
            reservation.setLocation(location);
        }

        return result;
    }

    public Result<Reservation> add(Reservation r) throws DataException {

        Result<Reservation> result = validate(r);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(reservationRepository.add(r));
        return result;
    }

    public Result<Reservation> cancel(Reservation reservation) throws DataException {

        Result<Reservation> result = new Result<>();

        if(reservation == null){
            result.addErrorMessage("Reservation cannot be null");
        }

        if(reservation.getLocation() == null) {
            result.addErrorMessage("Location cannot be null");
        }

        if(reservation.getStartDate().compareTo(LocalDate.now()) <= 0 || reservation.getEndDate().compareTo(LocalDate.now()) < 0) {
            result.addErrorMessage("Cannot delete reservation in the past");
        }

        if(reservation.getStartDate().compareTo(LocalDate.now()) <= 0 && reservation.getEndDate().compareTo(LocalDate.now()) >= 0){
            result.addErrorMessage("Cannot delete a reservation that is currently on-going");
        }

        if(result.isSuccess()) {
            result.setPayload(reservationRepository.cancel(reservation));
            return result;
        }

        return result;
    }

    public Result<Reservation> edit(Reservation r) throws DataException {

        Result<Reservation> result = new Result<>();
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(reservationRepository.edit(r));
        return result;
    }

    private Result<Reservation> validate(Reservation reservation) throws DataException {

        Result<Reservation> result = new Result<>();
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);

        return result;
    }


    //Guest, host, and start and end dates are required.
    //The guest and host must already exist in the "database". Guests and hosts cannot be created.
    //The start date must come before the end date.
    //The reservation may never overlap existing reservation dates.
    //The start date must be in the future.
    private Result<Reservation> validateFields(Reservation reservation, Result<Reservation> result) throws DataException {
        List<Reservation> reservations = reservationRepository.findByLocation(reservation.getLocation().getLocationId());

        //guest id required
        if (reservation.getGuest() == null ||
                guestRepository.findById(reservation.getGuest().getId()) == null) {
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
