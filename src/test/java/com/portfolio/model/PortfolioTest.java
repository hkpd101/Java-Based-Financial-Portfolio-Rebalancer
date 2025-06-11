package com.portfolio.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class PortfolioTest {
    private Portfolio portfolio;
    private Asset appleStock;
    private Asset microsoftStock;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        portfolio.setName("Test Portfolio");
        portfolio.setCashBalance(1000.0);

        appleStock = Asset.builder()
                .symbol("AAPL")
                .name("Apple Inc.")
                .quantity(10)
                .currentPrice(150.0)
                .targetAllocation(0.6)
                .type(AssetType.STOCK)
                .build();

        microsoftStock = Asset.builder()
                .symbol("MSFT")
                .name("Microsoft Corp.")
                .quantity(5)
                .currentPrice(300.0)
                .targetAllocation(0.4)
                .type(AssetType.STOCK)
                .build();
    }

    @Test
    void testAddAndRemoveAsset() {
        portfolio.addAsset(appleStock);
        assertEquals(1, portfolio.getAssets().size());
        assertEquals(2500.0, portfolio.getTotalValue(), 0.001);

        portfolio.addAsset(microsoftStock);
        assertEquals(2, portfolio.getAssets().size());
        assertEquals(4000.0, portfolio.getTotalValue(), 0.001);

        portfolio.removeAsset("AAPL");
        assertEquals(1, portfolio.getAssets().size());
        assertEquals(2500.0, portfolio.getTotalValue(), 0.001);
    }

    @Test
    void testGetCurrentAllocations() {
        portfolio.addAsset(appleStock);
        portfolio.addAsset(microsoftStock);

        Map<String, Double> allocations = portfolio.getCurrentAllocations();
        assertEquals(0.375, allocations.get("AAPL"), 0.001);
        assertEquals(0.375, allocations.get("MSFT"), 0.001);
    }

    @Test
    void testGetDeviationFromTarget() {
        portfolio.addAsset(appleStock);
        portfolio.addAsset(microsoftStock);

        Map<String, Double> deviations = portfolio.getDeviationFromTarget();
        assertEquals(-0.225, deviations.get("AAPL"), 0.001);
        assertEquals(-0.025, deviations.get("MSFT"), 0.001);
    }

    @Test
    void testGetTotalDeviation() {
        portfolio.addAsset(appleStock);
        portfolio.addAsset(microsoftStock);

        double totalDeviation = portfolio.getTotalDeviation();
        assertEquals(0.25, totalDeviation, 0.001);
    }

    @Test
    void testNeedsRebalancing() {
        portfolio.addAsset(appleStock);
        portfolio.addAsset(microsoftStock);

        assertTrue(portfolio.needsRebalancing(0.2));
        assertFalse(portfolio.needsRebalancing(0.3));
    }
} 