package org.example.impl;

import com.jayway.jsonpath.JsonPath;
import org.example.model.Event;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EventBodyHandler implements HttpResponse.BodyHandler<List<Event>> {
    @Override
    public HttpResponse.BodySubscriber<List<Event>> apply(HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8), this::parseJSON);
    }

    private List<Event> parseJSON(String responseBody) {
        List<Event> result = new ArrayList<>();

        List<String> eventNames = JsonPath.read(responseBody, "$._embedded.events[*].name");
        List<String> eventUrls = JsonPath.read(responseBody, "$._embedded.events[*].url");
        List<String> eventDescriptions = JsonPath.read(responseBody, "$._embedded.events[*].classifications[0].segment.name");
        List<String> eventAddresses = JsonPath.read(responseBody, "$._embedded.events[*]._embedded.venues[0].address.line1");
        List<String> eventImg = JsonPath.read(responseBody, "$._embedded.events[*].images[0].url");
        List<String> eventDate = JsonPath.read(responseBody, "$._embedded.events[*].dates.start.localDate");

//        String img= "$._embedded.events[0].images[0].url";
//        String date = "$._embedded.events[0].dates.start.localDate";

        for (int i = 0; i < eventNames.size(); i++) {
            Event event = new Event();
            event.setName(eventNames.get(i));
            event.setUrl(eventUrls.get(i));
            event.setDescription(eventDescriptions.get(i));
            event.setAddress(eventAddresses.get(i));
            event.setImg(eventImg.get(i));
            event.setStartDate(eventDate.get(i));
            result.add(event);
        }
        return result;
    }


}
