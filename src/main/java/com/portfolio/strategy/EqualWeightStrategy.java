package com.portfolio.strategy;

import com.portfolio.model.Portfolio;
import com.portfolio.model.Stock;
import java.util.HashMap;
import java.util.Map;

public class EqualWeightStrategy implements RebalancingStrategy {
    @Override
    public Map<String, Integer> calculateRebalancing(Portfolio portfolio) {
        Map<String, Integer> rebalancingActions = new HashMap<>();
        double equalWeight = 100.0 / portfolio.getStocks().size();
        
        for (Stock stock : portfolio.getStocks()) {
            double targetValue = (equalWeight / 100.0) * portfolio.getTotalValue();
            int targetQuantity = (int) Math.round(targetValue / stock.getCurrentPrice());
            int currentQuantity = stock.getQuantity();
            
            if (targetQuantity != currentQuantity) {
                rebalancingActions.put(stock.getSymbol(), targetQuantity - currentQuantity);
            }
        }
        
        return rebalancingActions;
    }
    
    @Override
    public String getStrategyName() {
        return "Equal Weight Strategy";
    }
} 