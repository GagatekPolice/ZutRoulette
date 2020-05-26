package zut.roulette.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import zut.roulette.R;
import zut.roulette.model.Message;

public class RecyclerChat extends RecyclerView.Adapter<RecyclerChat.ViewHolder> {

    private Context context;
    private RecyclerChat.ViewHolder holder;

    private ArrayList<Message> messages;

    public RecyclerChat(Context context, ArrayList<Message> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerChat.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.messages_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerChat.ViewHolder holder, int position) {

        Message currentMessages = messages.get(position);
        this.holder = holder;
        holder.bindTo(currentMessages, context);
        if(messages.size()>1 && messages.get(messages.size()-1).getText().equals("Czat zakończył się.")){
            holder.tvMessage.setTextColor(Color.parseColor("#c70039"));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvMessage;
        private TextView tvAuthor;


        ViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.text);
            tvAuthor = itemView.findViewById(R.id.author);
            itemView.setOnClickListener(this);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        void bindTo(Message currentMessage, Context context) {

            tvAuthor.setText(currentMessage.getAuthor());
            tvMessage.setText(currentMessage.getText());
            if(currentMessage.getText().equals("Czat zakończył się.")){
                tvMessage.setTextColor(Color.parseColor("#c70039"));
            } else {
                tvMessage.setTextColor(Color.rgb(0,0,0));
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}