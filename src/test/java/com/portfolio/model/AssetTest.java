package com.portfolio.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AssetTest {

    @Test
    void testGetCurrentValue() {
        Asset asset = Asset.builder()
                .symbol("AAPL")
                .name("Apple Inc.")
                .quantity(10)
                .currentPrice(150.0)
                .targetAllocation(0.2)
                .type(AssetType.STOCK)
                .build();

        assertEquals(1500.0, asset.getCurrentValue(), 0.001);
    }

    @Test
    void testGetCurrentAllocation() {
        Asset asset = Asset.builder()
                .symbol("AAPL")
                .name("Apple Inc.")
                .quantity(10)
                .currentPrice(150.0)
                .targetAllocation(0.2)
                .type(AssetType.STOCK)
                .build();

        double totalPortfolioValue = 10000.0;
        assertEquals(0.15, asset.getCurrentAllocation(totalPortfolioValue), 0.001);
    }

    @Test
    void testGetCurrentAllocationWithZeroPortfolioValue() {
        Asset asset = Asset.builder()
                .symbol("AAPL")
                .name("Apple Inc.")
                .quantity(10)
                .currentPrice(150.0)
                .targetAllocation(0.2)
                .type(AssetType.STOCK)
                .build();

        assertEquals(0.0, asset.getCurrentAllocation(0.0), 0.001);
    }

    @Test
    void testGetDeviationFromTarget() {
        Asset asset = Asset.builder()
                .symbol("AAPL")
                .name("Apple Inc.")
                .quantity(10)
                .currentPrice(150.0)
                .targetAllocation(0.2)
                .type(AssetType.STOCK)
                .build();

        double totalPortfolioValue = 10000.0;
        assertEquals(-0.05, asset.getDeviationFromTarget(totalPortfolioValue), 0.001);
    }
} 