package zut.roulette.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import zut.roulette.R;
import zut.roulette.model.Message;
import zut.roulette.view.RecyclerChat;


public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_messages);


        ArrayList<Message> mNewsData = new ArrayList<>();

        mNewsData.add(new Message("Rudy","siemka"));
        mNewsData.add(new Message("Doman","siemka chuju"));

        RecyclerChat adapter = new RecyclerChat(getActivity(), mNewsData);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter( adapter );

        Collections.reverse(mNewsData);
        adapter.notifyDataSetChanged();

        super.onViewCreated(view, savedInstanceState);
    }
}
