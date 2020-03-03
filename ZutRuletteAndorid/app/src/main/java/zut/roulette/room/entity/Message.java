package zut.roulette.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "message_table")
public class Message {

    @Getter @Setter @PrimaryKey(autoGenerate = true)
    private int id;

    @Getter
    private int chatId;

    @Getter
    private int sender;

    @Getter
    private int receiver;

    @Getter
    private String message;

    public Message(int chatId, int sender, int receiver, String message) {
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
}
