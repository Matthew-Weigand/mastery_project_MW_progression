import learn.mastery.data.DataException;
import learn.mastery.data.GuestFileRepository;
import learn.mastery.models.Guest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestFileRepositoryTest {

    static final String TEST_PATH = "dont-wreck-my-house-data/guests.csv";
    static final int count = 1000;

    GuestFileRepository repo = new GuestFileRepository(TEST_PATH);

    @Test
    void shouldFindAll() throws DataException {
        assertEquals(count,repo.findAll().size());

    }

    @Test
    void shouldFindById() throws DataException {

        Guest guest = new Guest();
        guest.setId(40);

        assertEquals(guest.getId(),repo.findById(40).getId());
    }

    @Test
    void shouldFindByEmail() throws DataException {
        Guest expected = repo.findByEmail("pweighv@skyrock.com");
        int expectedId = expected.getId();
        int actual = 32;

        assertEquals(actual,expectedId);
    }
}