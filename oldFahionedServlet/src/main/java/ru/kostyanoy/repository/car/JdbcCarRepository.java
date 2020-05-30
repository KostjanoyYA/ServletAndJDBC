package ru.kostyanoy.repository.car;

import ru.kostyanoy.entity.Car;
import ru.kostyanoy.entity.CarOwner;
import ru.kostyanoy.entity.StateNumber;
import ru.kostyanoy.repository.DataAccessException;
import ru.kostyanoy.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.kostyanoy.repository.reader.CarOwnerReader.readCarOwner;
import static ru.kostyanoy.repository.reader.CarReader.readCar;
import static ru.kostyanoy.repository.reader.StateNumberReader.readStateNumber;

public class JdbcCarRepository implements CarRepository {

    private static final String ADD_QUERY =
            "insert into Car (make, model, stateNumberID, carOwnerID) " +
                    "values (?, ?, ?, ?)";

    private static final String GET_QUERY =
            "select b.id             car_id," +
                    "       b.make           car_make," +
                    "       b.model          car_model," +
                    "       c.id             stateNumber_id," +
                    "       c.number         stateNumber_number," +
                    "       c.series         stateNumber_series," +
                    "       c.regioncode     stateNumber_regioncode," +
                    "       c.country        stateNumber_country," +
                    "       a.id             carOwner_id," +
                    "       a.firstName      carOwner_firstName," +
                    "       a.middleName     carOwner_middleName," +
                    "       a.lastName       carOwner_lastName " +
                    "from car b, carOwner a, stateNumber c " +
                    " where a.id = b.carOwnerID " +
                    " and b.statenumberid = c.id ";


    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " and b.id = ? ";

    private static final String GET_BY_USER_ID_QUERY =
            GET_QUERY +
                    " and a.id = ?";

    private static final String GET_BY_STATENUMBER_ID_QUERY =
            GET_QUERY +
                    " and b.stateNumberID = ?";

    private static final String GET_BY_MAKE_MODEL_QUERY =
            GET_QUERY +
                    " and lower(b.make) like lower('%' || ? || '%')" +
                    " and lower(b.model) like lower('%' || ? || '%')";

    private static final String UPDATE_QUERY =
            "update Car " +
                    "set make = ?, model = ?, stateNumberID = ?, carOwnerID = ? " +
                    "where id = ?";

    private static final String DELETE_QUERY =
            "delete from Car where id = ?";

    private static final JdbcCarRepository INSTANCE = new JdbcCarRepository();

    private final DataSource dataSource;

    private JdbcCarRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static JdbcCarRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(Car car) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, car.getMake().toUpperCase());
            ps.setString(2, car.getModel().toUpperCase());
            ps.setLong(3, car.getStateNumber().getId());
            ps.setLong(4, car.getCarOwner().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            car.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<Car> getById(Long id) {
        return getCar(id, GET_BY_ID_QUERY);
    }

    private Optional<Car> getCar(Long id, String query) {
        List<Car> cars = getByIdUnit(id, query);

        if (cars.isEmpty()) {
            return Optional.empty();
        } else if (cars.size() == 1) {
            return Optional.of(cars.iterator().next());
        } else {
            throw new DataAccessException(new SQLException("Unexpected result set size"));
        }
    }


    @Override
    public List<Car> getByUserId(Long userId) {
        return getByIdUnit(userId, GET_BY_USER_ID_QUERY);
    }



    @Override
    public Optional<Car> getByStateNumberId(Long stateNumberId) {

        return getCar(stateNumberId, GET_BY_STATENUMBER_ID_QUERY);
    }

    private List<Car> getByIdUnit(Long id, String query) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query)
        ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            Collection<Car> cars = readCarsList(rs);
            return new ArrayList<>(cars);

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<Car> get(String make, String model) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_MAKE_MODEL_QUERY)
        ) {
            ps.setString(1, make == null ? "" : make);
            ps.setString(2, model == null ? "" : model);

            ResultSet rs = ps.executeQuery();

            Collection<Car> cars = readCarsList(rs);

            return new ArrayList<>(cars);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(Car car) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setLong(3, car.getStateNumber().getId());
            ps.setLong(4, car.getCarOwner().getId());
            ps.setLong(5, car.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(Car car) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_QUERY)
        ) {
            ps.setLong(1, car.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Car> readCarsList(ResultSet rs) throws SQLException {
        List<Car> result = new ArrayList<>();
        while (rs.next()) {
            Car car = readCar(rs);
            StateNumber stateNumber = readStateNumber(rs);
            CarOwner carOwner = readCarOwner(rs);
            car.setStateNumber(stateNumber);
            car.setCarOwner(carOwner);

            result.add(car);
        }
        return result;
    }
}
