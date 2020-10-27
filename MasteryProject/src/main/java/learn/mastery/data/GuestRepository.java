package learn.mastery.data;
import learn.mastery.models.Guest;

import java.util.List;


public interface GuestRepository {

    List<Guest> findAll() throws DataException;

    Guest findById(int id) throws DataException;

    Guest findByEmail(String emailAddress) throws DataException;
}
