package com.example.myecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoanServicesAdapter extends RecyclerView.Adapter<LoanServicesAdapter.LoanViewHolder> {

    private List<LoanDisplay> loanList;
    private Context context;

    public LoanServicesAdapter(List<LoanDisplay> loanList, Context context) {
        this.loanList = loanList;
        this.context = context;
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loanservices, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        LoanDisplay loan = loanList.get(position);
        holder.tvLoanAmount.setText("Loan Amount: KSHS. " + loan.getAmount());
        holder.tvDueDate.setText("Due Date: " + loan.getDueDate());
        holder.tvLoanStatus.setText("Status: " + loan.getStatus());

        holder.btnRepayLoan.setOnClickListener(v -> {
            // Handle repayment
            Toast.makeText(context, "Repay clicked for loan: " + loan.getAmount(), Toast.LENGTH_SHORT).show();
        });

        holder.btnWithdrawLoan.setOnClickListener(v -> {
            // Handle withdraw
            Toast.makeText(context, "Withdraw clicked for loan: " + loan.getAmount(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {

        TextView tvLoanAmount, tvDueDate, tvLoanStatus;
        Button btnRepayLoan, btnWithdrawLoan;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoanAmount = itemView.findViewById(R.id.tvLoanAmount);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvLoanStatus = itemView.findViewById(R.id.tvLoanStatus);
            btnRepayLoan = itemView.findViewById(R.id.btnRepayLoan);
            btnWithdrawLoan = itemView.findViewById(R.id.btnWithdrawLoan);
        }
    }
}
