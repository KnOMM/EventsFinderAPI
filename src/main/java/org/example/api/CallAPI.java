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
    public static void main(String[] args) {

        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/credentials.properties")) {
            properties.load(fis);
        }
        String apiKey = properties.getProperty("apiKey");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://app.ticketmaster.com/discovery/v2/events.json?size=15&sort=random&apikey=" + apiKey + "&city=tallinn"))
//                .method("GET")
                .header("accept", "application/json")
                .build();
        List<Event> response = client.send(request, new EventBodyHandler()).body();
        response.forEach((ev -> {
            System.out.println(ev.getName() + "\t" + ev.getStartDate() + "\taddress: " + ev.getAddress() + "\tdescription: " + ev.getDescription() + "\timage href: " + ev.getImg());
        }));

    }

}
