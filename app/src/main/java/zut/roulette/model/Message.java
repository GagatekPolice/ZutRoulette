package zut.roulette.model;

import lombok.Getter;
import lombok.Setter;

public class Message {
    @Getter
    @Setter
    private String author;
    @Getter
    @Setter
    private String text;

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
    }
}
