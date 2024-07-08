package com.example.myecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoanServices extends AppCompatActivity {

    private Button btnApplyLoan;
    private RecyclerView recyclerViewLoans;
    private LoanServicesAdapter loanAdapter;
    private List<LoanDisplay> loanList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_services);

        btnApplyLoan = findViewById(R.id.btnApplyLoan);
        recyclerViewLoans = findViewById(R.id.recyclerViewLoans);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loanList = new ArrayList<>();
        loanAdapter = new LoanServicesAdapter(loanList, this);
        recyclerViewLoans.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLoans.setAdapter(loanAdapter);

        btnApplyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent applyLoanIntent = new Intent(LoanServices.this, LoanApplication.class);
                startActivity(applyLoanIntent);
            }
        });

        fetchLoans();
    }

    private void fetchLoans() {
        String userEmail = mAuth.getCurrentUser().getEmail();
        db.collection("loans")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loanList.clear();
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot != null) {
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                LoanDisplay loan = document.toObject(LoanDisplay.class);
                                loanList.add(loan);
                                // Log the fetched loan details
                                Log.d("LoanServices", "Fetched Loan: " + loan.getAmount() + ", " + loan.getDueDate() + ", " + loan.getStatus());
                            }
                            loanAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(LoanServices.this, "Failed to fetch loans.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
