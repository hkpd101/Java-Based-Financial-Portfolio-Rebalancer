package com.portfolio.strategy;

import com.portfolio.model.Portfolio;
import java.util.HashMap;
import java.util.Map;

public class ThresholdRebalancingStrategy implements RebalancingStrategy {
    @Override
    public Map<String, Double> calculateRebalancingTrades(Portfolio portfolio, double threshold) {
        Map<String, Double> trades = new HashMap<>();
        Map<String, Double> deviations = portfolio.getDeviationFromTarget();
        
        for (Map.Entry<String, Double> entry : deviations.entrySet()) {
            double deviation = entry.getValue();
            if (Math.abs(deviation) > threshold) {
                // Calculate the amount needed to rebalance
                double tradeAmount = -deviation * portfolio.getTotalValue();
                trades.put(entry.getKey(), tradeAmount);
            }
        }
        
        return trades;
    }

    @Override
    public String getStrategyName() {
        return "Threshold-Based Rebalancing";
    }

    @Override
    public String getStrategyDescription() {
        return "Rebalances assets when their allocation deviates from target by more than the specified threshold";
    }
} 