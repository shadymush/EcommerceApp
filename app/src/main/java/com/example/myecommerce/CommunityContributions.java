package com.example.myecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityContributions extends AppCompatActivity {

    private RecyclerView recyclerViewContributions;
    private Button btnPostContribution;
    private DatabaseReference contributionsRef;
    private List<Contribution> contributionList;
    private ContributionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_contributions);

        recyclerViewContributions = findViewById(R.id.recyclerViewContributions);
        btnPostContribution = findViewById(R.id.btnPostContribution);

        contributionsRef = FirebaseDatabase.getInstance().getReference("Contributions");

        recyclerViewContributions.setLayoutManager(new LinearLayoutManager(this));
        contributionList = new ArrayList<>();
        adapter = new ContributionsAdapter(this, contributionList, contribution -> {
            // Handle contribute button click
            showContributionDialog(contribution);
        });
        recyclerViewContributions.setAdapter(adapter);

        btnPostContribution.setOnClickListener(v -> showPostContributionDialog());

        loadContributions();
    }

    private void loadContributions() {
        contributionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contributionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Contribution contribution = snapshot.getValue(Contribution.class);
                    contributionList.add(contribution);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CommunityContributions.this, "Failed to load contributions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPostContributionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_post_contribution, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etContributionName = dialogView.findViewById(R.id.etContributionName);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etPaymentDetails = dialogView.findViewById(R.id.etPaymentDetails);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        AlertDialog dialog = builder.create();

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String contributionName = etContributionName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String paymentDetails = etPaymentDetails.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(contributionName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(paymentDetails)) {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = contributionsRef.push().getKey();
            Contribution contribution = new Contribution(id, contributionName, description, name, paymentDetails);

            contributionsRef.child(id).setValue(contribution)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Contribution posted successfully.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(this, "Failed to post contribution.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        dialog.show();
    }

    private void showContributionDialog(Contribution contribution) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_contribute, null);
        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etAmount = dialogView.findViewById(R.id.etAmount);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnPay = dialogView.findViewById(R.id.btnPay);

        AlertDialog dialog = builder.create();

        btnPay.setOnClickListener(v -> {
            String amount = etAmount.getText().toString().trim();
            if (TextUtils.isEmpty(amount)) {
                Toast.makeText(this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Implement payment logic here (e.g., using payment gateway API)
            // For now, we will just show a toast message
            Toast.makeText(this, "Payment of " + amount + " made to " + contribution.getPaymentDetails(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
