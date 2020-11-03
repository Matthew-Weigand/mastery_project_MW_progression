package learn.mastery.models;

import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.math.BigDecimal;

public class Reservation {

    private int reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Guest guest;
    private BigDecimal totalCost = new BigDecimal(0);
    private Location location;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guestId) {
        this.guest = guestId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal calculateTotal() {
        int weekDayCount = 0;
        int weekendCount = 0;

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (isWeekend(date)) {
                weekendCount++;
            } else if (!isWeekend(date)) {
                weekDayCount++;
            }
        }
        BigDecimal total = new BigDecimal((weekendCount * location.getWeekendRate()
                + weekDayCount * location.getStandardRate()));

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private static boolean isWeekend(LocalDate date) {
        DayOfWeek d = date.getDayOfWeek();
        return d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY;
    }

}
