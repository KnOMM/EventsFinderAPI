package org.example.model;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Event {
    private String name;
    private String description;
    private String url;
    private String address;
    private String img;
    private String startDate;

    @Override
    public String toString() {
        return '\'' + name + '\'' +
                "\nDescription: " + description +
                "\nAddress: " + address +
                "\nDate: " + startDate +
                "\nURL: " + url;
    }
}
