package ru.kostyanoy.repository.penaltyevent;

import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.StateNumber;
import ru.kostyanoy.repository.DataAccessException;
import ru.kostyanoy.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.kostyanoy.repository.reader.PenaltyEventReader.readPenaltyEvent;

public class JdbcPenaltyEventRepository implements PenaltyEventRepository {

    private static final String ADD_QUERY =
            "insert into penaltyevent (eventdate, fineid, carid) " +
                    "values (?, ?, ?)";

    private static final String GET_QUERY =
            "select p.id                    penaltyEvent_id, " +
                    "       p.eventDate             penaltyEvent_eventDate," +
                    "       fine.id                 fine_id," +
                    "       fine.type               fine_type," +
                    "       fine.charge             fine_charge," +
                    "       carOwner.id             carOwner_id," +
                    "       carOwner.lastName       carOwner_lastName," +
                    "       carOwner.firstName      carOwner_firstName," +
                    "       carOwner.middleName     carOwner_middleName," +
                    "       car.id                  car_id," +
                    "       car.make                car_make," +
                    "       car.model               car_model," +
                    "       stateNumber.id          stateNumber_id," +
                    "       stateNumber.number      stateNumber_number," +
                    "       stateNumber.series      stateNumber_series," +
                    "       stateNumber.regionCode  stateNumber_regionCode," +
                    "       stateNumber.country     stateNumber_country " +
                    "from penaltyEvent p " +
                    "join car on car.id = p.carID " +
                    "join stateNumber on stateNumber.id = car.stateNumberID " +
                    "join carOwner on carOwner.id = car.carOwnerID " +
                    "join fine on fine.id = p.fineID";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " where p.id = ?";

    private static final String GET_BY_OWNER_NAME_QUERY =
            GET_QUERY +
                    " where lower(carOwner.firstName) like lower('%' || ? || '%')" +
                    " and lower(carOwner.middleName) like lower('%' || ? || '%')" +
                    " and lower(carOwner.lastName) like lower('%' || ? || '%')";

    private static final String GET_BY_STATENUMBER_QUERY =
            GET_QUERY +
                    " where stateNumber.id = ?";

    private static final String UPDATE_QUERY =
            "update penaltyEvent " +
                    "set eventDate = ?, carID = ? " +
                    "where id = ?";

    private static final String DELETE_QUERY =
            "delete from penaltyEvent where id = ?";

    private static final JdbcPenaltyEventRepository INSTANCE = new JdbcPenaltyEventRepository();

    private final DataSource dataSource;

    private JdbcPenaltyEventRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static JdbcPenaltyEventRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(PenaltyEvent penaltyEvent) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setTimestamp(1, Timestamp.valueOf(penaltyEvent.getTimeStamp()));
            ps.setLong(2, penaltyEvent.getFine().getId());
            ps.setLong(3, penaltyEvent.getCar().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            penaltyEvent.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<PenaltyEvent> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);
            ResultSet rs;
            rs = ps.executeQuery();

            Collection<PenaltyEvent> penaltyEvents = readPenaltyEventsList(rs);

            if (penaltyEvents.isEmpty()) {
                return Optional.empty();
            } else if (penaltyEvents.size() == 1) {
                return Optional.of(penaltyEvents.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }


    @Override
    public List<PenaltyEvent> get(String firstName, String middleName, String lastName) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_OWNER_NAME_QUERY)
        ) {
            ps.setString(1, firstName == null ? "" : firstName);
            ps.setString(2, middleName == null ? "" : middleName);
            ps.setString(3, lastName == null ? "" : lastName);

            ResultSet rs = ps.executeQuery();

            Collection<PenaltyEvent> PenaltyEvents = readPenaltyEventsList(rs);

            return new ArrayList<>(PenaltyEvents);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<PenaltyEvent> get(StateNumber stateNumber) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_STATENUMBER_QUERY)
        ) {
            ps.setLong(1, stateNumber.getId());
            ResultSet rs = ps.executeQuery();

            Collection<PenaltyEvent> PenaltyEvents = readPenaltyEventsList(rs);

            return new ArrayList<>(PenaltyEvents);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(PenaltyEvent penaltyEvent) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setTimestamp(1, Timestamp.valueOf(penaltyEvent.getTimeStamp()));
            ps.setLong(2, penaltyEvent.getCar().getId());
            ps.setLong(3, penaltyEvent.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(PenaltyEvent penaltyEvent) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_QUERY)
        ) {
            ps.setLong(1, penaltyEvent.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<PenaltyEvent> readPenaltyEventsList(ResultSet rs) throws SQLException {
        List<PenaltyEvent> result = new ArrayList<>();
        while (rs.next()) {
            PenaltyEvent penaltyEvent = readPenaltyEvent(rs);
            result.add(penaltyEvent);
        }
        return result;
    }
}
