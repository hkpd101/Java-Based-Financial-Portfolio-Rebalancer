package com.portfolio.strategy;

import com.portfolio.model.Asset;
import com.portfolio.model.AssetType;
import com.portfolio.model.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class ThresholdRebalancingStrategyTest {
    private Portfolio portfolio;
    private ThresholdRebalancingStrategy strategy;
    private Asset appleStock;
    private Asset microsoftStock;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        portfolio.setName("Test Portfolio");
        portfolio.setCashBalance(1000.0);
        strategy = new ThresholdRebalancingStrategy();

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

        portfolio.addAsset(appleStock);
        portfolio.addAsset(microsoftStock);
    }

    @Test
    void testCalculateRebalancingTradesWithHighThreshold() {
        Map<String, Double> trades = strategy.calculateRebalancingTrades(portfolio, 0.3);
        assertTrue(trades.isEmpty(), "No trades should be suggested with high threshold");
    }

    @Test
    void testCalculateRebalancingTradesWithLowThreshold() {
        Map<String, Double> trades = strategy.calculateRebalancingTrades(portfolio, 0.1);
        
        assertFalse(trades.isEmpty(), "Trades should be suggested with low threshold");
        assertTrue(trades.containsKey("AAPL"), "AAPL should be present in trades");
        assertEquals(900.0, trades.get("AAPL"), 0.001, "AAPL should be bought");
        assertFalse(trades.containsKey("MSFT"), "MSFT should not be present in trades");
    }

    @Test
    void testGetStrategyName() {
        assertEquals("Threshold-Based Rebalancing", strategy.getStrategyName());
    }

    @Test
    void testGetStrategyDescription() {
        assertNotNull(strategy.getStrategyDescription());
        assertTrue(strategy.getStrategyDescription().contains("threshold"));
    }
} 