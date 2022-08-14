package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView tvSignup;
    private EditText etUserName, etPassword;
    private Button btnLogin;
    private  SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor spEdit;

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

        etUserName = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        tvSignup = findViewById(R.id.signup);
        btnLogin = findViewById(R.id.login);


        sharedPreferences = getSharedPreferences("MadSharedPref", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("loggedIn", false)) {
            incrementCoins(false);
            startActivity(new Intent(this, LeaguesActivity.class));
            finish();
        }

        spEdit = sharedPreferences.edit();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("authenticate");

        String url = builder.build().toString();
        Log.d("shashank", url);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", etUserName.getText().toString());
                    jsonBody.put("password", etPassword.getText().toString());
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
                                     String name = response.getJSONObject("maduser").getString("name");
                                     String userId = response.getJSONObject("maduser").getString("id");
                                     String userName = response.getJSONObject("maduser").getString("username");
                                     String email = response.getJSONObject("maduser").getString("email");
                                     String password = response.getJSONObject("maduser").getString("password");
                                     String isReminderOn = response.getJSONObject("maduser").getString("isReminderOn");
                                     String coins = response.getJSONObject("maduser").getString("coins");
                                     String billingAddress=response.getJSONObject("maduser").getString("billingAddress");
                                     String profilePicImgEncoded=response.getJSONObject("maduser").getString("profilePic");

                                    spEdit.putString("jwtToken", jwtToken);
                                    spEdit.putString("userId", userId);
                                    spEdit.putString("userName", userName);
                                    spEdit.putString("name", name);
                                    spEdit.putString("billingAddress",billingAddress);
                                    spEdit.putString("email", email);
                                    spEdit.putString("password", password);
                                    spEdit.putBoolean("isReminderOn","1".equals(isReminderOn)?true:false);
                                    spEdit.putInt("coins", Integer.parseInt(coins));
                                    spEdit.putBoolean("loggedIn", true);
                                    spEdit.putString("profilePicImgByteArrStr",profilePicImgEncoded);
                                    spEdit.apply();
                                    incrementCoins(true);

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

    private void incrementCoins(boolean goToUserProfile) {
            String userId = sharedPreferences.getString("userId", "");
            String url = "https://mad-backend-sprinboot-server.herokuapp.com/user/" + userId + "/increment/1";
            StringRequest stringRequest = new StringRequest(Request.Method.PUT,url,
                    response -> {
                Toast.makeText(this, "Congratulations, you earned 1 coin for daily sign in bonus!", Toast.LENGTH_LONG).show();
                spEdit.putInt("coins", sharedPreferences.getInt("coins", 0) + 1);
                spEdit.commit();
                if(goToUserProfile) {
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    finish();
                }
            },error -> {
                Log.e("shashank","onFailure"+error.toString());
            });
            SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void goRegister(View view){
        if(view.getId() == R.id.signup){
            Intent i = new Intent(LoginActivity.this,activityRegister.class);
            startActivity(i);
        }
    }
}