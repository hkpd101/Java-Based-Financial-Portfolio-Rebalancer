package com.portfolio;

import com.portfolio.gui.PortfolioGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PortfolioGUI gui = new PortfolioGUI();
            gui.setVisible(true);
        });
    }
} 