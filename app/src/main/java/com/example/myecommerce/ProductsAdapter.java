package com.example.myecommerce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private ArrayList<Items> productList;
    private Context context;

    public ProductsAdapter(Context context, ArrayList<Items> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        final Items items = productList.get(position);

        // Bind data to views
        holder.productName.setText(items.getProductName());
        holder.productPrice.setText("Kshs." + String.valueOf(items.getPrice()));

        holder.recCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkout = new Intent(context, Checkout.class);
                checkout.putExtra("productName", items.getProductName());
                checkout.putExtra("productPrice", items.getPrice());
                context.startActivity(checkout);

            }
        });
       // Glide.with(context).load(product.getImageResource()).into(holder.imageViewProduct);

        Glide.with(context)
                .asBitmap()
                .load(productList.get(position).getProductImage())
                .into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private CardView recCardView;

        private Context context;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            recCardView = itemView.findViewById(R.id.recCardView);
        }


    }
}

