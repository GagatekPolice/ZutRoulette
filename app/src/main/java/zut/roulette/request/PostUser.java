package zut.roulette.request;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import loginflow.app.database.DatabaseHelper;

public class PostUser extends AsyncTask<Void, Void, Void> {

    private static final String USER_URL = "http://46.41.135.179/user";

    private String login;
    private DatabaseHelper databaseHelper;

    private HttpResponse<String> response;

    public PostUser(String login, DatabaseHelper databaseHelper) {
        this.login = login;
        this.databaseHelper = databaseHelper;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        Unirest.setTimeouts(10000, 30000);
        try {
            response = Unirest.post(USER_URL)
                    .header("Content-Type", "application/json")
                    .body("{\n\t\"nickname\": \""+ this.login + "\"\n}")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = new JSONObject();

        try {
            jsonResponse = new JSONObject(response.getBody());
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
        Log.e("test", String.valueOf(jsonResponse));

        if(jsonResponse.has("interlocutor")) {

        } else if(jsonResponse.has("id")) {

        }

        return null;
    }
}
