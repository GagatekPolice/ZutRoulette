package zut.roulette.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import zut.roulette.room.entity.Message;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    @Delete
    void delete(Message message);

    @Query("DELETE FROM message_table")
    void deleteAllMessages();

    @Query("SELECT * FROM message_table")
    LiveData<List<Message>> getAllMessages();
}
