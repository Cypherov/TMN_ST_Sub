package com.example.monthly_apha;

import static java.security.AccessController.getContext;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportsViewHolder> {
    private List<reports> reportsList;
    private DatabaseReference mDatabase;

    public ReportAdapter(List<reports> reportsList) {

        this.reportsList = reportsList;
        mDatabase = fb.getCustomReferenace("Reports");
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        reports report = reportsList.get(position);
        holder.tvTitle.setText("Title: "+report.getTitle());
        holder.tvDetails.setText("Details: "+ report.getDetails());
        holder.tvTopic.setText("Type: "+report.getTopic());
        holder.tvUid.setText("User Id: " + report.getUID());
        holder.tvEmail.setText("Email: " + report.getEmail());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReport(report);
                Toast.makeText(v.getContext(), "Report Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        //holder.btnDelete.setOnClickListener(v -> deleteReport(report));
    }
    private void deleteReport(reports report) {
        fb.instance.getReportRef().child(report.getKey()).removeValue();
    }
    public void updateData(List<reports> newReportsList) {
        this.reportsList = newReportsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDetails, tvTopic, tvUid, tvEmail;
        Button btnDelete;

        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvreportTitle);
            tvDetails = itemView.findViewById(R.id.tvreportDescription);
            tvTopic = itemView.findViewById(R.id.tvreportTopic);
            tvUid = itemView.findViewById(R.id.tvreportUID);
            tvEmail = itemView.findViewById(R.id.tvreportEmail);
            btnDelete = itemView.findViewById(R.id.btndelete);
        }
    }
}