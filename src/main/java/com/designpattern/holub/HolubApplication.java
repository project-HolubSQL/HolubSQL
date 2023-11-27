package com.designpattern.holub;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holub.app.BuyHandler;
import com.holub.app.CurrentPriceManager;
import com.holub.app.LoginHandler;
import com.holub.app.PriceDisplayHandler;
import com.holub.app.SellHandler;
import com.holub.text.ParseFailure;

import com.designpattern.database.Database;
import com.designpattern.database.Table;

@SpringBootApplication
public class HolubApplication {

	public static void main(String[] args) throws IOException, ParseFailure {
		try {
			Database theDatabase = new Database();
			BufferedReader sql = new BufferedReader(new FileReader("Database.test.sql"));
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
			CurrentPriceManager.getInstance().setCurrentPrice(700);

			// SpringApplication.run(HolubApplication.class, args);

			HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
			server.createContext("/", new RootHandler());
			server.createContext("/login", new LoginHandler(theDatabase));
			server.createContext("/buy", new BuyHandler(theDatabase));
			server.createContext("/sell", new SellHandler(theDatabase));
			server.createContext("/price", new PriceDisplayHandler());

			server.setExecutor(null); // creates a default executor
			server.start();

		} catch (FileNotFoundException e) {
			// Handle the exception here
			e.printStackTrace();
		} catch (IOException e) {
			// Handle the exception here
			e.printStackTrace();
		}
	}

}

@RestController
class HelloWorldController {

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello, World!";
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