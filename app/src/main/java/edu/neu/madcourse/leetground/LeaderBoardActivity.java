package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity{

    private List<User> userDataList;
    private RecyclerView linkRecyclerView;
    private LeaderBoardAdapter leaderBoardAdapter;
    private String leagueId;
    private String leagueName = "dummy league 1";
    private String leagueAccessCode = "MjYtZHVtbXkgbGVhZ3VlIDItMg";
    private String deepLink = "https://leetground/league";

    private String userId;
    private String userName;
    private String userScore;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tvLeagueName;
    private TextView tvUserRank;
    private TextView tvUserName;
    private TextView tvUserScore;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

//        leagueName = getIntent().getStringExtra("league_name");
        leagueId = getIntent().getStringExtra("league_id");
//        leagueAccessCode = getIntent().getStringExtra("league_access_code");

        mSwipeRefreshLayout = findViewById(R.id.refresh_leaderboard_layout);
        userDataList = new ArrayList<>();
        // UPDATE LEAGUE ID RECEIVED FROM PREVIOUS PAGE AS STRING

        if (savedInstanceState != null) {
            userDataList = new ArrayList<>(savedInstanceState.getParcelableArrayList("link_data_list"));
        }

        linkRecyclerView = findViewById(R.id.leaderboard_recycler_view);
        linkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderBoardAdapter = new LeaderBoardAdapter(userDataList, this);
        linkRecyclerView.setAdapter(leaderBoardAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MadSharedPref", MODE_PRIVATE);

        String leagueName = sharedPreferences.getString("leagueName", "");
        //sharedPreferences.getString(, "");
        userId = sharedPreferences.getString("userId", "");
        userName = sharedPreferences.getString("userName", "");
        Log.d("shashank username", userName);
        this.leagueName = leagueName;
//        userScore = sharedPreferences.getString("userScore", "");



        tvLeagueName = findViewById(R.id.league_name);
        tvUserRank = findViewById(R.id.current_user_rank);
//        tvUserRank.setText("#3");

        tvUserName = findViewById(R.id.current_user_name);
        tvUserName.setText(userName);

        tvUserScore = findViewById(R.id.current_user_score);
//        tvUserScore.setText(userScore);

        profileImage = findViewById(R.id.user_leaderboard_profile_image);

        String encodedProfilePicImgStr= sharedPreferences.getString("profilePicImgByteArrStr", "");

        if(encodedProfilePicImgStr!=null) {
            byte[] profilePicImgByteArr = Base64.decode(encodedProfilePicImgStr, Base64.DEFAULT);
            Bitmap profilePicBitmap = BitmapFactory.decodeByteArray(profilePicImgByteArr, 0, profilePicImgByteArr.length);
            profileImage.setImageBitmap(profilePicBitmap);
        }

        getAllUsers();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Execute code when refresh layout swiped
                getAllUsers();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.leaderboard_menu, menu);
        return true;
    }

    private void getAllUsers() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("league")
                .appendPath(leagueId)
                .appendPath("rank")
                .appendPath("memebers");

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
                        // Process the JSON
                        try{
                            userDataList.clear();
                            // Loop through the array elements
                            for(int i=0; i < response.length(); i++){
                                // Get current json object
                                JSONObject jsonObject = response.getJSONObject(i).getJSONObject("leagueMemberDTO");
                                Log.d("shashank", jsonObject.toString());

                                String userName = jsonObject.getJSONObject("maduser").getString("username");
                                String currentUserId = jsonObject.getJSONObject("maduser").getString("id");

                                String userScore = response.getJSONObject(i).getString("points");
                                String userRank = response.getJSONObject(i).getString("rank");


                                userDataList.add(new User(userName, Integer.parseInt(userScore), i + 1));


                                if (currentUserId.equals( userId )) {
                                    tvUserScore.setText(userScore);
                                    tvUserRank.setText("#" + userRank);
                                }

                                if (i == response.length() - 1) {
                                    JSONObject leagueObject = jsonObject.getJSONObject("leagueDetails");
                                    leagueId = leagueObject.getString("id");

                                    leagueName = leagueObject.getString("leagueName");
                                    tvLeagueName.setText(leagueName);

                                    leagueAccessCode = leagueObject.getString("accessCode");

                                    Log.d("shashank95", userDataList.size() + " size ");
                                    updateLeaderBoardRows();
                                }

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        } finally {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
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



//    int cnt = 0;



//    private void refreshLeaderBoard(List<User> users) {
//        //RequestQueue requestQueue = Volley.newRequestQueue(this);
////        Log.d("shashank95", userNames.size() + "");
//
//        /*for (User user : users) {
//            String url = "https://leetcode-stats-api.herokuapp.com/" + user.getUserName();
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                    Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    //textView.setText("Response: " + response.toString());
//                    Log.d("shashank95", user.getUserName() + " " + response.toString());
//                    try {
//                        user.setUserScore(Integer.parseInt(response.getString("totalSolved")));
//                    } catch (JSONException exception) {
//                        Log.d("shashank95", exception.toString());
//                    }
//                    cnt++;
//                    if (cnt == users.size()) {
//                        Log.d("shashank95", "" + cnt);
//                        updateLeaderBoardRows();
//                    }
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("shashank95", error.toString());
//                }
//            });
//            SingletonVolley.getInstance(this).addToRequestQueue(jsonObjectRequest);
//        }*/
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("link_data_list", new ArrayList(userDataList));
    }

    private void updateLeaderBoardRows() {
        Log.d("shashank95", "updating adapter");
        leaderBoardAdapter.notifyDataSetChanged();
    }


    private String getDeepLink() {
        return deepLink + "/" + leagueAccessCode;
    }

    private void shareInvite() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra
                (Intent.EXTRA_TEXT, "Join our league " +
                        leagueName +
                        " on LeetGround using the following access code:\n" +
                        leagueAccessCode + "\n or you can click here: " + getDeepLink() + " if you have " +
                        "LeetGround installed!");
        startActivity(shareIntent);
    }


    private void startLeagueChat() {
        Log.d("shashank username", userName);
        startActivity(new Intent(this, ChatActivity.class)
                .putExtra("userId", userId)
                .putExtra("userName", userName)
                .putExtra("leagueId", leagueId)
                .putExtra("leagueName", leagueName));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.league_chat:
                startLeagueChat();
                return true;
            case R.id.share_invite:
                // User chose the "Settings" item, show the app settings UI...
                shareInvite();
                return true;

            case R.id.refresh_leaderboard:
                mSwipeRefreshLayout.setRefreshing(true);
                getAllUsers();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}