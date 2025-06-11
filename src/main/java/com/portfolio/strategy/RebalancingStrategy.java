package com.portfolio.strategy;

import com.portfolio.model.Portfolio;
import java.util.Map;

public interface RebalancingStrategy {
    Map<String, Double> calculateRebalancingTrades(Portfolio portfolio, double threshold);
    String getStrategyName();
    String getStrategyDescription();
} 