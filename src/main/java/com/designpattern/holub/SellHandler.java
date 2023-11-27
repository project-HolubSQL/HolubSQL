package com.holub.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.designpattern.database.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SellHandler implements HttpHandler {
    private Database db;
    private int id_count = 0;

    public SellHandler(Database db) {
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

            processSellOrder(price, quantity);
            String response = "Sell order processed";
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

    private void processSellOrder(int price, int quantity) {
        int remainingQuantity = matchWithBuyOrders(price, quantity);
        try {
            if (remainingQuantity > 0) {
                String insertQuery = String.format("INSERT INTO sells VALUES (%d, %d, %d)", ++id_count, price,
                        remainingQuantity);
                db.execute(insertQuery);
                // Table result = db.execute("select * from sells");
                // System.out.println(result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("매도 주문 처리 완료:" + price + " " + quantity + "남은 수량: " + remainingQuantity);
    }

    private int matchWithBuyOrders(int price, int quantity) {
        try {
            String selectQuery = String.format("SELECT * FROM buys WHERE price >= %d ORDER BY price DESC", price);
            Table buyTable = db.execute(selectQuery);
            Cursor buyOrders = buyTable.rows();

            int remainingQuantity = quantity;

            while (buyOrders.advance()) {
                int buyId = Integer.parseInt((String) buyOrders.column("id"));
                int buyPrice = Integer.parseInt((String) buyOrders.column("price"));
                int buyQuantity = Integer.parseInt((String) buyOrders.column("quantity"));

                if (buyQuantity <= remainingQuantity) {
                    String deleteQuery = String.format("DELETE FROM buys WHERE id = %d", buyId);
                    db.execute(deleteQuery);
                    CurrentPriceManager.getInstance().setCurrentPrice(buyPrice);
                    remainingQuantity -= buyQuantity;
                } else {
                    int newQuantity = buyQuantity - remainingQuantity;
                    String updateQuery = String.format("UPDATE buys SET quantity = %d WHERE id = %d", newQuantity,
                            buyId);
                    db.execute(updateQuery);
                    CurrentPriceManager.getInstance().setCurrentPrice(buyPrice);
                    remainingQuantity = 0;
                    break;
                }

                if (remainingQuantity <= 0)
                    break;
            }
            return remainingQuantity;
        } catch (Exception e) {
            System.out.println("Buy order matching failed");
            e.printStackTrace();
            return quantity;
        }
    }
}
