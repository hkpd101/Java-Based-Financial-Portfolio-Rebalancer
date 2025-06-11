package com.portfolio.service;

import com.portfolio.model.Portfolio;
import com.portfolio.strategy.RebalancingStrategy;
import java.util.Map;

public class PortfolioRebalancer {
    private final RebalancingStrategy strategy;
    private final double threshold;

    public PortfolioRebalancer(RebalancingStrategy strategy, double threshold) {
        this.strategy = strategy;
        this.threshold = threshold;
    }

    public Map<String, Double> rebalance(Portfolio portfolio) {
        if (!portfolio.needsRebalancing(threshold)) {
            return Map.of();
        }
        return strategy.calculateRebalancingTrades(portfolio, threshold);
    }

    public String getStrategyName() {
        return strategy.getStrategyName();
    }

    public String getStrategyDescription() {
        return strategy.getStrategyDescription();
    }

    public void printRebalancingPlan(Portfolio portfolio) {
        Map<String, Double> actions = rebalance(portfolio);
        
        System.out.println("\nRebalancing Plan (" + strategy.getStrategyName() + "):");
        System.out.println("----------------------------------------");
        
        actions.forEach((symbol, amount) -> {
            if (amount > 0) {
                System.out.printf("Buy $%.2f of %s%n", amount, symbol);
            } else if (amount < 0) {
                System.out.printf("Sell $%.2f of %s%n", Math.abs(amount), symbol);
            }
        });
        
        System.out.println("----------------------------------------");
    }
} 