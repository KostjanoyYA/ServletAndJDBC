package ru.kostyanoy.repository.fine;

import ru.kostyanoy.entity.Fine;
import ru.kostyanoy.repository.DataAccessException;
import ru.kostyanoy.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.kostyanoy.repository.reader.FineReader.readFine;

public class JdbcFineRepository implements FineRepository {

    private static final String GET_QUERY =
            "select         a.id          fine_id," +
                    "       a.type        fine_type," +
                    "       a.charge      fine_charge" +
                    "     from fine a";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " where a.id = ?";

    private static final JdbcFineRepository INSTANCE = new JdbcFineRepository();

    private final DataSource dataSource;

    private JdbcFineRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static FineRepository getInstance() {
        return INSTANCE;
    }


    @Override
    public Optional<Fine> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Collection<Fine> fines = readFinesList(rs);

            if (fines.isEmpty()) {
                return Optional.empty();
            } else if (fines.size() == 1) {
                return Optional.of(fines.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<Fine> get() {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_QUERY)
        ) {
            ResultSet rs = ps.executeQuery();

            Collection<Fine> fines = readFinesList(rs);

            if (fines.isEmpty()) {
                throw new SQLException("Unexpected result set size");
            }
            return new ArrayList<>(fines);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Fine> readFinesList(ResultSet rs) throws SQLException {
        Map<Long, Fine> result = new HashMap<>();
        while (rs.next()) {
            long fineID = rs.getLong("fine_id");

            Fine fine = result.get(fineID);
            if (fine == null) {
                fine = readFine(rs);
                result.put(fineID, fine);
            }
        }
        return result.values();
    }
}
