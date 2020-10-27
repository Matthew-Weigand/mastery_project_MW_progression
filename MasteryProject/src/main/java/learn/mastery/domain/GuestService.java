package learn.mastery.domain;

import learn.mastery.data.DataException;
import learn.mastery.data.GuestRepository;
import learn.mastery.models.Guest;

import java.util.List;

public class GuestService  {

    private final GuestRepository repository;

    public GuestService (GuestRepository repository) {
        this.repository = repository;
    }

    public Guest findById(int id) throws DataException {
    return repository.findById(id);

    }

    public List<Guest> findAll() throws DataException {
        return repository.findAll();
    }
}



