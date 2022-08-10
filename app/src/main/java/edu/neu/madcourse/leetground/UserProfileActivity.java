package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView leetcodeUsername;
    private TextView name;

    private List<LeagueRank> leagueDataList;
    private RecyclerView leagueRecyclerView;
    private LeagueRankAdapter leagueRankAdapter;

    private String userId = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        leagueDataList = new ArrayList<>();
        leagueRecyclerView = findViewById(R.id.league_rank_recycler_view);
        profileImage = findViewById(R.id.user_profile_image);
        leagueRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leagueRankAdapter = new LeagueRankAdapter(leagueDataList, this);
        leagueRecyclerView.setAdapter(leagueRankAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MadSharedPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");

        getAllLeagues();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Log.d("shashank95", "case 1");
                    profileImage.setImageURI(selectedImage);
                }
                break;
        }
    }

    private void getAllLeagues() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("user")
                .appendPath(userId)
                .appendPath("League");

        String url = builder.build().toString();
        Log.d("shashank", url);
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
                        // Process the JSON
                        try{
                            leagueDataList.clear();
                            // Loop through the array elements
                            for(int i=0; i < response.length(); i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                Log.d("shashank", student.toString());
                                // Get the current student (json object) data
                                String leagueName = student.getString("leagueId");
                                int leagueRank = Integer.parseInt(student.getString("easySolved"));
                                leagueDataList.add(new LeagueRank(leagueName, leagueRank));
                                if (i == response.length() - 1) {
                                    Log.d("shashank95", leagueDataList.size() + " size ");
                                    updateLeagueRankRows();
                                }

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
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
        SingletonVolley.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void updateLeagueRankRows() {
        Log.d("shashank95", "updating leagueRank Adapter");
        leagueRankAdapter.notifyDataSetChanged();
    }
}