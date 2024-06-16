package com.example.monthly_apha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {
    private List<SmsMessageModel> smsList;
    private Context context;

    public SmsAdapter(Context context, List<SmsMessageModel> smsList) {
        this.context = context;
        this.smsList = smsList;
    }

    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmsAdapter.ViewHolder holder, int position) {
        SmsMessageModel sms = smsList.get(position);
        holder.messageTextView.setText(sms.getMessage());
        holder.buttonDeleteMessage.setOnClickListener(v -> {
            deleteMessage(sms.getMsg_id(), position);
        });
    }

    public void removeMessage(int position) {
        smsList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public Button buttonDeleteMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            buttonDeleteMessage = itemView.findViewById(R.id.buttonDeleteMessage);
        }
    }

    public void deleteMessage(String messageId, int position) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        boolean isDeleted = dbHelper.deleteMessage(messageId);

        if(isDeleted) {
            removeMessage(position);
        } else {
            Toast.makeText(context, "Failed to delete message", Toast.LENGTH_SHORT).show();
        }
    }


}