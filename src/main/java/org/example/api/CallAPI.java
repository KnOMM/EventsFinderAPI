package org.example.api;

import lombok.SneakyThrows;
import org.example.impl.EventBodyHandler;
import org.example.model.Event;

import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;

public class CallAPI {

    @SneakyThrows
    public static List<Event> findEvents(String sort, String size) {

        Properties properties = new Properties();
//        try (FileInputStream fis = new FileInputStream("src/main/resources/credentials.properties")) {
        try (FileInputStream fis = new FileInputStream("target/credentials.properties")) { // when bot is deployed
            properties.load(fis);
        }
        String apiKey = properties.getProperty("apiKey");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://app.ticketmaster.com/discovery/v2/events.json?size=" + size + "&sort=" + sort + "&apikey=" + apiKey + "&city=hamburg"))
//                .method("GET")
                .header("accept", "application/json")
                .build();
        HttpResponse<List<Event>> response = client.send(request, new EventBodyHandler());
        if (response.statusCode() == 200) {
            List<Event> events = response.body();

            events.forEach((ev) -> {
                System.out.println(ev.getName() + "\t" + ev.getStartDate() + "\taddress: " + ev.getAddress() + "\tdescription: " + ev.getDescription());
            });
        } else {
            System.out.println("Request failed with status code: " + response.statusCode());
        }
        return response.body();
    }

}
