package zut.roulette.request;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;

import zut.roulette.database.DatabaseHelper;

public class DeleteChat extends AsyncTask<Void, Void, Void> {

    private static final String USER_URL = "http://46.41.135.179/chat/";

    private DatabaseHelper databaseHelper;

    private HttpResponse<String> response;

    public DeleteChat(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        Unirest.setTimeouts(10000, 30000);
        try {
            response = Unirest.delete(USER_URL + databaseHelper.getChatId())
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonResponse = new JSONObject();


        Log.i("ChatAPI", "DELETE CHAT FINISHED "+String.valueOf(jsonResponse));



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        databaseHelper.setInterlocutorId(0);
        databaseHelper.setInterlocutornickname("");
        databaseHelper.setChatId(0);
        super.onPostExecute(aVoid);
    }
}
