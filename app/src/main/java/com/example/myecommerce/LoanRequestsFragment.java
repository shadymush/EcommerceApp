package com.example.myecommerce;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LoanRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LoanAdapter loanAdapter;
    private List<Loan> loanList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLoans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loanList = new ArrayList<>();
        loanAdapter = new LoanAdapter(getContext(), loanList);
        recyclerView.setAdapter(loanAdapter);

        db = FirebaseFirestore.getInstance();
        loadLoanRequests();

        return view;
    }

    private void loadLoanRequests() {
        db.collection("loans").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    loanList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Loan loan = document.toObject(Loan.class);
                        loan.setId(document.getId());
                        loanList.add(loan);
                    }
                    loanAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load loan requests.", Toast.LENGTH_SHORT).show();
                });
    }
}
