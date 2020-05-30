package ru.kostyanoy.repository.carowner;

import ru.kostyanoy.entity.Car;
import ru.kostyanoy.entity.CarOwner;
import ru.kostyanoy.repository.DataAccessException;
import ru.kostyanoy.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static ru.kostyanoy.repository.reader.CarOwnerReader.readCarOwner;
import static ru.kostyanoy.repository.reader.CarReader.readCar;

public class JdbcCarOwnerRepository implements CarOwnerRepository {

    private static final String ADD_QUERY =
            "insert into carOwner (firstName, middleName, lastName) " +
                    "values (?, ?, ?)";

    private static final String GET_QUERY =
            "select         a.id               carOwner_id," +
                    "       a.firstName        carOwner_firstName," +
                    "       a.middleName       carOwner_middleName," +
                    "       a.lastName         carOwner_lastName," +
                    "       b.id               car_id," +
                    "       b.make             car_make," +
                    "       b.model            car_model," +
                    "       b.stateNumberID    car_stateNumberID" +
                    "     from carOwner a " +
                    "left join car b on a.id = b.carOwnerID";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " where a.id = ?";

    private static final String GET_BY_LAST_NAME_QUERY =
            GET_QUERY +
                    " where lower(a.lastName) like lower('%' || ? || '%')";

    private static final String GET_BY_FIRST_LAST_NAME_QUERY =
            GET_BY_LAST_NAME_QUERY +
                    " and lower(a.firstName) like lower('%' || ? || '%')";

    private static final String GET_BY_FIRST_MIDDLE_LAST_NAME_QUERY =
            GET_BY_FIRST_LAST_NAME_QUERY +
                    " and lower(a.middleName) like lower('%' || ? || '%')";

    private static final String UPDATE_QUERY =
            "update carOwner " +
                    "set firstName = ?, middleName = ?, lastName = ? " +
                    "where id = ?";

    private static final JdbcCarOwnerRepository INSTANCE = new JdbcCarOwnerRepository();

    private final DataSource dataSource;

    private JdbcCarOwnerRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static CarOwnerRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(CarOwner carOwner) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, carOwner.getFirstName().toUpperCase());
            ps.setString(2, carOwner.getMiddleName().toUpperCase());
            ps.setString(3, carOwner.getLastName().toUpperCase());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            carOwner.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<CarOwner> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Collection<CarOwner> CarOwners = readCarOwnersList(rs);

            if (CarOwners.isEmpty()) {
                return Optional.empty();
            } else if (CarOwners.size() == 1) {
                return Optional.of(CarOwners.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<CarOwner> get(String... fromLastToFirstNames) {
        String query;
        switch (fromLastToFirstNames.length) {
            case 0: {
                query = GET_QUERY;
                break;
            }
            case 1: {
                query = GET_BY_LAST_NAME_QUERY;
                break;
            }
            case 2: {
                query = GET_BY_FIRST_LAST_NAME_QUERY;
                break;
            }
            default: {
                query = GET_BY_FIRST_MIDDLE_LAST_NAME_QUERY;
                break;
            }
        }

        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query)
        ) {
            if (fromLastToFirstNames.length > 0 && fromLastToFirstNames.length <= 3) {
                for (int i = 0; i < fromLastToFirstNames.length; i++) {
                    ps.setString(i + 1, fromLastToFirstNames[i] == null ? "" : fromLastToFirstNames[i].toUpperCase());
                }
            }
            int lastElementIndex = fromLastToFirstNames.length - 1;
            if (fromLastToFirstNames.length > 3) {
                ps.setString(1, fromLastToFirstNames[0] == null ? "" : fromLastToFirstNames[0].toUpperCase());
                ps.setString(3, fromLastToFirstNames[lastElementIndex] == null ? "" : fromLastToFirstNames[lastElementIndex].toUpperCase());
                String middleName = "";
                for (int i = 1; i < lastElementIndex; i++) {
                    middleName += fromLastToFirstNames[i];
                }
                ps.setString(2, middleName == null ? "" : middleName.toUpperCase());
            }

            ResultSet rs = ps.executeQuery();

            Collection<CarOwner> carOwners = readCarOwnersList(rs);

            return new ArrayList<>(carOwners);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(CarOwner carOwner) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setString(1, carOwner.getFirstName().toUpperCase());
            ps.setString(2, carOwner.getMiddleName().toUpperCase());
            ps.setString(3, carOwner.getLastName().toUpperCase());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<CarOwner> readCarOwnersList(ResultSet rs) throws SQLException {
        Map<Long, CarOwner> result = new HashMap<>();
        while (rs.next()) {
            long carOwnerID = rs.getLong("carOwner_id");

            CarOwner carOwner = result.get(carOwnerID);
            if (carOwner == null) {
                carOwner = readCarOwner(rs);
                result.put(carOwnerID, carOwner);
            }

            long carID = rs.getLong("car_id");
            if (rs.wasNull()) {
                continue;
            }
            Car car = readCar(rs);
            car.setCarOwner(carOwner);
            if (carOwner.getCars() == null) {
                carOwner.setCars(new ArrayList<>());
            }
            carOwner.getCars().add(car);
        }
        return result.values();
    }
}
