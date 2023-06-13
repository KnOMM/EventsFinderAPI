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
}
