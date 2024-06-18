package com.example.myecommerce;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private List<Loan> loanList;
    private FirebaseFirestore db;

    public LoanAdapter(List<Loan> loanList) {
        this.loanList = loanList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loanList.get(position);
        holder.loanEmail.setText("Email: " + loan.getEmail());
        holder.loanAmount.setText("Amount: $" + loan.getAmount());
        holder.loanPurpose.setText("Purpose: " + loan.getLoan_purpose());

        holder.btnApprove.setOnClickListener(v -> updateLoanStatus(loan, "Approved"));
        holder.btnReject.setOnClickListener(v -> updateLoanStatus(loan, "Rejected"));
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    private void updateLoanStatus(Loan loan, String status) {
        db.collection("loans").document(loan.getId())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(db.getApp().getApplicationContext(), "Loan " + status, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(db.getApp().getApplicationContext(), "Failed to update loan status", Toast.LENGTH_SHORT).show();
                });
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {

        TextView loanEmail, loanAmount, loanPurpose;
        Button btnApprove, btnReject;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            loanEmail = itemView.findViewById(R.id.loanEmail);
            loanAmount = itemView.findViewById(R.id.loanAmount);
            loanPurpose = itemView.findViewById(R.id.loanPurpose);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
