import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;

public class UI extends UIComponent {
    private JFrame frame;
    private JButton scanButton;
    private JLabel performanceLabel;
    private JTable historyTable;
    private final Config config;    // Asosiasi

    public UI(Config config) {
        this.config = config;
        initialize();
        updateHistoryTable();
    }

    @Override
    public void initialize() {
        frame = new JFrame("Device Resource Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame.setBackground(new Color(255, 214, 170)); // Warna tema Senja

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane);

        JPanel homePanel = new JPanel(new GridBagLayout()); // Untuk posisi tombol di tengah
        tabbedPane.addTab("Home", null, homePanel, null);

        performanceLabel = new JLabel("  Battery  =  0%  |   RAM  =  0%  |  CPU  =  0%  ");
        performanceLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        performanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        GridBagConstraints performanceLabelConstraints = new GridBagConstraints();
        performanceLabelConstraints.gridx = 0;
        performanceLabelConstraints.gridy = 0; // Posisi di atas tombol
        performanceLabelConstraints.insets = new Insets(10, 10, 20, 10);
        homePanel.add(performanceLabel, performanceLabelConstraints);

        scanButton = new JButton("SCAN");
        scanButton.addActionListener(e -> onScanButtonClicked());
        scanButton.setFont(new Font("Arial", Font.BOLD, 16));
        scanButton.setBackground(new Color(153, 107, 61));
        scanButton.setForeground(Color.BLACK);
        GridBagConstraints scanButtonConstraints = new GridBagConstraints();
        scanButtonConstraints.gridx = 0;
        scanButtonConstraints.gridy = 1; // Posisi di bawah label performa
        scanButtonConstraints.insets = new Insets(20, 10, 10, 10);
        homePanel.add(scanButton, scanButtonConstraints);

        JPanel historyPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("History", null, historyPanel, null);

        String[] historyColumnNames = {"No", "Date & Time", "Battery", "RAM", "CPU"};
        DefaultTableModel historyModel = new DefaultTableModel(historyColumnNames, 0);
        historyTable = new JTable(historyModel);

        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        JTableHeader header = historyTable.getTableHeader();
        header.setBackground(new Color(153, 107, 61));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        historyTable.setDefaultRenderer(Object.class, new CustomRenderer());
        historyPanel.setBackground(new Color(255, 214, 170));

        setComponentColors(frame.getContentPane());
    }

    @Override
    public void setComponentColors(Container container) {
        container.setBackground(new Color(255, 214, 170));

        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                setComponentColors((Container) component);
            }

            if (component instanceof JTableHeader) {
                component.setBackground(new Color(153, 107, 61));
            }
        }
    }

    @Override
    public void show() {
        frame.pack(); // Menyesuaikan ukuran frame secara otomatis
        frame.setLocationRelativeTo(null); // Menempatkan frame di tengah layar
        frame.setVisible(true);
    }

    private void onScanButtonClicked() {
        double currentBattery = 88.75;
        double currentRam = 97.3;
        double currentCpu = 86.12;

        // Untuk Menyimpan performa saat ini ke database
        config.insertPerformanceData(currentBattery, currentRam, currentCpu);

        // Untuk Mengupdate label performa pada home page
        updatePerformanceLabel(currentBattery, currentRam, currentCpu);

        // Untuk memperbarui tabel history
        updateHistoryTable();
        System.out.println("\n    Update history table: done");
    }

    private void updatePerformanceLabel(double battery, double ram, double cpu) {
        DecimalFormat df = new DecimalFormat("0.00");
        String performanceText = String.format("  Battery  =  %s%%  |   RAM  =  %s%%  |  CPU  =  %s%%  ",
                df.format(battery), df.format(ram), df.format(cpu));
        performanceLabel.setText(performanceText);
    }

    private void updateHistoryTable() {
        DefaultTableModel model = (DefaultTableModel) historyTable.getModel();
        model.setRowCount(0);

        config.populateHistoryTable(model);
    }

    private static class CustomRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                component.setBackground(row % 2 == 0 ? new Color(255, 214, 170) : new Color(204, 153, 102));
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            return component;
        }
    }
}