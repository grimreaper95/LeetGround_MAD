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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class createDialogue extends AppCompatDialogFragment {
    private EditText editLeague_name;
    private RequestQueue requestQueue;
    private static final String TAG = "Create_Dialogue";
    private DialogueListener dialogueListener;
    //private DialogueListener dialogueListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_dialogue,null);
        editLeague_name = view.findViewById(R.id.edit_leaguename);
        builder.setView(view)
                .setTitle("Create A League")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String League_name = editLeague_name.getText().toString();
                        PostCreate();
                    }
                });

        return builder.create();
    }

    public void PostCreate(){
//        String url = "http://mad-backend-sprinboot-server.herokuapp.com/league";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("league");;

        String url = builder.build().toString();
        Log.d("shashankCreateDialogue", url);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MadSharedPref", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        Log.d("shashankuserId", userId);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("leagueName", editLeague_name.getText().toString());
        params.put("userId", userId);

        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            dialogueListener
                                    .notify(editLeague_name.getText().toString(),
                                            response.getInt("id")
                                ,1);
                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        });
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request_json);
    }

    public interface DialogueListener{
        void notify(String leagueName, int leagueId, int userId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogueListener = (DialogueListener)context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement DialogueListener");
        }

    }


}