package com.holub.app;

import com.holub.database.Database;
import com.holub.database.Table;
import com.holub.text.ParseFailure;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;

public class AuthServer {
  public static void main(String[] args) throws IOException, ParseFailure {

    Database theDatabase = new Database();

    // Read a sequence of SQL statements in from the file
    // Database.test.sql and execute them.

    BufferedReader sql = new BufferedReader(
        new FileReader("Database.test.sql"));
    String test;
    while ((test = sql.readLine()) != null) {
      test = test.trim();
      if (test.length() == 0)
        continue;

      while (test.endsWith("\\")) {
        test = test.substring(0, test.length() - 1);
        test += sql.readLine().trim();
      }

      System.out.println("Parsing: " + test);
      Table result = theDatabase.execute(test);
      // if (result != null)
      // System.out.println(result.toString());
    }

    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/", new RootHandler());
    server.createContext("/login", new LoginHandler(theDatabase));
    server.createContext("/buy", new BuyHandler(theDatabase)); // 매수 핸들러
    server.createContext("/sell", new SellHandler(theDatabase)); // 매도 핸들러

    server.setExecutor(null); // creates a default executor
    server.start();
  }
}

class RootHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String response = "Hello World!";
    exchange.sendResponseHeaders(200, response.length());
    exchange.getResponseBody().write(response.getBytes());
    exchange.getResponseBody().close();
  }
}