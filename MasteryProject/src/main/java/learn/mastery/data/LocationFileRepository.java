package learn.mastery.data;

import learn.mastery.models.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationFileRepository implements LocationRepository {

    private final String filePath;

    public LocationFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Location> findAll() throws DataException {
        ArrayList<Location> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;

    }

    @Override
    public Location findById(String locationId) throws DataException {
        return findAll().stream()
                .filter(i -> i.getLocationId().equals(locationId))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Location findByEmail(String emailAddress) throws DataException {
        return findAll().stream()
                .filter(e -> e.getEmail().equals(emailAddress))
                .findFirst()
                .orElse(null);
    }


//id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate

    private Location deserialize(String[] fields) {
        Location result = new Location();
        result.setLocationId(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail((fields[2]));
        result.setPhoneNumber(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate(Double.parseDouble(fields[8]));
        result.setWeekendRate(Double.parseDouble(fields[9]));
        return result;
    }

}
