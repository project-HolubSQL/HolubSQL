package com.holub.app;

public class CurrentPriceManager {
    private static CurrentPriceManager instance = new CurrentPriceManager();
    private int currentPrice = 0;

    private CurrentPriceManager() {
    }

    public static CurrentPriceManager getInstance() {
        return instance;
    }

    public synchronized void setCurrentPrice(int price) {
        currentPrice = price;
    }

    public synchronized int getCurrentPrice() {
        return currentPrice;
    }
}
