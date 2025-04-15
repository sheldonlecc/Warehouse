package View;

import Controller.WarehouseController;
import Model.*;
import Exception.WarehouseCapacityExceededException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class WarehouseGUI extends JFrame {
    private JTable itemTable;
    private DefaultTableModel tableModel;
    private JButton addButton, useButton, sortButton, searchButton, removeButton;
    private JButton saveButton, loadButton;
    private JTextField searchField;
    private JComboBox<String> sortCriteriaBox, sortOrderBox;
    private WarehouseController controller;
    private JProgressBar capacityBar;
    private JLabel capacityLabel;

    public WarehouseGUI() {
        controller = new WarehouseController();

        // Add some initial items
        try {
            // Add food items
            controller.addItem(new Food("Apple", 0.2, LocalDate.now().plusDays(7)));
            controller.addItem(new Food("Bread", 0.5, LocalDate.now().plusDays(3)));

            // Add drink items
            controller.addItem(new Drink("Cola", 0.5, LocalDate.now().plusMonths(6)));
            controller.addItem(new Drink("Water", 1.0, LocalDate.now().plusYears(1)));

            // Add weapons
            controller.addItem(new Bomb("C4", 1.5));
            controller.addItem(new Bomb("Grenade", 0.5));
            controller.addItem(new Gun("Pistol", 1.0, 12));
            controller.addItem(new Gun("Rifle", 3.0, 30));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding initial items: " + e.getMessage());
        }

        setTitle("Warehouse Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize table model
        String[] columnNames = {"Type", "Name", "Weight", "Expiration", "Bullets"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable = new JTable(tableModel);

        // Initialize components
        addButton = new JButton("Add Item");
        useButton = new JButton("Use Item");
        sortButton = new JButton("Sort Items");
        searchButton = new JButton("Search");
        removeButton = new JButton("Remove Item");
        saveButton = new JButton("Save Data");
        loadButton = new JButton("Load Data");
        searchField = new JTextField(15);
        sortCriteriaBox = new JComboBox<>(new String[]{"weight", "name", "type"});
        sortOrderBox = new JComboBox<>(new String[]{"Ascending", "Descending"});

        // Initialize capacity components
        capacityBar = new JProgressBar(0, 100);
        capacityBar.setStringPainted(true);
        capacityLabel = new JLabel();

        // Layout setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top panel for search and sort
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Sort by:"));
        topPanel.add(sortCriteriaBox);
        topPanel.add(sortOrderBox);
        topPanel.add(sortButton);

        // Center panel for table and capacity
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(itemTable), BorderLayout.CENTER);

        // Capacity panel
        JPanel capacityPanel = new JPanel(new BorderLayout());
        capacityPanel.add(new JLabel("Warehouse Capacity: "), BorderLayout.WEST);
        capacityPanel.add(capacityBar, BorderLayout.CENTER);
        capacityPanel.add(capacityLabel, BorderLayout.EAST);

        // Add table and capacity to center panel
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(capacityPanel, BorderLayout.SOUTH);

        // Bottom panel for actions
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(useButton);
        bottomPanel.add(removeButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);

        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddItemDialog();
            }
        });

        useButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useSelectedItem();
            }
        });

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String criteria = (String) sortCriteriaBox.getSelectedItem();
                boolean ascending = sortOrderBox.getSelectedItem().equals("Ascending");
                controller.sortItems(criteria, ascending);
                refreshTable();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().toLowerCase();
                refreshTable(searchText);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedItem();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveWarehouseData();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadWarehouseData();
            }
        });

        // Initial table refresh
        refreshTable();
        updateCapacityDisplay();
    }

    private void showAddItemDialog() {
        JDialog dialog = new JDialog(this, "Add New Item", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));

        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Food", "Drink", "Bomb", "Gun"});
        JTextField nameField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField expirationField = new JTextField();
        JTextField bulletsField = new JTextField();

        dialog.add(new JLabel("Type:"));
        dialog.add(typeBox);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Weight:"));
        dialog.add(weightField);
        dialog.add(new JLabel("Expiration (yyyy-MM-dd):"));
        dialog.add(expirationField);
        dialog.add(new JLabel("Bullets (for Gun):"));
        dialog.add(bulletsField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                String type = (String) typeBox.getSelectedItem();
                String name = nameField.getText();
                double weight = Double.parseDouble(weightField.getText());

                switch (type) {
                    case "Food":
                    case "Drink":
                        LocalDate expiration = LocalDate.parse(expirationField.getText());
                        if (type.equals("Food")) {
                            controller.addItem(new Food(name, weight, expiration));
                        } else {
                            controller.addItem(new Drink(name, weight, expiration));
                        }
                        break;
                    case "Bomb":
                        controller.addItem(new Bomb(name, weight));
                        break;
                    case "Gun":
                        int bullets = Integer.parseInt(bulletsField.getText());
                        controller.addItem(new Gun(name, weight, bullets));
                        break;
                }
                refreshTable();
                updateCapacityDisplay();
                dialog.dispose();
            } catch (WarehouseCapacityExceededException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage());
            }
        });

        dialog.add(addButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void useSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to use");
            return;
        }

        String itemName = (String) tableModel.getValueAt(selectedRow, 1);
        try {
            controller.useItem(itemName);
            refreshTable();
            updateCapacityDisplay();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void removeSelectedItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove");
            return;
        }

        String itemName = (String) tableModel.getValueAt(selectedRow, 1);
        controller.removeItem(itemName);
        refreshTable();
        updateCapacityDisplay();
    }

    private void refreshTable() {
        refreshTable("");
    }

    private void refreshTable(String searchText) {
        tableModel.setRowCount(0);
        List<Item> items = controller.getItems();
        boolean found = false;

        for (Item item : items) {
            if (searchText.isEmpty() || item.getName().toLowerCase().contains(searchText)) {
                found = true;
                Object[] rowData = new Object[5];
                rowData[0] = item.getClass().getSimpleName();
                rowData[1] = item.getName();
                rowData[2] = item.getWeight();

                if (item instanceof Consumable) {
                    rowData[3] = ((Consumable) item).getExpirationDate();
                }

                if (item instanceof Gun) {
                    rowData[4] = ((Gun) item).getBulletCount();
                }

                tableModel.addRow(rowData);
            }
        }

        if (!found && !searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items found matching: " + searchText);
        }
    }

    private void updateCapacityDisplay() {
        double currentCapacity = controller.getCurrentCapacity();
        double maxCapacity = controller.getMaxCapacity();
        double percentage = (currentCapacity / maxCapacity) * 100;

        capacityBar.setValue((int) percentage);
        capacityLabel.setText(String.format("%.1f/%.1f (%.1f%%)",
                currentCapacity, maxCapacity, percentage));

        // Change color based on capacity
        if (percentage >= 90) {
            capacityBar.setForeground(Color.RED);
        } else if (percentage >= 70) {
            capacityBar.setForeground(Color.ORANGE);
        } else {
            capacityBar.setForeground(Color.GREEN);
        }
    }

    private void saveWarehouseData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存仓库数据");
        
        // 设置默认文件名和扩展名
        fileChooser.setSelectedFile(new File("warehouse_data.json"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            
            // 确保文件名以.json结尾
            if (!filePath.toLowerCase().endsWith(".json")) {
                filePath += ".json";
            }
            
            try {
                controller.saveWarehouseData(filePath);
                JOptionPane.showMessageDialog(this, "数据成功保存到: " + filePath);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "保存数据时出错: " + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadWarehouseData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("加载仓库数据");
        
        // 添加文件过滤器，只显示JSON文件
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".json");
            }
            
            @Override
            public String getDescription() {
                return "JSON 文件 (*.json)";
            }
        });
        
        int userSelection = fileChooser.showOpenDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            
            try {
                controller.loadWarehouseData(fileToLoad.getAbsolutePath());
                refreshTable();
                updateCapacityDisplay();
                JOptionPane.showMessageDialog(this, "数据成功加载");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "加载数据时出错: " + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "处理数据时出错: " + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WarehouseGUI().setVisible(true);
            }
        });
    }
}