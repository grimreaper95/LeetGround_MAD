package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView tvSignup;
    private EditText etUserName, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                List<String> pathSegments = data.getPathSegments();
                Toast.makeText(this,"Access code: " + pathSegments.get(1),
                        Toast.LENGTH_LONG).show();
            }
        }

        etUserName = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        tvSignup = (TextView) findViewById(R.id.signup);
        btnLogin = (Button) findViewById(R.id.login);

        //Creating sharedPreferences file to save user settings across the app
        SharedPreferences sharedPreferences = getSharedPreferences("MadSharedPref", MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor spEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext

//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("https")
//                .authority(SERVER_URL)
//                .appendPath("authenticate");
        //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        //finish();
        String url = "https://mad-backend-sprinboot-server.herokuapp.com/authenticate";
        Log.d("shashank", url);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonBody = new JSONObject();


                try {
                    jsonBody.put("username", "Jacob");
                    jsonBody.put("password", "Jacob");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String mRequestBody = jsonBody.toString();

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

                                try{
                                     String jwtToken = response.getString("jwt");
                                     String userName = response.getJSONObject("maduser").getString("name");
                                     String userId = response.getJSONObject("maduser").getString("id");
                                     String name = response.getJSONObject("maduser").getString("username");
                                     String email = response.getJSONObject("maduser").getString("email");
                                     String password = response.getJSONObject("maduser").getString("password");
                                     String isReminderOn = response.getJSONObject("maduser").getString("isReminderOn");
                                     String coins = response.getJSONObject("maduser").getString("coins");

                                    spEdit.putString("jwtToken", jwtToken);
                                    spEdit.putString("userId", userId);
                                    spEdit.putString("userName", userName);
                                    spEdit.putString("name", name);
                                    spEdit.putString("email", email);
                                    spEdit.putString("password", password);
                                    spEdit.putBoolean("isReminderOn",
                                            Boolean.parseBoolean(isReminderOn));
                                    spEdit.putInt("coins",
                                            Integer.parseInt(coins));
                                    spEdit.apply();
                                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                                    finish();
//                                     new UserProfile(jwtToken, name, userName, password,
//                                             email, Boolean.parseBoolean(isReminderOn),
//                                             Integer.parseInt(coins));

                                }catch (JSONException e){
                                    Toast.makeText(getApplicationContext(), "Invalid username or password. Please try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                // Do something when error occurred
                                Toast.makeText(getApplicationContext(), "Invalid username or password. Please try again!", Toast.LENGTH_LONG).show();
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
                SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}