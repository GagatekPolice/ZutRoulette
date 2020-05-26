package zut.roulette.request;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;

import zut.roulette.database.DatabaseHelper;

public class DeleteUser extends AsyncTask<Void, Void, Void> {

    private static final String USER_URL = "http://46.41.135.179/user/";

    private DatabaseHelper databaseHelper;

    private HttpResponse<String> response;

    public DeleteUser(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Unirest.setTimeouts(10000, 30000);
        try {
            response = Unirest.delete(USER_URL + databaseHelper.getUserId())
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        JSONObject jsonResponse = new JSONObject();


        Log.i("ChatAPI", "DELETE USER FINISHED "+String.valueOf(jsonResponse));



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        databaseHelper.setInterlocutorId(0);
        databaseHelper.setInterlocutornickname("");
        databaseHelper.setChatId(0);
        databaseHelper.setUserId(0);
        databaseHelper.setUserNickname("");
        super.onPostExecute(aVoid);
    }
}
