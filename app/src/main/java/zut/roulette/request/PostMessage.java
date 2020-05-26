package zut.roulette.request;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;

import zut.roulette.database.DatabaseHelper;

public class PostMessage extends AsyncTask<Void, Void, Void> {

    private static final String USER_URL = "http://46.41.135.179/message";

    private String message;
    private DatabaseHelper databaseHelper;

    private HttpResponse<String> response;

    public PostMessage(String message, DatabaseHelper databaseHelper) {
        this.message = message;
        this.databaseHelper = databaseHelper;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        Unirest.setTimeouts(10000, 30000);
        try {
            response = Unirest.post(USER_URL)
                    .header("Content-Type", "application/json")
                    .body("{\n\t\"chatId\": "+databaseHelper.getChatId()+",\n\t\"sender\": "+databaseHelper.getUserId()+",\n\t\"receiver\": "+databaseHelper.getInterlocutorId()+",\n\t\"message\": \""+this.message+"\"\n}")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonResponse = new JSONObject();


        Log.i("ChatAPI", "POST MESSAGE FINISHED TO"+databaseHelper.getInterlocutorId()+" "+this.message);



        return null;
    }

}
