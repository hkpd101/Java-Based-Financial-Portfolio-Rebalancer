package com.portfolio.strategy;

import com.portfolio.model.Portfolio;
import java.util.Map;

public interface RebalancingStrategy {
    Map<String, Integer> calculateRebalancing(Portfolio portfolio);
    String getStrategyName();
} 