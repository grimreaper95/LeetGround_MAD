package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView profileImage;

    private TextView tvUserName;
    private TextView tvUserPoints;
    private TextView tvUserCoins;
    private TextView tvProfileName;
    private TextView tvEmail;
    private static final int pic_id = 123;
//    private List<LeagueRank> leagueDataList;
    private RecyclerView leagueRecyclerView;
    private LeagueRankAdapter leagueRankAdapter;

    private String userId;
    private String userName;
    private String profileName;
    private int userPoints;
    private int userCoins;
    private SharedPreferences sharedPreferences;
    private Button logoutButton;
    private Switch reminder;
    private Button save;
    private TextInputEditText billingInputEditText;
    private RequestQueue requestQueue;
    private Bitmap profileImgBitMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        this.requestQueue = Volley.newRequestQueue(this);
        tvEmail= findViewById(R.id.user_email);
//        leagueDataList = new ArrayList<>();
        //leagueRecyclerView = findViewById(R.id.league_rank_recycler_view);
        profileImage = findViewById(R.id.user_profile_image);
        billingInputEditText= findViewById(R.id.billing_address);

//        leagueRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        leagueRankAdapter = new LeagueRankAdapter(leagueDataList, this);
//        leagueRecyclerView.setAdapter(leagueRankAdapter);
        sharedPreferences = getSharedPreferences("MadSharedPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
        userName = sharedPreferences.getString("userName", "");
        userCoins = sharedPreferences.getInt("coins", 0);
        userPoints = sharedPreferences.getInt("points", 0);
        profileName = sharedPreferences.getString("name", "");
        tvEmail.setText(sharedPreferences.getString("email", ""));
        String encodedProfilePicImgStr= sharedPreferences.getString("profilePicImgByteArrStr", "");
        if(encodedProfilePicImgStr!=null) {
            byte[] profilePicImgByteArr = Base64.decode(encodedProfilePicImgStr, Base64.DEFAULT);
            Bitmap profilePicBitmap = BitmapFactory.decodeByteArray(profilePicImgByteArr, 0, profilePicImgByteArr.length);
            profileImage.setImageBitmap(profilePicBitmap);
        }
        billingInputEditText.setText(sharedPreferences.getString("billingAddress", ""));
        logoutButton = findViewById(R.id.log_out);
        tvUserName = findViewById(R.id.leetcode_username_value);
        tvUserName.setText(userName);
        tvUserCoins = findViewById(R.id.user_coins_value);
        tvUserCoins.setText(String.valueOf(userCoins));

        tvUserPoints = findViewById(R.id.user_leagues);

        tvUserPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       // tvUserPoints.setText(String.valueOf(userPoints));

        tvProfileName = findViewById(R.id.user_profile_name);
        tvProfileName.setText(profileName);

        // initiate a Switch
        reminder = findViewById(R.id.reminder_switch);
        reminder.setChecked(sharedPreferences.getBoolean("isReminderOn",true));

        save = findViewById(R.id.save_changes);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplication(), LoginActivity.class));
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences.getBoolean("isReminderOn",reminder.isChecked());
                sharedPreferences.getString("billingAddress",billingInputEditText.getText().toString());

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority(SERVER_URL)
                        .appendPath("user");

                String url = builder.build().toString();

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("id",userId);
                params.put("name",profileName);
                params.put("username",userName);
                params.put("email",sharedPreferences.getString("email", ""));
                params.put("password",sharedPreferences.getString("password", ""));
                params.put("isReminderOn",(reminder.isChecked()==true?"1":"0"));
                params.put("coins",""+userCoins);
                params.put("billingAddress",billingInputEditText.getText().toString());
                byte[] profilePicImgByteArr=convertBitmapToByteArray(profileImgBitMap);
                String encodedProfilePicImgStr= Base64.encodeToString(profilePicImgByteArr, Base64.DEFAULT);
                params.put("profilePic",encodedProfilePicImgStr);
           //     System.out.println("encodedProfilePicImgStr: "+encodedProfilePicImgStr);
           //     System.out.println("params: "+params);
                JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.PUT,url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(request_json);

Toast.makeText(UserProfileActivity.this,"Your changes are saved!", Toast.LENGTH_SHORT).show();
            }
        });
//        getAllLeagues();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, pic_id);//zero can be replaced with any action code (called requestCode)
            }
        });


    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                 //   Log.e(BitmapUtils.class.getSimpleName(), "ByteArrayOutputStream was not closed");
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
      /*  switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Log.d("shashank95", "case 1");
                    profileImage.setImageURI(selectedImage);
                }
                break;
        }
       */
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {

            // BitMap is data structure of image file
            // which store the image in memory
            Bitmap photo = (Bitmap)imageReturnedIntent.getExtras()
                    .get("data");

            // Set the image in imageview for display
            profileImage.setImageBitmap(photo);
            profileImgBitMap=photo;
        }
    }

//    private void getAllLeagues() {
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("https")
//                .authority(SERVER_URL)
//                .appendPath("user")
//                .appendPath(userId)
//                .appendPath("League");
//
//        String url = builder.build().toString();
//        Log.d("shashank", url);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // Do something with response
//                        //mTextView.setText(response.toString());
//                        Log.d("shashank", response.toString());
//                        // Process the JSON
//                        try{
//                            leagueDataList.clear();
//                            // Loop through the array elements
//                            for(int i=0; i < response.length(); i++){
//                                // Get current json object
//                                JSONObject student = response.getJSONObject(i);
//                                Log.d("shashank", student.toString());
//                                // Get the current student (json object) data
//                                String leagueName = student.getString("leagueId");
//                                int leagueRank = Integer.parseInt(student.getString("easySolved"));
//                                leagueDataList.add(new LeagueRank(leagueName, leagueRank));
//                                if (i == response.length() - 1) {
//                                    Log.d("shashank95", leagueDataList.size() + " size ");
//                                    updateLeagueRankRows();
//                                }
//
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error){
//                        // Do something when error occurred
//                        Log.d("shashank", error.toString());
//                    }
//                }
//        );
//        SingletonVolley.getInstance(this).addToRequestQueue(jsonArrayRequest);
//    }

    private void updateLeagueRankRows() {
        Log.d("shashank95", "updating leagueRank Adapter");
        leagueRankAdapter.notifyDataSetChanged();
    }

    public void goShopping(View view) {
        startActivity(new Intent(this, Store.class));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(this, LeaguesActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userCoins = sharedPreferences.getInt("coins", 0);
        tvUserCoins.setText(String.valueOf(userCoins));
    }
}