package zut.roulette.request;

import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import zut.roulette.database.DatabaseHelper;

public class GetUserChat extends AsyncTask<Void, Void, Void> {

    private static final String USER_URL = "http://46.41.135.179/chat/";

    private DatabaseHelper databaseHelper;

    private HttpResponse<String> response;

    public GetUserChat(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;

    }


    @Override
    protected Void doInBackground(Void... voids) {
        Unirest.setTimeouts(10000, 30000);


        try {
            response = Unirest.get(USER_URL + String.valueOf(databaseHelper.getUserId()))
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = new JSONObject();

        try {
            jsonResponse = new JSONObject(response.getBody());
        }catch (JSONException err){
            Log.e("Error", "Chat not found "+err);
        }

        if(jsonResponse.has("chatId")) {
            Log.i("ChatAPI"," GET USER CHAT REQUEST " + String.valueOf(jsonResponse) );

            try {
                databaseHelper.setChatId(Integer.parseInt(jsonResponse.getString("chatId")));
                databaseHelper.setInterlocutorId(Integer.parseInt(jsonResponse.getString("userIdCreated")));
                databaseHelper.setInterlocutornickname(jsonResponse.getString("stangerNickname"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("ChatAPI"," DATABASE " + databaseHelper.getDatabase());
            Log.i("ChatAPI"," DATABASE USER ID " + databaseHelper.getUserId());
            Log.i("ChatAPI"," DATABASE USER NICKNAME " + databaseHelper.getUserNickName());
            Log.i("ChatAPI"," DATABASE CHAT ID " + databaseHelper.getChatId());
            Log.i("ChatAPI"," DATABASE INTERLOCUTOR ID " + databaseHelper.getInterlocutorId());
            Log.i("ChatAPI"," DATABASE INTERLOCUTOR NICKNAME " + databaseHelper.getInterlocutorNickname());
        }



        return null;
    }


}
