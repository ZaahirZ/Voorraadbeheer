package org.voorraadbeheer.Classes;

import org.voorraadbeheer.Patterns.Observer;

import java.util.ArrayList;
import java.util.List;

public class ProductObserver {
    private static ProductObserver instance;
    private List<Observer> observers = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    public ProductObserver() {
    }

    public static synchronized ProductObserver getInstance() {
        if (instance == null) {
            instance = new ProductObserver();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addProduct(Product product) {
        products.add(product);
        notifyObservers();
    }

    public void removeProduct(Product product) {
        products.remove(product);
        notifyObservers();
    }

    public List<Product> getProducts() {
        return products;
    }
}
