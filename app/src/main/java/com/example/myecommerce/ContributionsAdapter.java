package com.example.myecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContributionsAdapter extends RecyclerView.Adapter<ContributionsAdapter.ContributionViewHolder> {

    private Context context;
    private List<Contribution> contributionList;
    private OnContributeClickListener listener;

    public interface OnContributeClickListener {
        void onContributeClick(Contribution contribution);
    }

    public ContributionsAdapter(Context context, List<Contribution> contributionList, OnContributeClickListener listener) {
        this.context = context;
        this.contributionList = contributionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContributionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contribution, parent, false);
        return new ContributionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributionViewHolder holder, int position) {
        Contribution contribution = contributionList.get(position);
        holder.tvContributionName.setText(contribution.getName());
        holder.tvContributionDescription.setText(contribution.getDescription());
        holder.tvPostedBy.setText("Posted by: " + contribution.getPostedBy());

        holder.btnContribute.setOnClickListener(v -> listener.onContributeClick(contribution));
    }

    @Override
    public int getItemCount() {
        return contributionList.size();
    }

    public class ContributionViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContributionName, tvContributionDescription, tvPostedBy;
        public Button btnContribute;

        public ContributionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContributionName = itemView.findViewById(R.id.tvContributionName);
            tvContributionDescription = itemView.findViewById(R.id.tvContributionDescription);
            tvPostedBy = itemView.findViewById(R.id.tvPostedBy);
            btnContribute = itemView.findViewById(R.id.btnContribute);
        }
    }
}
