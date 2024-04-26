package com.example.myecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity {
    private EditText searchBar;
    private RecyclerView recyclerViewProducts;
    private ArrayList<Items> products;
    private ArrayList<Items> filteredList;
    private ProductsAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        searchBar = findViewById(R.id.searchBar);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        // Initialize the lists
        products = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Add products to the list
        products.add(new Items("Fridge", "https://cdn.pixabay.com/photo/2016/10/24/21/03/appliance-1767311_640.jpg", 12000.00));
        products.add(new Items("Smart Phone", "https://img.freepik.com/free-vector/realistic-display-smartphone-with-different-apps_52683-30241.jpg", 12225.00));
        products.add(new Items("Water Dispenser", "https://c8.alamy.com/comp/HAY0F5/white-water-cooler-HAY0F5.jpg", 6450.00));
        products.add(new Items("Calculator", "https://upload.wikimedia.org/wikipedia/commons/c/cf/Casio_calculator_JS-20WK_in_201901_002.jpg", 1230.00));
        products.add(new Items("Comfy Chairs", "https://www.shutterstock.com/image-vector/vector-realistic-yellow-armchair-3d-600nw-2239751635.jpg", 3140.20));
        products.add(new Items("Smart Watch", "https://img.freepik.com/free-vector/realistic-fitness-trackers_23-2148530529.jpg", 4340.50));

        // Initially, display all products in the RecyclerView
        filteredList.addAll(products);

        // Set up RecyclerView with GridLayoutManager
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize adapter with the filtered list of products
        adapter = new ProductsAdapter(this, filteredList);
        recyclerViewProducts.setAdapter(adapter);

        // Add TextChangedListener to the search bar EditText
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the list based on the search query
                filterProducts(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }

    // Method to filter the product list based on search query
    private void filterProducts(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            // If the search query is empty, display all products
            filteredList.addAll(products);
        } else {
            // Filter products that contain the search query (case-insensitive)
            for (Items item : products) {
                if (item.getProductName().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
        }

        // Notify adapter of changes in the filtered list
        adapter.notifyDataSetChanged();
    }
}
