package zut.roulette.request;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import zut.roulette.database.DatabaseHelper;

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

        Log.i("ChatAPI"," POST USER REQUEST " + response);

        try {
            jsonResponse = new JSONObject(response.getBody());
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
        Log.i("ChatAPI"," POST USER REQUEST " + String.valueOf(jsonResponse));

        if(jsonResponse.has("interlocutor")) {
            if (databaseHelper.isEmpty()) {
                try {
                    databaseHelper.insertUser( Integer.parseInt(jsonResponse.getString("id")), this.login, Integer.parseInt(jsonResponse.getString("interlocutor")), jsonResponse.getString("interlocutorNickname"), Integer.parseInt(jsonResponse.getString("chatId")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    databaseHelper.updateUser( Integer.parseInt(jsonResponse.getString("id")), this.login, Integer.parseInt(jsonResponse.getString("interlocutor")), jsonResponse.getString("interlocutorNickname"), Integer.parseInt(jsonResponse.getString("chatId")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if(jsonResponse.has("id")) {
            if (databaseHelper.isEmpty()) {
                try {
                    databaseHelper.insertUser( Integer.parseInt(jsonResponse.getString("id")), this.login,0,"",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    databaseHelper.updateUser( Integer.parseInt(jsonResponse.getString("id")), this.login,0,"",0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("ChatAPI"," DATABASE " + databaseHelper.getDatabase());
        Log.i("ChatAPI"," DATABASE USER ID " + databaseHelper.getUserId());
        Log.i("ChatAPI"," DATABASE USER NICKNAME " + databaseHelper.getUserNickName());
        Log.i("ChatAPI"," DATABASE CHAT ID " + databaseHelper.getChatId());
        Log.i("ChatAPI"," DATABASE INTERLOCUTOR ID " + databaseHelper.getInterlocutorId());
        Log.i("ChatAPI"," DATABASE INTERLOCUTOR NICKNAME " + databaseHelper.getInterlocutorNickname());

        Log.i("ChatAPI", "POST USER FINISHED ");

        return null;
    }


}
