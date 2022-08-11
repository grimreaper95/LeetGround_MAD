package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class activityRegister extends AppCompatActivity {

    private EditText username;
    private EditText leetcodename;
    private EditText email;
    private EditText password;
    private EditText confirmpassword;
    private AppCompatButton signupbutton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.registration_name);
        leetcodename = findViewById(R.id.registration_leetcodename);
        email = findViewById(R.id.registration_email);
        password = findViewById(R.id.registration_password);
        confirmpassword = findViewById(R.id.registration_confirm_password);
        signupbutton = findViewById(R.id.registration_submit);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(confirmpassword.getText().toString())){
                    postUser();
                }else{
                    Toast.makeText(activityRegister.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void postUser(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("user");

        String url = builder.build().toString();
        Log.d("shashankCreateDialogue", url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name",username.getText().toString());
        params.put("username",leetcodename.getText().toString());
        params.put("email",email.getText().toString());
        params.put("password",password.getText().toString());
        params.put("isReminderOn","1");
        params.put("coins","0");

        JsonObjectRequest request_json = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //dialogueListener.notify(editLeague_name.getText().toString(), userId);
                        try {
                            String id = response.getString("id");
                            if(id.equals("-1")){
                                Toast.makeText(activityRegister.this, "Your Leetcode username doesn't exist.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(activityRegister.this, "Welcome to LeetGround!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request_json);
    }
}
