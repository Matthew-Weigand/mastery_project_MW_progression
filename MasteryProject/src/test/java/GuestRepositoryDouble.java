import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepository;
import learn.mastery.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    public final static Guest GUEST = makeGuest();

    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        guests.add(GUEST);
    }


    @Override
    public List<Guest> findAll() throws DataException {
        return null;
    }

    @Override
    public Guest findById(int id) throws DataException {
        return guests.stream()
                .filter(i->i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByEmail(String emailAddress) throws DataException {
        return null;
    }


    private static Guest makeGuest() {

        Guest guest = new Guest();
        guest.setId(15);
        return guest;
    }
}
