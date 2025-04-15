package View;

import Controller.WarehouseController;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class WarehouseGUI extends JFrame {
    private JTable itemTable;
    private DefaultTableModel tableModel;
    private JButton addButton, useButton, sortButton, searchButton, removeButton;
    private JTextField searchField;
    private JComboBox<String> sortCriteriaBox, sortOrderBox;
    private WarehouseController controller;

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
            System.out.println("Error adding initial items: " + e.getMessage());
        }
        
        setTitle("Model.Warehouse Management System");
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
        addButton = new JButton("Add Model.Item");
        useButton = new JButton("Use Model.Item");
        sortButton = new JButton("Sort Items");
        searchButton = new JButton("Search");
        removeButton = new JButton("Remove Model.Item");
        searchField = new JTextField(15);
        sortCriteriaBox = new JComboBox<>(new String[]{"weight", "name", "type"});
        sortOrderBox = new JComboBox<>(new String[]{"Ascending", "Descending"});

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top panel for search and sort
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(new JLabel("Sort by:"));
        topPanel.add(sortCriteriaBox);
        topPanel.add(sortOrderBox);
        topPanel.add(sortButton);

        // Bottom panel for actions
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(useButton);
        bottomPanel.add(removeButton);

        // Add components to main panel
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(panel);

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

        // Initial table refresh
        refreshTable();
    }

    private void showAddItemDialog() {
        JDialog dialog = new JDialog(this, "Add New Model.Item", true);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Model.Food", "Model.Drink", "Model.Bomb", "Model.Gun"});
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
        dialog.add(new JLabel("Bullets (for Model.Gun):"));
        dialog.add(bulletsField);
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                String type = (String) typeBox.getSelectedItem();
                String name = nameField.getText();
                double weight = Double.parseDouble(weightField.getText());
                
                switch (type) {
                    case "Model.Food":
                    case "Model.Drink":
                        LocalDate expiration = LocalDate.parse(expirationField.getText());
                        if (type.equals("Model.Food")) {
                            controller.addItem(new Food(name, weight, expiration));
                        } else {
                            controller.addItem(new Drink(name, weight, expiration));
                        }
                        break;
                    case "Model.Bomb":
                        controller.addItem(new Bomb(name, weight));
                        break;
                    case "Model.Gun":
                        int bullets = Integer.parseInt(bulletsField.getText());
                        controller.addItem(new Gun(name, weight, bullets));
                        break;
                }
                refreshTable();
                dialog.dispose();
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
        
        if (!searchText.isEmpty() && !found) {
            JOptionPane.showMessageDialog(this, 
                "No items found matching: " + searchText, 
                "Search Results", 
                JOptionPane.INFORMATION_MESSAGE);
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