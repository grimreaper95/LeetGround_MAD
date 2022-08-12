package edu.neu.madcourse.leetground;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Shop_Confirm_Dialogue extends AppCompatDialogFragment {
    private String mprice;
    private TextView cur_price;
    private RequestQueue requestQueue;
    private Context context;
    private String userId;
    private static final String TAG = "Shop_Confirm_Dialogue";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MadSharedPref", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.shop_confirm, null);
        //verification_code = view.findViewById(R.id.verification_code);
        cur_price = view.findViewById(R.id.shop_text);
        //View view2 = inflater.inflate(R.layout.shop_item, null);
        //price = view2.findViewById(R.id.item_price);
        cur_price.setText("Are you sure to spend " +mprice+" coins buying this item?");
        builder.setView(view)
                .setTitle("Confirm to purchase")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PutBuy();
                    }
                });

        return builder.create();
    }
    public void getPrice(String price,Context mcontext){
        mprice = price;
        context = mcontext;
    }

    public void PutBuy(){
        String url = "http://mad-backend-sprinboot-server.herokuapp.com/user/" + userId + "/increment/-" + mprice;
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,url,
                response -> Toast.makeText(context, "Successfully purchased. It's on its way to you!", Toast.LENGTH_LONG).show(),
                        //Toast.makeText(,"Success",Toast.LENGTH_SHORT).show(),
                error -> Log.e(TAG,"onFailure"+error.toString()));
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}
