package zut.roulette.request;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import zut.roulette.database.DatabaseHelper;
import zut.roulette.model.Message;
import zut.roulette.view.RecyclerChat;

public class GetMessage extends AsyncTask<Void, Void, Void> {

    private static final String USER_URL = "http://46.41.135.179/message/";

    private DatabaseHelper databaseHelper;

    private HttpResponse<String> response;

    private ArrayList<Message> messages = new ArrayList<>();
    private RecyclerChat adapter;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;

    private boolean isMessageAdded = false;

    public GetMessage(DatabaseHelper databaseHelper, ArrayList<Message> messages, RecyclerChat adapter, RecyclerView recyclerView) {
        this.databaseHelper = databaseHelper;
        this.adapter = adapter;
        this.messages = messages;
        this.recyclerView = recyclerView;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Unirest.setTimeouts(10000, 30000);

        try {
            response = Unirest.get(USER_URL+databaseHelper.getUserId())
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = new JSONObject();

        try {
            jsonResponse = new JSONObject(response.getBody());
        }catch (JSONException err){
            Log.e("Error", "Message not found "+err);
        }

        Log.i("ChatAPI", "GET MESSAGE FINISHED "+String.valueOf(jsonResponse));

        if(jsonResponse.has("message")) {
            try {
                isMessageAdded = true;
                if(jsonResponse.getString("message").equals("Czat zakończył się.")){
                    messages.add(new Message("INFO",jsonResponse.getString("message")));
                } else {
                    messages.add(new Message(databaseHelper.getInterlocutorNickname(),jsonResponse.getString("message")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter.notifyDataSetChanged();
        if(isMessageAdded && messages.size()>1) {
            recyclerView.smoothScrollToPosition(messages.size() - 1);
        }
        super.onPostExecute(aVoid);
    }
}
