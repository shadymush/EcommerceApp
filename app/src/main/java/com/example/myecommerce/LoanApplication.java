package com.example.myecommerce;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoanApplication extends AppCompatActivity {

    private EditText emailaddress, phonenumber, idnumber, krapin, accno, guarantorcontact, purpose, loanAmount, loanDue;
    private Button btnSubmit;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_applications);

        // Initialization of views
        emailaddress = findViewById(R.id.emailaddress);
        phonenumber = findViewById(R.id.phonenumber);
        idnumber = findViewById(R.id.idnumber);
        krapin = findViewById(R.id.krapin);
        accno = findViewById(R.id.accno);
        guarantorcontact = findViewById(R.id.guarantorcontact);
        purpose = findViewById(R.id.purpose);
        loanAmount = findViewById(R.id.loanAmount);
        loanDue = findViewById(R.id.loanDue);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailaddress.getText().toString().trim();
                String phone = phonenumber.getText().toString().trim();
                String id = idnumber.getText().toString().trim();
                String kra = krapin.getText().toString().trim();
                String accountno = accno.getText().toString().trim();
                String guarantor_contact = guarantorcontact.getText().toString().trim();
                String loan_purpose = purpose.getText().toString().trim();
                double amount = Double.parseDouble(loanAmount.getText().toString().trim());
                String dueDate = loanDue.getText().toString().trim();

                Map<String, Object> loanDetails = new HashMap<>();
                loanDetails.put("email", email);
                loanDetails.put("phone", phone);
                loanDetails.put("id", id);
                loanDetails.put("kra", kra);
                loanDetails.put("accountno", accountno);
                loanDetails.put("guarantor_contact", guarantor_contact);
                loanDetails.put("loan_purpose", loan_purpose);
                loanDetails.put("amount", amount);
                loanDetails.put("dueDate", dueDate);

                db.collection("loans").add(loanDetails)
                        .addOnSuccessListener(documentReference -> {
                            showSubmissionSnackbar(v, documentReference.getId());
                        })
                        .addOnFailureListener(e -> {
                            Snackbar.make(v, "Failed to submit loan details.", Snackbar.LENGTH_LONG).show();
                        });
            }
        });
    }

    private void showSubmissionSnackbar(View view, String documentId) {
        Snackbar snackbar = Snackbar.make(view, "Submitted successfully. View Receipt?", Snackbar.LENGTH_INDEFINITE)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showReceiptDialog(documentId);
                    }
                });
        snackbar.show();
    }

    private void showReceiptDialog(String documentId) {
        db.collection("loans").document(documentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");
                        String accountno = documentSnapshot.getString("accountno");
                        double amount = documentSnapshot.getDouble("amount");
                        String dueDate = documentSnapshot.getString("dueDate");

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        LayoutInflater inflater = this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.receipt_dialog, null);
                        builder.setView(dialogView);

                        AlertDialog dialog = builder.create();

                        TextView receiptEmail = dialogView.findViewById(R.id.receiptEmailaddress);
                        TextView receiptPhone = dialogView.findViewById(R.id.receiptNumber);
                        TextView receiptAccountNo = dialogView.findViewById(R.id.receiptAccno);
                        TextView receiptAmount = dialogView.findViewById(R.id.receiptAmount);
                        //this is the line that has a null
                        TextView receiptDueDate = dialogView.findViewById(R.id.receiptDueDate);

                        receiptEmail.setText("Email: " + email);
                        receiptPhone.setText("Phone: " + phone);
                        receiptAccountNo.setText("Account No: " + accountno);
                        receiptAmount.setText("Amount: Kshs. " + amount);
                        receiptDueDate.setText("Repay Date: " + dueDate);

                        dialogView.findViewById(R.id.close).setOnClickListener(v -> dialog.dismiss());

                        dialog.show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LoanApplication", "Error fetching loan receipt", e);
                });
    }
}
