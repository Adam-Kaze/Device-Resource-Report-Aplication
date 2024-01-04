import javax.swing.table.DefaultTableModel;

public interface DatabaseOperation {
    // Definisi interface untuk operasi database
    void insertPerformanceData(double battery, double ram, double cpu);
    void insertHistory();
    void populateHistoryTable(DefaultTableModel model);
}