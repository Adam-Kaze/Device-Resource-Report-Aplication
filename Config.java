import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Config implements DatabaseOperation {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/db_DeviceResourceReport";
    private static final String USER = "root";
    private static final String PASS = "";

    @Override
    public void insertPerformanceData(double battery, double ram, double cpu) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASS)) {
            String insertQuery = "INSERT INTO Performance_Data (battery, ram, cpu, date_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setDouble(1, battery);
                preparedStatement.setDouble(2, ram);
                preparedStatement.setDouble(3, cpu);
                preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertHistory() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASS)) {
            String insertQuery = "INSERT INTO Performance_Data (date_time) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            }
            System.out.println("Inserted history data!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populateHistoryTable(DefaultTableModel model) {     // Agregasi
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASS)) {
            String selectQuery = "SELECT * FROM Performance_Data";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(selectQuery);
                int count = 1;

                while (resultSet.next()) {
                    Object[] rowData = {
                            count++,
                            formatDate(resultSet.getTimestamp("date_time")),
                            getBatteryValue(resultSet.getDouble("battery")),
                            getRamValue(resultSet.getDouble("ram")),
                            getCpuValue(resultSet.getDouble("cpu"))
                    };
                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String formatDate(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(new Date(timestamp.getTime()));
    }

    private String getBatteryValue(double battery) {
        return String.format("%.2f%%", battery);
    }

    private String getRamValue(double ram) {
        return String.format("%.2f%%", ram);
    }

    private String getCpuValue(double cpu) {
        return String.format("%.2f%%", cpu);
    }
}