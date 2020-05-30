package ru.kostyanoy.repository.statenumber;

import ru.kostyanoy.entity.StateNumber;
import ru.kostyanoy.repository.DataAccessException;
import ru.kostyanoy.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.kostyanoy.repository.reader.StateNumberReader.readStateNumber;

public class JdbcStateNumberRepository implements StateNumberRepository {

    private static final String ADD_QUERY =
            "insert into stateNumber (country, regionCode, series, number) " +
                    "values (?, ?, ?, ?)";

    private static final String GET_QUERY =
            "select         a.id               stateNumber_id," +
                    "       a.number           stateNumber_number," +
                    "       a.series           stateNumber_series," +
                    "       a.regionCode       stateNumber_regionCode," +
                    "       a.country          stateNumber_country," +
                    "       b.id               car_id," +
                    "       b.make             car_make," +
                    "       b.model            car_model," +
                    "       b.carOwnerID       car_carOwnerID" +
                    "     from stateNumber a " +
                    "left join car b on a.id = b.StateNumberID";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " where a.id = ?";

    private static final String GET_BY_STATE_NUMBER_QUERY =
            GET_QUERY +
                    " where a.number = ? " +
                    " and a.series = ? " +
                    " and a.regionCode = ? " +
                    " and a.country = ?";

    private static final String UPDATE_QUERY =
            "update stateNumber " +
                    "set country = ?, regionCode = ?, series = ?, number = ? " +
                    "where id = ?";

    private static final JdbcStateNumberRepository INSTANCE = new JdbcStateNumberRepository();

    private final DataSource dataSource;

    private JdbcStateNumberRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static StateNumberRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(StateNumber stateNumber) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, stateNumber.getCountry().toUpperCase());
            ps.setInt(2, stateNumber.getRegionCode());
            ps.setString(3, stateNumber.getSeries().toUpperCase());
            ps.setInt(4, stateNumber.getNumber());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            stateNumber.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<StateNumber> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            return checkList(readStateNumbersList(rs));

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<StateNumber> get(StateNumber stateNumber) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_STATE_NUMBER_QUERY)
        ) {
            ps.setInt(1, stateNumber.getNumber());
            ps.setString(2, stateNumber.getSeries().toUpperCase());
            ps.setInt(3, stateNumber.getRegionCode());
            ps.setString(4, stateNumber.getCountry().toUpperCase());

            ResultSet rs = ps.executeQuery();

            return checkList(readStateNumbersList(rs));

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Optional<StateNumber> checkList(Collection<StateNumber> stateNumbers) throws SQLException {
        if (stateNumbers.isEmpty()) {
            return Optional.empty();
        } else if (stateNumbers.size() == 1) {
            return Optional.of(stateNumbers.iterator().next());
        } else {
            throw new SQLException("Unexpected result set size");
        }
    }


    @Override
    public void update(StateNumber stateNumber) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setString(1, stateNumber.getCountry().toUpperCase());
            ps.setInt(2, stateNumber.getRegionCode());
            ps.setString(3, stateNumber.getSeries().toUpperCase());
            ps.setInt(4, stateNumber.getNumber());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<StateNumber> readStateNumbersList(ResultSet rs) throws SQLException {
        Map<Long, StateNumber> result = new HashMap<>();
        while (rs.next()) {
            long stateNumberID = rs.getLong("stateNumber_id");

            StateNumber stateNumber = result.get(stateNumberID);
            if (stateNumber == null) {
                stateNumber = readStateNumber(rs);
                result.put(stateNumberID, stateNumber);
            }
        }
        return result.values();
    }
}
