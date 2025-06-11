package com.portfolio.service;

import com.portfolio.model.Portfolio;
import com.portfolio.strategy.RebalancingStrategy;
import java.util.Map;

public class PortfolioRebalancer {
    private final RebalancingStrategy strategy;
    
    public PortfolioRebalancer(RebalancingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public Map<String, Integer> rebalance(Portfolio portfolio) {
        portfolio.updateTotalValue();
        return strategy.calculateRebalancing(portfolio);
    }
    
    public void printRebalancingPlan(Portfolio portfolio) {
        Map<String, Integer> actions = rebalance(portfolio);
        
        System.out.println("\nRebalancing Plan (" + strategy.getStrategyName() + "):");
        System.out.println("----------------------------------------");
        
        actions.forEach((symbol, quantity) -> {
            if (quantity > 0) {
                System.out.printf("Buy %d shares of %s%n", quantity, symbol);
            } else if (quantity < 0) {
                System.out.printf("Sell %d shares of %s%n", Math.abs(quantity), symbol);
            }
        });
        
        System.out.println("----------------------------------------");
    }
} 