package zut.roulette.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zut.roulette.R;
import zut.roulette.database.DatabaseHelper;
import zut.roulette.model.Message;
import zut.roulette.request.DeleteChat;
import zut.roulette.request.GetMessage;
import zut.roulette.request.GetUserChat;
import zut.roulette.request.PostMessage;
import zut.roulette.view.RecyclerChat;


public class ChatFragment extends Fragment implements View.OnClickListener {

    private final static String MESSAGE_PATTERN = ".{1,256}";
    private final static Pattern messagePattern = Pattern.compile(MESSAGE_PATTERN);

    private EditText etMessage;
    private TextView tvStranger;
    private Button btnSend;
    private Button btnExit;

    private ArrayList<Message> messages = new ArrayList<>();
    private RecyclerChat adapter;

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;

    private ProgressBar prbFindChat;

    private Handler getMessageHandler = new Handler();
    private Runnable getMessageRunner = new Runnable() {
        @Override
        public void run() {
            getMessageHandler.postDelayed(getMessageRunner, 500);

            new GetMessage(databaseHelper,messages,adapter,recyclerView).execute();
        }
    };

    private Handler getChatHandler = new Handler();
    private Runnable findChateRunner = new Runnable() {
        @Override
        public void run() {
            getChatHandler.postDelayed(getMessageRunner, 500);
            if (databaseHelper.getChatId() != 0) {
                Log.i("ChatAPI", "FIND USER CHAT ");
                prbFindChat.setVisibility(View.INVISIBLE);

                messages.clear();
                adapter.notifyDataSetChanged();

                getMessageRunner.run();
                getChatHandler.removeCallbacks(findChateRunner);

            } else {
                Log.i("ChatAPI", "RETRY find chat ");
                new GetUserChat(databaseHelper).execute();
                prbFindChat.setVisibility(View.INVISIBLE);
            }
        }
    };

    public ChatFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        btnExit = view.findViewById(R.id.chat_fragment_btn_next);
        btnSend = view.findViewById(R.id.chat_fragment_btn_send);
        etMessage = view.findViewById(R.id.chat_fragment_et_text);
        tvStranger = view.findViewById(R.id.chat_fragment_tv_stranger);
        prbFindChat = view.findViewById(R.id.chat_fragment_progressBar);

        btnExit.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getContext());

        recyclerView = view.findViewById(R.id.recyclerview_messages);

        adapter = new RecyclerChat(getActivity(), messages);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter( adapter );


        adapter.notifyDataSetChanged();

        getMessageRunner.run();

        tvStranger.setText( getResources().getString(R.string.talk_with) + " " + databaseHelper.getInterlocutorNickname());

        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_fragment_btn_send:
                Matcher matcher = ChatFragment.messagePattern.matcher(etMessage.getText());
                if (!matcher.matches()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.text_to_short), Toast.LENGTH_LONG).show();
                    break;
                }
                new PostMessage(String.valueOf(etMessage.getText()),databaseHelper).execute();

                messages.add(new Message(databaseHelper.getUserNickName(),String.valueOf(etMessage.getText())));
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messages.size() - 1);


                etMessage.setText("");
                break;
            case R.id.chat_fragment_btn_next:
                new PostMessage(getResources().getString(R.string.stranger_disconected),databaseHelper).execute();
                new DeleteChat(getResources().getString(R.string.stranger_disconected),databaseHelper).execute();

                messages.add(new Message("INFO",getResources().getString(R.string.stranger_disconected)));

                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messages.size() - 1);

                findChateRunner.run();
                getMessageHandler.removeCallbacks(getMessageRunner);
                prbFindChat.setVisibility(View.VISIBLE);

                break;
        }


    }
}
