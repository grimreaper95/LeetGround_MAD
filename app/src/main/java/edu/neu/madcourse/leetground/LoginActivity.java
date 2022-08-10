package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView signup;
    private EditText username, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("authenticate");

        String url = builder.build().toString();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Do something with response
                                //mTextView.setText(response.toString());
                                Log.d("shashank", response.toString());
                                // Process the JSON
//                                try{
////                                     Loop through the array elements
////                                    for(int i=0; i < response.length(); i++){
////                                        // Get current json object
////                                        JSONObject student = response.getJSONObject(i);
////                                        Log.d("shashank", student.toString());
////                                        // Get the current student (json object) data
////                                        String userName = student.getString("userId");
////                                        int userScore = Integer.parseInt(student.getString("easySolved"));
////                                        userDataList.add(new User(userName, userScore, i + 1));
////                                        if (i == response.length() - 1) {
////                                            Log.d("shashank95", userDataList.size() + " size ");
////                                            updateLeaderBoardRows();
////                                        }
////
////                                    }
//                                }catch (JSONException e){
//                                    e.printStackTrace();
//                                }
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                // Do something when error occurred
                                Log.d("shashank", error.getMessage());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // below line we are creating a map for
                        // storing our values in key and value pair.
                        Map<String, String> params = new HashMap<String, String>();

                        // on below line we are passing our key
                        // and value pair to our parameters.
                        params.put("username", username.getText().toString());
                        params.put("password", password.getText().toString());

                        // at last we are
                        // returning our params.
                        return params;
                    }
                };
                SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
            }
        });
    }
}