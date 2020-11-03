package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepository;
import learn.mastery.models.Guest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService  {

    private final GuestRepository repository;

    public GuestService (GuestRepository repository) {
        this.repository = repository;
    }

    public Guest findById(int id) throws DataException {
    return repository.findById(id);
    }

    public Guest findByEmail(String email) throws DataException {
        return repository.findByEmail(email);
    }
    public List<Guest> findAll() throws DataException {
        return repository.findAll();
    }
}



