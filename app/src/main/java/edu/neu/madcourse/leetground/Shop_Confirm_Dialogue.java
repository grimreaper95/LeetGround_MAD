package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;

public class Shop_Confirm_Dialogue extends AppCompatDialogFragment {
    private String mprice;
    private TextView cur_price;
    private RequestQueue requestQueue;
    private Context context;
    private String userId;
    private Integer coins;
    private String email;
    private String mtitle;
    private TextInputEditText address;
    private static final String TAG = "Shop_Confirm_Dialogue";
    private SharedPreferences sharedPreferences;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences("MadSharedPref", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        coins = sharedPreferences.getInt("coins",0);
        email = sharedPreferences.getString("email","");
        //address = sharedPreferences.getString("","")
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.shop_confirm, null);
        cur_price = view.findViewById(R.id.shop_text);

        View view2 = inflater.inflate(R.layout.activity_user_profile, null);
        address = view2.findViewById(R.id.user_address);

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
                        if(coins >= Integer.valueOf(mprice)) {
                            PutBuy();
                        }else{
                            Toast.makeText(context, "Sorry, you don't have enough coins to buy this item.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return builder.create();
    }
    public void getPrice(String price,Context mcontext,String title){
        mprice = price;
        context = mcontext;
        mtitle = title;
    }

    public void PutBuy(){
        String url = "https://mad-backend-sprinboot-server.herokuapp.com/user/" + userId + "/increment/-" + mprice;
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,url,response -> {
            Toast.makeText(context, "Successfully purchased. It's on its way to you!", Toast.LENGTH_LONG).show();
            sendshopEmail();
            SharedPreferences.Editor spEdit = sharedPreferences.edit();
            Integer res = coins-Integer.valueOf(mprice);
            spEdit.putInt("coins", res);
            spEdit.commit();
        },error -> {
            Log.e(TAG,"onFailure"+error.toString());
        });

                /*response -> ,
                        //Toast.makeText(,"Success",Toast.LENGTH_SHORT).show(),
                error -> );

                 */
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public void sendshopEmail(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("email");

        String url = builder.build().toString();
        Log.d("send shop email start", url);

        String subject = "LeetGround Item purchase Confirmation";
        String body = "Thank you for your purchase. Your order for "+mtitle+"is on its way to the address: "+address.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("senderEmail", email);
        params.put("subject", subject);
        params.put("body", body);

        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"send email successfully");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        });
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request_json);


    }

}
