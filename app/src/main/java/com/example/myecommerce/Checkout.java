package com.example.myecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Checkout extends AppCompatActivity {

    private EditText name, address, phoneNumber;
    private Button btnCheckout, btnProducts, btnReceipt;
    private FloatingActionButton close;
    private TextView itemName, quantity, unitPrice, totalPrice;
    private TextView receiptName, receiptAddress, receiptNumber, receiptItem, receiptPrice;
    private Dialog checkoutDialog, receiptDialog;
    private ConstraintLayout checkoutLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkoutLayout = findViewById(R.id.checkoutLayout);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);
        btnCheckout = findViewById(R.id.btnCheckout);
        itemName = findViewById(R.id.itemName);
       //quantity = findViewById(R.id.quantity);
        unitPrice = findViewById(R.id.unitPrice);
        //totalPrice = findViewById(R.id.totalPrice);

        //create a new dialog with respect to activity it will be displayed on
        checkoutDialog = new Dialog(Checkout.this);
        checkoutDialog.setContentView(R.layout.checkout_dialog);
        //set layout parameters for the dialog button
        checkoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //set the background drawable to be sused for the dialog
        checkoutDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.checkout_dialog_bg));

        //used to ensure whether user clicks anywhere outside the dialog it disapppears or not
        checkoutDialog.setCancelable(false);


        //define receipt dialog elements
        receiptDialog = new Dialog(Checkout.this);
        receiptDialog.setContentView(R.layout.receipt_dialog);
        //set layout parameters
        receiptDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //set bg
        receiptDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.checkout_dialog_bg));
        //declaration of receipt dialog
        close = receiptDialog.findViewById(R.id.close);
        receiptName = receiptDialog.findViewById(R.id.receiptName);
        receiptAddress = receiptDialog.findViewById(R.id.receiptAddress);
        receiptNumber = receiptDialog.findViewById(R.id.receiptNumber);
        receiptItem = receiptDialog.findViewById(R.id.receiptItem);
        receiptPrice = receiptDialog.findViewById(R.id.receiptPrice);

        //set text for various text views used in the receipt dialog box




        //declaration of checkoutDialog
        btnProducts = checkoutDialog.findViewById(R.id.btnProducts);
        btnReceipt = checkoutDialog.findViewById(R.id.btnReceipt);


        //obtain string values for respective input fields
//        String userName = name.getText().toString();
//        String userAddress = address.getText().toString();
//        String userPhone = phoneNumber.getText().toString();
//        String number = quantity.getText().toString();

        //get productName and productPrice from intent used in the productAdapter java file
        Intent getIntent = getIntent();
        if (getIntent != null) {
            String productName = getIntent.getStringExtra("productName");
            double productPrice = getIntent.getDoubleExtra("productPrice", 0.0);

            itemName.setText(productName);
            unitPrice.setText("Kshs. " + String.format("%.2f", productPrice));
        }
        receiptItem.setText(itemName.getText().toString());
        receiptPrice.setText(unitPrice.getText().toString());

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptName.setText(name.getText().toString());
                receiptAddress.setText(address.getText().toString());
                receiptNumber.setText(phoneNumber.getText().toString());
                checkoutDialog.show();
            }
        });

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productsIntent = new Intent(Checkout.this, Products.class);
                Toast.makeText(Checkout.this, "Redirecting back to products available. Please wait.....", Toast.LENGTH_SHORT).show();
                checkoutDialog.dismiss();
                startActivity(productsIntent);
                finish();
            }
        });

        btnReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptDialog.show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiptDialog.dismiss();
            }
        });






    }


}