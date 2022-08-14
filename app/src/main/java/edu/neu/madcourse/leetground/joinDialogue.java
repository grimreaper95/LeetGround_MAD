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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class joinDialogue extends AppCompatDialogFragment {
    private EditText verification_code;
    private RequestQueue requestQueue;
    private joinDialogue.DialogueListener dialogueListener;
    private static final String TAG = "Join_Dialogue";
    private Context context;

    private String leagueName;
    private String leagueId;
    private int totalUsersInLeague;

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

    public void setContext(Context mcontext){
        context = mcontext;
    }

    public void PostJoin(){


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MadSharedPref", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        String accessCode = verification_code.getText().toString();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("accessCode", accessCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonBody.toString();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("user")
                .appendPath(userId)
                .appendPath("league");

        String url = builder.build().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        //mTextView.setText(response.toString());
                        Log.d("shashank", response.toString());
                        try {
                            leagueName = response.getJSONObject("leagueDetails").getString("leagueName");
                            leagueId = response.getJSONObject("leagueDetails").getString("id");
                            getTotalUsersAndUpdateRecyclerView();
                        }catch (JSONException e){
                            Toast.makeText(context, "Invalid username or password. Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(context, "Failed to join league! Please try again.", Toast.LENGTH_LONG).show();
                        if (error.getMessage()!= null)
                            Log.d("shashank", error.getMessage());
                    }
                }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        SingletonVolley.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void getTotalUsersAndUpdateRecyclerView() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("league")
                .appendPath(leagueId)
                .appendPath("users");

        String url = builder.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());
                        Log.d("shashank", response.toString());
                        totalUsersInLeague = response.length();
                        dialogueListener.notify(leagueName,
                                Integer.parseInt(leagueId), totalUsersInLeague);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("shashank", error.toString());
                    }
                }
        );
        SingletonVolley.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }


    public interface DialogueListener{
        void notify(String leagueName, int leagueId, int userId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogueListener = (joinDialogue.DialogueListener)context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement DialogueListener");
        }

    }
}