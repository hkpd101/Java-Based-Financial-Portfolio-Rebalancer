package com.portfolio.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.portfolio.model.Asset;
import com.portfolio.model.AssetType;
import com.portfolio.model.Portfolio;
import com.portfolio.service.PortfolioRebalancer;
import com.portfolio.strategy.EqualWeightStrategy;
import com.portfolio.strategy.ThresholdRebalancingStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.event.ChangeListener;
import java.util.Map;

public class PortfolioGUI extends JFrame {
    private final Portfolio portfolio;
    private final JTable portfolioTable;
    private final DefaultTableModel tableModel;
    private final JLabel totalValueLabel;
    private final JComboBox<String> strategyComboBox;
    private final JSpinner thresholdSpinner;
    private PortfolioRebalancer rebalancer;

    public PortfolioGUI() {
        portfolio = new Portfolio();
        portfolio.setName("My Portfolio");

        // Set up the main frame
        setTitle("Portfolio Rebalancer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create the main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Create the portfolio table
        String[] columnNames = {"Symbol", "Name", "Type", "Quantity", "Price", "Value", "Target %", "Current %", "Deviation"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 5 || column == 6; // Only quantity, price, and target % are editable
            }
        };
        portfolioTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(portfolioTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Create the control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Add asset button
        JButton addButton = new JButton("Add Asset");
        addButton.addActionListener(e -> showAddAssetDialog());
        controlPanel.add(addButton);

        // Remove asset button
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> removeSelectedAsset());
        controlPanel.add(removeButton);

        // Strategy selection
        String[] strategies = {"Threshold-Based", "Equal Weight"};
        strategyComboBox = new JComboBox<>(strategies);
        strategyComboBox.addActionListener(e -> updateRebalancer());
        controlPanel.add(new JLabel("Strategy:"));
        controlPanel.add(strategyComboBox);

        // Threshold spinner
        SpinnerNumberModel thresholdModel = new SpinnerNumberModel(0.05, 0.01, 0.5, 0.01);
        thresholdSpinner = new JSpinner(thresholdModel);
        thresholdSpinner.addChangeListener(e -> updateRebalancer());
        controlPanel.add(new JLabel("Threshold:"));
        controlPanel.add(thresholdSpinner);

        // Rebalance button
        JButton rebalanceButton = new JButton("Rebalance");
        rebalanceButton.addActionListener(e -> rebalancePortfolio());
        controlPanel.add(rebalanceButton);

        // Total value label
        totalValueLabel = new JLabel("Total Value: $0.00");
        controlPanel.add(totalValueLabel);

        // Initialize the rebalancer
        updateRebalancer();

        // Set the look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddAssetDialog() {
        JDialog dialog = new JDialog(this, "Add Asset", true);
        dialog.setLayout(new BorderLayout(10, 10));

        // Header panel with description
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("<html><h2>Add New Asset</h2><p style='font-size:10px;'>Fill in the details below to add a new asset to your portfolio.</p></html>");
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Main input panel with titled border
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Asset Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Symbol
        gbc.gridx = 0; gbc.gridy = row;
        JLabel symbolLabel = new JLabel("Symbol:");
        symbolLabel.setToolTipText("Enter the stock/asset symbol (e.g., AAPL)");
        inputPanel.add(symbolLabel, gbc);
        gbc.gridx = 1;
        JTextField symbolField = new JTextField();
        symbolField.setToolTipText("e.g., AAPL");
        symbolField.setColumns(12);
        inputPanel.add(symbolField, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("<html><i>Ticker symbol (e.g., AAPL)</i></html>"), gbc);
        row++;

        // Name
        gbc.gridx = 0; gbc.gridy = row;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setToolTipText("Enter the full name of the asset");
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField();
        nameField.setToolTipText("e.g., Apple Inc.");
        nameField.setColumns(18);
        inputPanel.add(nameField, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("<html><i>Full asset name</i></html>"), gbc);
        row++;

        // Type
        gbc.gridx = 0; gbc.gridy = row;
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setToolTipText("Select the type of asset");
        inputPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        String[] assetTypes = {"STOCK", "BOND", "ETF", "MUTUAL_FUND", "CRYPTO", "OTHER"};
        JComboBox<String> typeComboBox = new JComboBox<>(assetTypes);
        typeComboBox.setToolTipText("Asset type");
        inputPanel.add(typeComboBox, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("<html><i>Stock, Bond, ETF, etc.</i></html>"), gbc);
        row++;

        // Quantity
        gbc.gridx = 0; gbc.gridy = row;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setToolTipText("Enter the number of shares/units");
        inputPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setColumns(8);
        inputPanel.add(quantitySpinner, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("<html><i>Number of units owned</i></html>"), gbc);
        row++;

        // Price
        gbc.gridx = 0; gbc.gridy = row;
        JLabel priceLabel = new JLabel("Price ($):");
        priceLabel.setToolTipText("Current price per share/unit");
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        JSpinner priceSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 0.01));
        ((JSpinner.DefaultEditor) priceSpinner.getEditor()).getTextField().setColumns(8);
        inputPanel.add(priceSpinner, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("<html><i>Current market price</i></html>"), gbc);
        row++;

        // Value (calculated)
        gbc.gridx = 0; gbc.gridy = row;
        JLabel valueLabel = new JLabel("Value ($):");
        valueLabel.setToolTipText("Total value (Quantity × Price)");
        inputPanel.add(valueLabel, gbc);
        gbc.gridx = 1;
        JLabel valueDisplay = new JLabel("$0.00");
        valueDisplay.setForeground(new Color(0, 102, 204));
        inputPanel.add(valueDisplay, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("<html><i>Auto-calculated</i></html>"), gbc);
        row++;

        // Target %
        gbc.gridx = 0; gbc.gridy = row;
        JLabel targetLabel = new JLabel("Target %:");
        targetLabel.setToolTipText("Desired portfolio allocation (e.g., 0.2 for 20%)");
        inputPanel.add(targetLabel, gbc);
        gbc.gridx = 1;
        JSpinner targetSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01));
        ((JSpinner.DefaultEditor) targetSpinner.getEditor()).getTextField().setColumns(8);
        inputPanel.add(targetSpinner, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("<html><i>e.g., 0.2 = 20%</i></html>"), gbc);
        row++;

        // Value update listener
        ChangeListener valueUpdateListener = e -> {
            double quantity = ((Number) quantitySpinner.getValue()).doubleValue();
            double price = ((Number) priceSpinner.getValue()).doubleValue();
            double value = quantity * price;
            valueDisplay.setText(String.format("$%.2f", value));
        };
        quantitySpinner.addChangeListener(valueUpdateListener);
        priceSpinner.addChangeListener(valueUpdateListener);

        dialog.add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            // Input validation
            if (symbolField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a symbol", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a name", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ((Double) targetSpinner.getValue() <= 0) {
                JOptionPane.showMessageDialog(dialog, "Target allocation must be greater than 0", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ((Double) targetSpinner.getValue() > 1) {
                JOptionPane.showMessageDialog(dialog, "Target allocation cannot exceed 100%", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Asset asset = Asset.builder()
                    .symbol(symbolField.getText().toUpperCase().trim())
                    .name(nameField.getText().trim())
                    .type(AssetType.valueOf((String) typeComboBox.getSelectedItem()))
                    .quantity((Integer) quantitySpinner.getValue())
                    .currentPrice((Double) priceSpinner.getValue())
                    .targetAllocation((Double) targetSpinner.getValue())
                    .build();
            portfolio.addAsset(asset);
            updatePortfolioTable();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removeSelectedAsset() {
        int selectedRow = portfolioTable.getSelectedRow();
        if (selectedRow >= 0) {
            String symbol = (String) tableModel.getValueAt(selectedRow, 0);
            portfolio.removeAsset(symbol);
            updatePortfolioTable();
        }
    }

    private void updatePortfolioTable() {
        tableModel.setRowCount(0);
        double totalValue = portfolio.getTotalValue();

        for (Asset asset : portfolio.getAssets()) {
            double currentValue = asset.getCurrentValue();
            double currentAllocation = asset.getCurrentAllocation(totalValue);
            double deviation = asset.getDeviationFromTarget(totalValue);

            tableModel.addRow(new Object[]{
                    asset.getSymbol(),
                    asset.getName(),
                    asset.getType(),
                    asset.getQuantity(),
                    String.format("$%.2f", asset.getCurrentPrice()),
                    String.format("$%.2f", currentValue),
                    String.format("%.1f%%", asset.getTargetAllocation() * 100),
                    String.format("%.1f%%", currentAllocation * 100),
                    String.format("%.1f%%", deviation * 100)
            });
        }

        totalValueLabel.setText(String.format("Total Value: $%.2f", totalValue));
    }

    private void updateRebalancer() {
        String selectedStrategy = (String) strategyComboBox.getSelectedItem();
        double threshold = (Double) thresholdSpinner.getValue();

        if ("Threshold-Based".equals(selectedStrategy)) {
            rebalancer = new PortfolioRebalancer(new ThresholdRebalancingStrategy(), threshold);
        } else {
            rebalancer = new PortfolioRebalancer(new EqualWeightStrategy(), threshold);
        }
    }

    private void rebalancePortfolio() {
        Map<String, Double> trades = rebalancer.rebalance(portfolio);
        
        if (trades.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Portfolio is already balanced within the threshold.",
                    "No Rebalancing Needed",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder message = new StringBuilder("Rebalancing trades needed:\n\n");
        for (Map.Entry<String, Double> trade : trades.entrySet()) {
            message.append(String.format("%s: %s$%.2f\n",
                    trade.getKey(),
                    trade.getValue() > 0 ? "+" : "",
                    trade.getValue()));
        }

        JOptionPane.showMessageDialog(this,
                message.toString(),
                "Rebalancing Trades",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PortfolioGUI().setVisible(true);
        });
    }
} 