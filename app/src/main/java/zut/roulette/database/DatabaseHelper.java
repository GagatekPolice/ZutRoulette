package zut.roulette.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER = "userId";
    private static final String COLUMN_NICKNAME = "nickname";
    private static final String COLUMN_INTERLOCUTOR = "interlocutor";
    private static final String COLUMN_INTERLOCUTOR_NICKNAME = "interlocutorNickname";
    private static final String COLUMN_CHAT = "chat";


    private static final int DEFAULT_USER_ID = 1;

    private static final String USER_DATA_DATABASE_QUERY = String.format(Locale.ENGLISH, "select %1$s from %2$s where %3$s=%4$d", "*", TABLE_NAME, COLUMN_ID, DEFAULT_USER_ID);


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_CHAT + " integer," +
                        COLUMN_USER + " integer," +
                        COLUMN_NICKNAME + " text," +
                        COLUMN_INTERLOCUTOR + " integer," +
                        COLUMN_INTERLOCUTOR_NICKNAME + " text)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertUser(int userId, String nickname, int interlocutor, String interlocutorNickname,int chat) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER, userId);
        contentValues.put(COLUMN_NICKNAME, nickname);
        contentValues.put(COLUMN_INTERLOCUTOR, interlocutor);
        contentValues.put(COLUMN_INTERLOCUTOR_NICKNAME, interlocutorNickname);
        contentValues.put(COLUMN_CHAT, chat);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void updateUser(int userId, String nickname, int interlocutor, String interlocutorNickname, int chat) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER, userId);
        values.put(COLUMN_NICKNAME, nickname);
        values.put(COLUMN_INTERLOCUTOR, interlocutor);
        values.put(COLUMN_INTERLOCUTOR_NICKNAME, interlocutorNickname);
        values.put(COLUMN_CHAT, chat);

        sqLiteDatabase.update(TABLE_NAME, values, COLUMN_ID + "=" + DEFAULT_USER_ID, null);
    }

    public Cursor getDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery(USER_DATA_DATABASE_QUERY, null);
    }


    private int numberOfRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);
    }

    public boolean isEmpty() {
        return numberOfRows() <= 0;
    }

//    public boolean isUserIsLoggedIn() {
//        if (isEmpty()) {
//            return false;
//        }
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor userData = sqLiteDatabase.rawQuery(USER_DATA_DATABASE_QUERY, null);
//
//        userData.moveToFirst();
//        boolean isLogged = userData.getInt(userData.getColumnIndex(DatabaseHelper.COLUMN_SESSION)) == USER_IS_LOGGED;
//        if (!userData.isClosed()) {
//            userData.close();
//        }
//        return isLogged;
//    }
//
//    public void changeUserSession(int session) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_SESSION, session);
//
//        sqLiteDatabase.update(TABLE_NAME, values, COLUMN_ID + "=" + DEFAULT_USER_ID, null);
//    }
//
//
    public int getUserId() {
        if (isEmpty()) {
            return 0;
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor userData = sqLiteDatabase.rawQuery(USER_DATA_DATABASE_QUERY, null);
        userData.moveToFirst();
        int userId = userData.getInt(userData.getColumnIndex(DatabaseHelper.COLUMN_USER));
        if (!userData.isClosed()) {
            userData.close();
        }
        return userId;
    }

    public String getUserNickName() {
        if (isEmpty()) {
            return "";
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor userData = sqLiteDatabase.rawQuery(USER_DATA_DATABASE_QUERY, null);
        userData.moveToFirst();
        String userNickname = userData.getString(userData.getColumnIndex(DatabaseHelper.COLUMN_NICKNAME));
        if (!userData.isClosed()) {
            userData.close();
        }
        return userNickname;
    }

    public int getChatId() {
        if (isEmpty()) {
            return 0;
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor userData = sqLiteDatabase.rawQuery(USER_DATA_DATABASE_QUERY, null);
        userData.moveToFirst();
        int chatId = userData.getInt(userData.getColumnIndex(DatabaseHelper.COLUMN_CHAT));
        if (!userData.isClosed()) {
            userData.close();
        }
        return chatId;
    }

    public int getInterlocutorId() {
        if (isEmpty()) {
            return 0;
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor userData = sqLiteDatabase.rawQuery(USER_DATA_DATABASE_QUERY, null);
        userData.moveToFirst();
        int interlocutorId = userData.getInt(userData.getColumnIndex(DatabaseHelper.COLUMN_INTERLOCUTOR));
        if (!userData.isClosed()) {
            userData.close();
        }
        return interlocutorId;
    }

    public String getInterlocutorNickname() {
        if (isEmpty()) {
            return "";
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor userData = sqLiteDatabase.rawQuery(USER_DATA_DATABASE_QUERY, null);
        userData.moveToFirst();
        String interlocutorNickname = userData.getString(userData.getColumnIndex(DatabaseHelper.COLUMN_INTERLOCUTOR_NICKNAME));
        if (!userData.isClosed()) {
            userData.close();
        }
        return interlocutorNickname;
    }


}
