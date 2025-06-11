package com.portfolio.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.portfolio.model.Portfolio;
import com.portfolio.model.Stock;
import com.portfolio.service.PortfolioRebalancer;
import com.portfolio.strategy.EqualWeightStrategy;
import com.portfolio.strategy.RebalancingStrategy;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Map;

public class PortfolioGUI extends JFrame {
    private final Portfolio portfolio;
    private final PortfolioRebalancer rebalancer;
    private final JTable portfolioTable;
    private final DefaultTableModel tableModel;
    private final JTextArea rebalancingOutput;
    private JLabel totalValueLabel;
    private final Color primaryColor = new Color(41, 128, 185);
    private final Color secondaryColor = new Color(52, 152, 219);
    private final Color backgroundColor = new Color(236, 240, 241);
    private final Color textColor = new Color(44, 62, 80);

    public PortfolioGUI() {
        portfolio = new Portfolio();
        rebalancer = new PortfolioRebalancer(new EqualWeightStrategy());
        
        // Set up the frame
        setTitle("Portfolio Rebalancer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(backgroundColor);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Create portfolio table with custom styling
        String[] columnNames = {"Symbol", "Name", "Price", "Quantity", "Value", "Allocation %"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only quantity is editable
            }
        };
        portfolioTable = createStyledTable();
        JScrollPane tableScrollPane = new JScrollPane(portfolioTable);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 10, 0),
            BorderFactory.createLineBorder(primaryColor, 1)
        ));
        
        // Create input panel
        JPanel inputPanel = createInputPanel();
        
        // Create rebalancing output area
        rebalancingOutput = new JTextArea(5, 40);
        rebalancingOutput.setEditable(false);
        rebalancingOutput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rebalancingOutput.setBackground(new Color(255, 255, 255));
        rebalancingOutput.setForeground(textColor);
        JScrollPane outputScrollPane = new JScrollPane(rebalancingOutput);
        outputScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor, 1),
                "Rebalancing Recommendations",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                primaryColor
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(outputScrollPane, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        
        JLabel titleLabel = new JLabel("Portfolio Rebalancer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        
        totalValueLabel = new JLabel("Total Portfolio Value: $0.00");
        totalValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalValueLabel.setForeground(textColor);
        
        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(totalValueLabel, BorderLayout.EAST);
        
        return panel;
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel);
        
        // Style the table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Style the table
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(secondaryColor);
        table.setShowGrid(true);
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        return table;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor, 1),
                "Add New Stock",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                primaryColor
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 8, 0, 8);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField symbolField = createStyledTextField();
        JTextField nameField = createStyledTextField();
        JTextField priceField = createStyledTextField();
        JTextField quantityField = createStyledTextField();

        // Symbol
        gbc.gridx = 0;
        panel.add(createStyledLabel("Symbol:"), gbc);
        gbc.gridx = 1;
        panel.add(symbolField, gbc);

        // Name
        gbc.gridx = 2;
        panel.add(createStyledLabel("Name:"), gbc);
        gbc.gridx = 3;
        panel.add(nameField, gbc);

        // Price
        gbc.gridx = 4;
        panel.add(createStyledLabel("Price:"), gbc);
        gbc.gridx = 5;
        panel.add(priceField, gbc);

        // Quantity
        gbc.gridx = 6;
        panel.add(createStyledLabel("Quantity:"), gbc);
        gbc.gridx = 7;
        panel.add(quantityField, gbc);

        // Add Button
        gbc.gridx = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton addButton = createStyledButton("Add Stock", "add.svg");
        addButton.addActionListener(e -> {
            try {
                String symbol = symbolField.getText().toUpperCase();
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                if (symbol.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please fill in all fields.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Stock stock = new Stock(symbol, name, price, quantity, 0);
                portfolio.addStock(stock);
                updatePortfolioTable();

                // Clear input fields
                symbolField.setText("");
                nameField.setText("");
                priceField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for price and quantity.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBackground(backgroundColor);
        
        JButton rebalanceButton = createStyledButton("Rebalance Portfolio", "rebalance.svg");
        rebalanceButton.addActionListener(e -> {
            updatePortfolioTable();
            Map<String, Integer> actions = rebalancer.rebalance(portfolio);
            
            StringBuilder output = new StringBuilder("Rebalancing Plan:\n\n");
            actions.forEach((symbol, quantity) -> {
                if (quantity > 0) {
                    output.append(String.format("Buy %d shares of %s\n", quantity, symbol));
                } else if (quantity < 0) {
                    output.append(String.format("Sell %d shares of %s\n", Math.abs(quantity), symbol));
                }
            });
            
            rebalancingOutput.setText(output.toString());
        });
        
        JButton clearButton = createStyledButton("Clear Portfolio", "clear.svg");
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear the portfolio?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                portfolio.getStocks().clear();
                updatePortfolioTable();
                rebalancingOutput.setText("");
            }
        });
        
        panel.add(rebalanceButton);
        panel.add(clearButton);
        
        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(secondaryColor, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(textColor);
        return label;
    }

    private JButton createStyledButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(primaryColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(secondaryColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor);
            }
        });
        
        return button;
    }

    private void updatePortfolioTable() {
        tableModel.setRowCount(0);
        portfolio.updateTotalValue();
        
        for (Stock stock : portfolio.getStocks()) {
            Object[] row = {
                stock.getSymbol(),
                stock.getName(),
                String.format("$%.2f", stock.getCurrentPrice()),
                stock.getQuantity(),
                String.format("$%.2f", stock.getCurrentValue()),
                String.format("%.2f%%", stock.getCurrentAllocation(portfolio.getTotalValue()))
            };
            tableModel.addRow(row);
        }
        
        totalValueLabel.setText(String.format("Total Portfolio Value: $%.2f", portfolio.getTotalValue()));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        
        SwingUtilities.invokeLater(() -> {
            new PortfolioGUI().setVisible(true);
        });
    }
} 