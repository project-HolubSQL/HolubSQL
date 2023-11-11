package com.holub.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.holub.database.*;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginHandler implements HttpHandler {
  private Database db;

  public LoginHandler(Database db) {
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
      String username = params.get("username");
      String password = params.get("password");

      // Verify credentials
      boolean isAuthenticated = authenticateUser(username, password);

      // Send appropriate response
      String response = isAuthenticated ? "Authenticated" : "Authentication Failed";
      exchange.sendResponseHeaders(isAuthenticated ? 200 : 403, response.length());
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

  private boolean authenticateUser(String username, String password) {
    try {
      // Execute a SELECT query to check for the username and password
      String query = String.format("SELECT * FROM users WHERE username='%s' AND password='%s'", username, password);
      Table result = db.execute(query);

      // Check if any row is returned
      return result.rows().advance();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private Map<String, String> parseQuery(String query) throws UnsupportedEncodingException {
    // Regular expression to match key-value pairs in the JSON string
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
}
