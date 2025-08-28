import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MainScreen extends JFrame {

    JButton addBtn = new JButton();
    JButton viewBtn = new JButton();
    JButton clearBtn = new JButton();
    JButton clearAllBtn = new JButton();
    JButton deleteBtn = new JButton();
    JButton editBtn = new JButton();
    JButton saveBtn = new JButton();
    JButton loadBtn = new JButton();
    JButton exportBtn =  new JButton();

    JLabel amountLabel = new JLabel();
    JLabel categoryLabel = new JLabel();
    JLabel descriptionLabel = new JLabel();
    JLabel dateLabel = new JLabel();

    JTextField amountField = new JTextField();
    JTextField descriptionField = new JTextField();
    JFormattedTextField dateField;

    JTable table;
    DefaultTableModel model;
    JComboBox categoryBox;
    JLabel totalLabel;

    PieChartPanel pieChartPanel;
    private Map<String, Double> categoryMap = new HashMap<>();
    private boolean isEditing = false;
    private int editingRow = -1;

    JPanel labelsPanel = new JPanel();

    public MainScreen(){
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(194, 241, 223),
                        getWidth(), 0, new Color(153, 192, 251)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);
        ImageIcon image= new ImageIcon(getClass().getResource("/BB1.png"));
        setIconImage(image.getImage());

        JPanel subPanel1 = new JPanel();
        subPanel1.setLayout(new BorderLayout());
        subPanel1.setPreferredSize(new Dimension(850,650));
        subPanel1.setBorder(BorderFactory.createTitledBorder("Expense Records"));
        subPanel1.setOpaque(false);

        JPanel subPanel2 = new JPanel();
        subPanel2.setLayout(new BoxLayout(subPanel2, BoxLayout.Y_AXIS));
        subPanel2.setOpaque(false);
        subPanel2.setPreferredSize(new Dimension(410,400));
        JPanel summaryDetails = new JPanel();

        JPanel subPanel3 = new JPanel();
        subPanel3.setBorder(BorderFactory.createTitledBorder("Export"));
        subPanel3.setOpaque(false);
        subPanel3.setPreferredSize(new Dimension(410,200));

        JPanel comboPanel = new JPanel();
        comboPanel.setOpaque(false);
        comboPanel.setPreferredSize(new Dimension(410,650));

        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BorderLayout());
        formContainer.setOpaque(false);
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);

        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        bottomPanel.setOpaque(false);

        addBtn.setText("Add Expense");
        addBtn.setPreferredSize(new Dimension(150,30));
        addBtn.setForeground(new Color(0, 0, 128));
        addBtn.setBackground(new Color(70, 130, 180));
        addBtn.addActionListener(e->{
            if (amountField.getText().trim().isEmpty() || descriptionField.getText().trim().isEmpty() || dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String category = (String) categoryBox.getSelectedItem();
            String description = descriptionField.getText().trim();
            String date = dateField.getText().trim();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                sdf.parse(date);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Date! Please use dd/MM/yyyy format.");
                return;
            }
            Object[] row = {date, category, description, amount};
            model.addRow(row);
            categoryMap.put(category, categoryMap.getOrDefault(category, 0.0) + amount);
            clearFields();
            updateTotal();
            pieChartPanel.repaint();
            updateSummary();
        });
        viewBtn.setText("View All Expenses");
        viewBtn.setPreferredSize(new Dimension(150,30));
        viewBtn.setBackground(new Color(70, 130, 180));
        viewBtn.setForeground(new Color(0, 0, 128));

        clearBtn.setText("Clear Fields");
        clearBtn.setPreferredSize(new Dimension(150,30));
        clearBtn.setForeground(new Color(0, 0, 128));
        clearBtn.setBackground(new Color(70, 130, 180));
        clearBtn.addActionListener(e-> {
            clearFields();
            updateTotal();
            updateSummary();
        });
        clearAllBtn.setText("Clear All Expenses");
        clearAllBtn.setPreferredSize(new Dimension(150,30));
        clearAllBtn.setForeground(new Color(0, 0, 128));
        clearAllBtn.setBackground(new Color(70, 130, 180));
        clearAllBtn.addActionListener(e->{
            model.setRowCount(0);
            updateTotal();
            updateSummary();
        });
        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(clearAllBtn);

        String[] categories = {"Food & Dining", "Transport", "Shopping", "Bills & Utensils", "Health & Fitness", "Education", "Groceries", "Travel", "Others"};
        categoryBox = new JComboBox(categories);
        categoryBox.setEditable(true);
        categoryBox.setPreferredSize(new Dimension(120,35));
        amountLabel.setText("Amount");
        amountLabel.setPreferredSize(new Dimension(60,35));
        amountField.setPreferredSize(new Dimension(60,35));
        categoryLabel.setText("Category");
        categoryLabel.setPreferredSize(new Dimension(60,35));
        descriptionLabel.setText("Description");
        descriptionLabel.setPreferredSize(new Dimension(60,35));
        descriptionField.setPreferredSize(new Dimension(150,35));
        dateLabel.setText("Date (dd/mm/yyyy)");
        dateLabel.setPreferredSize(new Dimension(100,35));
        MaskFormatter dateMask = null;
        try {
            dateMask = new MaskFormatter("##/##/####");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateMask.setPlaceholderCharacter('0');
        dateField =  new JFormattedTextField(dateMask);
        dateField.setColumns(7);
        dateField.setText(" / /");
        dateField.setForeground(Color.LIGHT_GRAY);
        dateField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                dateField.setForeground(Color.BLACK);
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (dateField.getText().trim().equals("00/00/0000")) {
                    dateField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setPreferredSize(new Dimension(750,40));
        row.setOpaque(false);
        row.add(dateLabel);
        row.add(dateField);
        row.add(categoryLabel);
        row.add(categoryBox);
        row.add(descriptionLabel);
        row.add(descriptionField);
        row.add(amountLabel);
        row.add(amountField);
        formPanel.add(row);

        String[] columns = {"Date", "Category", "Description", "Amount"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setBackground(new Color(194, 241, 253));
        table.setFillsViewportHeight(true);
        table.setRowHeight(20);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));
        pane.setPreferredSize(new Dimension(750,450));
        formContainer.add(formPanel, BorderLayout.NORTH);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        deleteBtn.setText("DELETE SELECTED ROW");
        deleteBtn.setPreferredSize(new Dimension(200,40));
        deleteBtn.setForeground(new Color(0, 0, 128));
        deleteBtn.setBackground(new Color(70, 130, 180));
        deleteBtn.addActionListener(e->{
            int selectedRow = table.getSelectedRow();
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(this,"Please select a row first!", "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                model.removeRow(selectedRow);
            }
            updateTotal();
            pieChartPanel.repaint();
            updateSummary();
        });
        editBtn.setText("UPDATE / EDIT");
        editBtn.setPreferredSize(new Dimension(150,40));
        editBtn.setForeground(new Color(0, 0, 128));
        editBtn.setBackground(new Color(70, 130, 180));
        editBtn.addActionListener(e -> {
            if (!isEditing) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a row first.");
                    return;
                }
                dateField.setText(model.getValueAt(selectedRow, 0).toString());
                categoryBox.setSelectedItem(model.getValueAt(selectedRow, 1).toString());
                descriptionField.setText(model.getValueAt(selectedRow, 2).toString());
                amountField.setText(model.getValueAt(selectedRow, 3).toString());
                editingRow = selectedRow;
                isEditing = true;
                editBtn.setText("Save Changes");
            }
            else {
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String category = categoryBox.getSelectedItem().toString();
                    String description = descriptionField.getText().trim();
                    String date = dateField.getText().trim();
                    if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        JOptionPane.showMessageDialog(this, "Invalid date format. Use dd/MM/yyyy.");
                        return;
                    }
                    model.setValueAt(date, editingRow, 0);
                    model.setValueAt(category, editingRow, 1);
                    model.setValueAt(description, editingRow, 2);
                    model.setValueAt(amount, editingRow, 3);
                    clearFields();
                    isEditing = false;
                    editingRow = -1;
                    editBtn.setText("Update/Edit");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount.");
                }
            }
            updateTotal();
            updateSummary();
        });
        bottomPanel.add(deleteBtn);
        bottomPanel.add(editBtn);

        subPanel1.add(formContainer, BorderLayout.NORTH);
        subPanel1.add(pane, BorderLayout.CENTER);
        subPanel1.add(bottomPanel,BorderLayout.SOUTH);

        pieChartPanel = new PieChartPanel(categoryMap);
        JPanel pieContainer = new JPanel(new BorderLayout());
        pieContainer.setOpaque(false);
        pieContainer.add(pieChartPanel, BorderLayout.CENTER);

        totalLabel = new JLabel("Total Spending: Rs. 0");
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));

        subPanel2.add(Box.createVerticalStrut(20));
        subPanel2.add(Box.createVerticalStrut(10));
        summaryDetails.setLayout(new BoxLayout(summaryDetails, BoxLayout.Y_AXIS));
        summaryDetails.setOpaque(false);
        summaryDetails.add(labelsPanel);
        summaryDetails.add(Box.createVerticalStrut(15));
        summaryDetails.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryDetails.add(pieContainer);
        subPanel2.add(summaryDetails);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.add(totalLabel);
        summaryPanel.add(summaryDetails);

        JScrollPane summaryScroll = new JScrollPane(summaryPanel);
        summaryScroll.setPreferredSize(new Dimension(410, 400));

        saveBtn.setText("Save Expenses");
        saveBtn.setPreferredSize(new Dimension(200,40));
        saveBtn.setForeground(new Color(0, 0, 128));
        saveBtn.setBackground(new Color(70, 130, 180));
        saveBtn.addActionListener(e -> {
            try (FileWriter writer = new FileWriter("expenses.csv",true)) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        writer.write(model.getValueAt(i, j).toString() + ",");
                    }
                    writer.write("\n");
                }
                JOptionPane.showMessageDialog(this, "Expenses saved to expenses.csv");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        });

        loadBtn.setText("Load Expenses");
        loadBtn.setPreferredSize(new Dimension(200,40));
        loadBtn.setForeground(new Color(0, 0, 128));
        loadBtn.setBackground(new Color(70, 130, 180));
        loadBtn.addActionListener(e -> {
            try (BufferedReader br = new BufferedReader(new FileReader("expenses.csv"))) {
                model.setRowCount(0);
                String line;
                boolean firstLine = true;
                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] values = line.split(",");
                    if (values.length == model.getColumnCount()) {
                        model.addRow(values);
                    }
                }
                JOptionPane.showMessageDialog(this, "Expenses loaded from expenses.csv");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage());
            }
            updateTotal();
            updateSummary();
            pieChartPanel.repaint();
        });
        exportBtn.setText("Export to CSV");
        exportBtn.setPreferredSize(new Dimension(200,40));
        exportBtn.setForeground(new Color(0, 0, 128));
        exportBtn.setBackground(new Color(70, 130, 180));
        exportBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export Expenses to CSV");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(fileToSave + ".csv")) {
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        writer.write(model.getColumnName(i) + ",");
                    }
                    writer.write("\n");
                    for (int i = 0; i < model.getRowCount(); i++) {
                        for (int j = 0; j < model.getColumnCount(); j++) {
                            writer.write(model.getValueAt(i, j).toString() + ",");
                        }
                        writer.write("\n");
                    }
                    JOptionPane.showMessageDialog(this, "Exported successfully to " + fileToSave.getAbsolutePath() + ".csv");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting file: " + ex.getMessage());
                }
            }
        });
        subPanel3.add(saveBtn);
        subPanel3.add(loadBtn);
        subPanel3.add(exportBtn);

        bgPanel.add(subPanel1,BorderLayout.WEST);
        comboPanel.add(summaryScroll);
        comboPanel.add(subPanel3);
        bgPanel.add(comboPanel,BorderLayout.EAST);

        setTitle("BUDGET BUDDY");
        setSize(1280,690);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        updateSummary();
        setVisible(true);
    }
    private void updateTotal() {
        double sum = 0;
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                sum += Double.parseDouble(model.getValueAt(i, 3).toString());
            }
            totalLabel.setText("Total Spending: Rs. " + sum);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number!");
            return;
        }
    }
    private void updateSummary() {
        double total = 0;
        labelsPanel.removeAll();
        categoryMap.clear();

        for (int i = 0; i < model.getRowCount(); i++) {
            String category = model.getValueAt(i, 1).toString();
            double amount = Double.parseDouble(model.getValueAt(i, 3).toString());
            categoryMap.put(category, categoryMap.getOrDefault(category, 0.0) + amount);
            total += amount;
        }

        totalLabel.setText("Total Spending: Rs. " + total);

        double max = 0;
        String highestCategory = "None";
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            JLabel lbl = new JLabel(entry.getKey() + ": Rs. " + entry.getValue());
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            lbl.setFont(new Font("Arial", Font.BOLD, 16));
            labelsPanel.add(lbl);

            if (entry.getValue() > max) {
                max = entry.getValue();
                highestCategory = entry.getKey();
            }
        }

        JLabel highestLabel = new JLabel("Highest Spending: " + highestCategory + " (Rs. " + max + ")");
        highestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        highestLabel.setFont(new Font("Arial", Font.BOLD, 18));
        labelsPanel.add(Box.createVerticalStrut(8));
        labelsPanel.add(highestLabel);
        labelsPanel.add(totalLabel);
        labelsPanel.revalidate();
        labelsPanel.repaint();
        pieChartPanel.repaint();
    }

    private void clearFields() {
        amountField.setText("");
        descriptionField.setText("");
        dateField.setText("");
    }
}