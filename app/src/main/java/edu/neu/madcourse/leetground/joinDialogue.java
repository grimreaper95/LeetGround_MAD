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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class joinDialogue extends AppCompatDialogFragment {
    private EditText verification_code;
    private RequestQueue requestQueue;
    private static final String TAG = "Join_Dialogue";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.join_dialogue, null);
        verification_code = view.findViewById(R.id.verification_code);
        builder.setView(view)
                .setTitle("Enter League code to join")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String code = verification_code.getText().toString();
                        PostJoin();

                    }
                });

        return builder.create();
    }

    public void PostJoin(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MadSharedPref", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("accessCode")
                .appendPath(verification_code.getText().toString())
                .appendPath("user")
                .appendPath(userId);

        String url = builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                response -> Toast.makeText(getActivity().getApplicationContext(),"Success",Toast.LENGTH_SHORT).show(),
                error -> Log.e(TAG,"onFailure"+error.toString()));
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}