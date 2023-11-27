package com.holub.app;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class CurrentPriceManager {
    private static CurrentPriceManager instance = new CurrentPriceManager();
    private int currentPrice;
    private PropertyChangeSupport support;

    private CurrentPriceManager() {
        support = new PropertyChangeSupport(this);
    }

    public static CurrentPriceManager getInstance() {
        return instance;
    }

    public synchronized void setCurrentPrice(int price) {
        int oldPrice = currentPrice;
        this.currentPrice = price;
        support.firePropertyChange("currentPrice", oldPrice, price);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public synchronized int getCurrentPrice() {
        return currentPrice;
    }
}
