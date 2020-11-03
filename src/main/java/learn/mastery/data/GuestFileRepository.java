package learn.mastery.data;

import learn.mastery.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class GuestFileRepository implements GuestRepository {

    private final String filePath;

    public GuestFileRepository(@Value("dont-wreck-my-house-data/guests.csv") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() throws DataException {
        ArrayList<Guest> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;

    }

    @Override
    public Guest findById(int id) throws DataException {
        return findAll().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByEmail(String emailAddress) throws DataException {
        return findAll().stream()
                .filter(i -> i.getGuestEmail().equals(emailAddress))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest add(Guest guest) {
        return null;
    }


//guest_id,first_name,last_name,email,phone,state

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setId(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName((fields[2]));
        result.setGuestEmail(fields[3]);
        result.setPhoneNumber(fields[4]);
        result.setState(fields[5]);
        return result;
    }


}
