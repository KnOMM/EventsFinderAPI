package org.example.impl;

import org.example.model.Event;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsoupHandler {
    private static final String URL = "https://www.elbphilharmonie.de/en/whats-on/EPHH/TICKETS/FOR/EIN/EXF/";
    private static final String AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36 Google Favicon";
    private static final String BASE = "https://www.elbphilharmonie.de";

    public static List<Event> main() throws IOException {
        Document doc = Jsoup
                .connect(URL)
                .userAgent(AGENT)
                .ignoreHttpErrors(true)
                .followRedirects(true)
                .ignoreContentType(true)
                .get();

        Elements elements = doc.select(".event-item");
        List<Event> result = new ArrayList<>();

        for (Element el : elements) {
            String date = el.select("time>.date.nobreak").text() + " " + el.select("time>span.time").text();

            Event event = new Event();
            event.setName(el.select(".event-title.h2.no-line").text());
            event.setUrl(BASE + el.select("a").attr("href"));
            event.setDescription(el.select("p.event-subtitle").text());
            event.setAddress(el.select("span.caption.uppercase").text());
            event.setImg(el.select("img").attr("src"));
            event.setStartDate(date);
            result.add(event);
            System.out.println(event.toString());
        }
        return result;
    }
}
