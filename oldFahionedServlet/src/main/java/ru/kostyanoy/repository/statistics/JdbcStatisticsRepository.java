package ru.kostyanoy.repository.statistics;

import ru.kostyanoy.entity.Statistics;
import ru.kostyanoy.repository.DataAccessException;
import ru.kostyanoy.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.kostyanoy.repository.reader.StatisticsReader.readStatistics;

public class JdbcStatisticsRepository implements StatisticsRepository {

    private static final String DROP_TEMP_TABLE =
            "DROP TABLE IF EXISTS temp_table";

    private static final String DROP_TEMP_COUNT_TABLE =
            "DROP TABLE IF EXISTS temp_count_table";

    private static final String DROP_TEMP_RANG_TABLE =
            "DROP TABLE IF EXISTS temp_rang_table";

    private static final String CREATE_TEMP_TABLE_QUERY =
                    "CREATE TEMP TABLE temp_table AS " +
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

private static final String CREATE_TEMP_COUNT_TABLE_QUERY =
                    "CREATE TEMP TABLE temp_count_table AS " +
                    "SELECT temp_table.fine_id, COUNT(*) cnt " +
                    "FROM temp_table " +
                    "GROUP BY fine_id " +
                    "ORDER BY cnt DESC";

    private static final String CREATE_TEMP_RANG_TABLE_QUERY =
                    "CREATE TEMP TABLE temp_rang_table AS " +
                    "select * from " +
                    "  (select *, dense_rank() over (order by cnt desc) as fine_top_place " +
                    "  from temp_count_table) subquery " +
                    "where fine_top_place <= ? ";

    private static final String GET_BY_TOP_QUERY =
                    "SELECT fine.id as fine_id, fine.type as fine_type, " +
                    "fine.charge as fine_charge, temp_rang_table.fine_top_place, " +
                    "temp_rang_table.cnt as fine_occurrences " +
                    "FROM fine, temp_rang_table " +
                    "WHERE fine.id = temp_rang_table.fine_id " +
                    "ORDER BY temp_rang_table.fine_top_place ASC;";

    private static final JdbcStatisticsRepository INSTANCE = new JdbcStatisticsRepository();

    private final DataSource dataSource;

    private JdbcStatisticsRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static StatisticsRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Statistics> getByTop(Long top) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement psDropTable = con.prepareStatement(DROP_TEMP_TABLE);
                PreparedStatement psDropCountTable = con.prepareStatement(DROP_TEMP_COUNT_TABLE);
                PreparedStatement psDropRangTable = con.prepareStatement(DROP_TEMP_RANG_TABLE);
                PreparedStatement psCreateTable = con.prepareStatement(CREATE_TEMP_TABLE_QUERY);
                PreparedStatement psCreateCountTable = con.prepareStatement(CREATE_TEMP_COUNT_TABLE_QUERY);
                PreparedStatement psCreateRangTable = con.prepareStatement(CREATE_TEMP_RANG_TABLE_QUERY);
                PreparedStatement psSelect = con.prepareStatement(GET_BY_TOP_QUERY)
        ) {
            psDropTable.execute();
            psDropCountTable.execute();
            psDropRangTable.execute();

            psCreateTable.execute();
            psCreateCountTable.execute();

            psCreateRangTable.setLong(1, top);
            psCreateRangTable.execute();

            ResultSet rs = psSelect.executeQuery();

            Collection<Statistics> statistics = readStatisticsList(rs);

            return new ArrayList<>(statistics);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Statistics> readStatisticsList(ResultSet rs) throws SQLException {
        List<Statistics> result = new ArrayList<>();
        while (rs.next()) {
            Statistics statistics = readStatistics(rs);
            result.add(statistics);
        }
        return result;
    }
}
