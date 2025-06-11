package com.portfolio.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Portfolio {
    private String name;
    private List<Asset> assets;
    private double totalValue;
    private double cashBalance;

    public Portfolio() {
        this.assets = new ArrayList<>();
    }

    public void addAsset(Asset asset) {
        assets.add(asset);
        updateTotalValue();
    }

    public void removeAsset(String symbol) {
        assets.removeIf(asset -> asset.getSymbol().equals(symbol));
        updateTotalValue();
    }

    public void updateTotalValue() {
        this.totalValue = assets.stream()
                .mapToDouble(Asset::getCurrentValue)
                .sum() + cashBalance;
    }

    public Map<String, Double> getCurrentAllocations() {
        return assets.stream()
                .collect(Collectors.toMap(
                    Asset::getSymbol,
                    asset -> asset.getCurrentAllocation(totalValue)
                ));
    }

    public Map<String, Double> getDeviationFromTarget() {
        return assets.stream()
                .collect(Collectors.toMap(
                    Asset::getSymbol,
                    asset -> asset.getDeviationFromTarget(totalValue)
                ));
    }

    public double getTotalDeviation() {
        return assets.stream()
                .mapToDouble(asset -> Math.abs(asset.getDeviationFromTarget(totalValue)))
                .sum();
    }

    public boolean needsRebalancing(double threshold) {
        return getTotalDeviation() > threshold;
    }
} 