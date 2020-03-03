package zut.roulette.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import zut.roulette.room.dao.MessageDao;
import zut.roulette.room.entity.Message;

@Database(entities = Message.class, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    private static MessageDatabase instance;

    public abstract MessageDao messageDao();

    public static synchronized MessageDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MessageDatabase.class, "message_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
