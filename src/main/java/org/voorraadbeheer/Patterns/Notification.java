package org.voorraadbeheer.Patterns;

public abstract class Notification implements Observer {
    @Override
    public void update(){
        checkLowStockProducts();
    }

    public abstract void showNotification();
    public abstract void checkLowStockProducts();
}
