import learn.mastery.data.DataException;
import learn.mastery.data.LocationFileRepository;
import learn.mastery.models.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationFileRepositoryTest {

    static final String TEST_PATH = "dont-wreck-my-house-data/locations.csv";
    LocationFileRepository repo = new LocationFileRepository(TEST_PATH);
    static final int count = 1000;

    @Test
    void shouldFindAllLocations() throws DataException {
        assertEquals(count,repo.findAll().size());
    }

    @Test
    void shouldFindByEmail() throws DataException {
        Location location = new Location();
        location.setEmail("hvalasek5@fastcompany.com");
        assertEquals(location.getEmail(),repo.findByEmail("hvalasek5@fastcompany.com").getEmail());
    }

    @Test
    void shouldReturnNullFindByEmail() throws DataException {
        assertNull(repo.findByEmail("superfunguy99999@gmail.com"));
    }

    @Test
    void getWeekendRate() throws DataException {
        Location location = repo.findByEmail("hvalasek5@fastcompany.com");
       assertEquals(483.75,location.getWeekendRate());
    }

    @Test
    void getStandardRate() throws DataException {
        Location location = repo.findById("ea2ff808-7976-4832-912f-d3dd7fe196d3");
        assertEquals(365.0,location.getStandardRate());
    }
}
