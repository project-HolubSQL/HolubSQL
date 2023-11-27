package com.holub.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.designpattern.database.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyHandler implements HttpHandler {
    private Database db;
    private int id_count = 0;

    public BuyHandler(Database db) {
        this.db = db;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");

            BufferedReader br = new BufferedReader(isr);
            StringBuilder queryBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                queryBuilder.append(line);
            }
            String query = queryBuilder.toString();

            Map<String, String> params = parseQuery(query);
            int price = Integer.parseInt(params.get("price"));
            int quantity = Integer.parseInt(params.get("quantity"));

            processBuyOrder(price, quantity);
            String response = "Buy order processed";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private Map<String, String> parseQuery(String query) throws UnsupportedEncodingException {
        Pattern pattern = Pattern.compile("\"(\\w+)\":\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(query);

        Map<String, String> params = new HashMap<>();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            params.put(key, value);
        }

        return params;
    }

    private boolean processBuyOrder(int price, int quantity) {
        int res = matchWithSellOrders(price, quantity);
        try {
            if (res > 0) {
                String insertQuery = String.format("INSERT INTO buys VALUES (%d, %d, %d)", ++id_count, price, res);
                db.execute(insertQuery);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("매수 주문 처리 완료:" + price + " " + quantity + "남은 수량: " + res);
        return true;

    }

    private int matchWithSellOrders(int price, int quantity) {
        // 현재 매도 주문 중 매수 가격보다 낮거나 같은 최저 가격의 매도 주문 찾기
        try {
            // 매수 가격보다 낮거나 같은 모든 매도 주문 찾기
            String selectQuery = String.format("SELECT * FROM sells WHERE price <= %d ORDER BY price ASC", price);

            // 매수 가격보다 낮거나 같은 모든 매도 주문 찾기
            Table sellTable = db.execute(selectQuery);
            Cursor sellOrders = sellTable.rows();

            // 매수 주문의 남은 수량을 추적
            int remainingQuantity = quantity;

            // 찾은 매도 주문들을 순회하며 거래 체결
            while (sellOrders.advance()) {
                int sellId = Integer.parseInt((String) sellOrders.column("id"));
                int sellPrice = Integer.parseInt((String) sellOrders.column("price"));
                int sellQuantity = Integer.parseInt((String) sellOrders.column("quantity"));
                if (sellQuantity <= remainingQuantity) {
                    // 매도 수량 전체가 매수 수량에 포함될 경우
                    String deleteQuery = String.format("DELETE FROM sells WHERE id = %d", sellId);
                    db.execute(deleteQuery);
                    CurrentPriceManager.getInstance().setCurrentPrice(sellPrice);
                    remainingQuantity -= sellQuantity;

                } else {
                    int newQuantity = sellQuantity - remainingQuantity;
                    String updateQuery = String.format("UPDATE sells SET quantity = %d WHERE id = %d", newQuantity,
                            sellId);
                    db.execute(updateQuery);
                    CurrentPriceManager.getInstance().setCurrentPrice(sellPrice);
                    remainingQuantity = 0;
                    break; // 모든 매수 수량이 소진되었으므로 반복 종료
                }

                if (remainingQuantity <= 0)
                    break; // 모든 매수 수량이 소진되었으므로 반복 종료
            }
            return remainingQuantity;

        } catch (Exception e) {
            System.out.println("Sell order matching failed");
            e.printStackTrace();
            return quantity;
        }

    }
}
