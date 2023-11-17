package com.holub.app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class PriceDisplayHandler implements HttpHandler, PropertyChangeListener {

    private volatile int currentPrice;

    public PriceDisplayHandler() {
        CurrentPriceManager.getInstance().addPropertyChangeListener(this);
        this.currentPrice = CurrentPriceManager.getInstance().getCurrentPrice();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("currentPrice".equals(evt.getPropertyName())) {
            this.currentPrice = (Integer) evt.getNewValue();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String response = "Current Price: " + this.currentPrice;

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // 요청이 GET 메소드가 아닌 경우 에러 메시지를 반환합니다.
            String response = "Error: Only GET method is supported";
            exchange.sendResponseHeaders(405, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
