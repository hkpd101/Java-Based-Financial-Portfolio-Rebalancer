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
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Symbol field
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Symbol:"), gbc);
        gbc.gridx = 1;
        JTextField symbolField = new JTextField(10);
        dialog.add(symbolField, gbc);

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        dialog.add(nameField, gbc);

        // Type field
        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<AssetType> typeComboBox = new JComboBox<>(AssetType.values());
        dialog.add(typeComboBox, gbc);

        // Quantity field
        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        dialog.add(quantitySpinner, gbc);

        // Price field
        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        JSpinner priceSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 0.01));
        dialog.add(priceSpinner, gbc);

        // Target allocation field
        gbc.gridx = 0;
        gbc.gridy = 5;
        dialog.add(new JLabel("Target %:"), gbc);
        gbc.gridx = 1;
        JSpinner targetSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01));
        dialog.add(targetSpinner, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        addButton.addActionListener(e -> {
            Asset asset = Asset.builder()
                    .symbol(symbolField.getText().toUpperCase())
                    .name(nameField.getText())
                    .type((AssetType) typeComboBox.getSelectedItem())
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