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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private Context context;
    private List<Loan> loanList;
    private FirebaseFirestore db;

    public LoanAdapter(Context context, List<Loan> loanList) {
        this.context = context;
        this.loanList = loanList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loansadmin, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loanList.get(position);
        holder.loanAmount.setText("Amount: " + loan.getAmount());
        holder.loanStatus.setText("Status: " + loan.getStatus());

        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveLoan(loan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    private void approveLoan(Loan loan) {
        loan.setStatus("Approved");
        db.collection("loans").document(loan.getId()).set(loan)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Loan Approved", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                        // Update user's loan balance in the database
                        updateUserLoanBalance(loan);

                        // Send notification to the user
                        String userToken = loan.getId();
                        String title = "Loan Approved";
                        String message = "Your loan of " + loan.getAmount() + " has been approved.";
                        FCMService.sendNotification(userToken, title, message);
                    }
                });
    }

    private void updateUserLoanBalance(Loan loan) {
        // Logic to update the user's loan balance in the database
        // This should be implemented based on your specific database structure
    }

    static class LoanViewHolder extends RecyclerView.ViewHolder {

        TextView loanAmount, loanStatus;
        Button btnApprove;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            loanAmount = itemView.findViewById(R.id.loanAmount);
            loanStatus = itemView.findViewById(R.id.loanStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
        }
    }
}
