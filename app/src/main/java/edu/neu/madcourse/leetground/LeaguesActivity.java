package edu.neu.madcourse.leetground;

import static edu.neu.madcourse.leetground.Constants.SERVER_URL;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LeaguesActivity extends AppCompatActivity implements createDialogue.DialogueListener{
    private FloatingActionButton fab_main;
    private FloatingActionButton fab_join;
    private FloatingActionButton fab_create;
    private static final String TAG = "Leagues";
    private RecyclerView recyclerView;
    private LeagueItemAdapter leagueItemAdapter;
    private ArrayList<LeagueItem> leagueItems;
    private RequestQueue requestQueue;
    private String userId;

    //private TextView textView;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);
        fab_main = findViewById(R.id.mainbutton);
        fab_join = findViewById(R.id.join_button);
        fab_create = findViewById(R.id.create_button);


        recyclerView = findViewById(R.id.League_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        leagueItems = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("MadSharedPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");

        leagueItemAdapter = new LeagueItemAdapter(LeaguesActivity.this,leagueItems);

        parseJSON();

        Animation rotate_open = AnimationUtils.loadAnimation(LeaguesActivity.this,R.anim.rotate_open_anim);
        Animation rotate_close = AnimationUtils.loadAnimation(LeaguesActivity.this,R.anim.rotate_close_anim);
        Animation from_to = AnimationUtils.loadAnimation(LeaguesActivity.this,R.anim.from_bottom_anim);
        Animation to_from = AnimationUtils.loadAnimation(LeaguesActivity.this,R.anim.to_bottom_anim);



        // click the main button
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fab_create.getVisibility() == View.VISIBLE && fab_join.getVisibility() == View.VISIBLE){
                    fab_create.setVisibility(View.GONE);
                    fab_join.setVisibility(View.GONE);
                    fab_create.startAnimation(from_to);
                    fab_join.startAnimation(from_to);
                    fab_main.startAnimation(rotate_open);
                } else {
                    fab_create.setVisibility(View.VISIBLE);
                    fab_join.setVisibility(View.VISIBLE);
                    fab_join.startAnimation(to_from);
                    fab_create.startAnimation(to_from);
                    fab_main.startAnimation(rotate_close);
                }
            }
        });

        // click create button
        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateDialogue();
            }
        });

        // click join button
        fab_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJoinDialogue();
            }
        });

    }

    public void parseJSON(){

//        String url = "https://mad-backend-sprinboot-server.herokuapp.com/user/2/League";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(SERVER_URL)
                .appendPath("user")
                .appendPath(userId)
                .appendPath("League");;

        String url = builder.build().toString();


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // get json objet to get data
                //JSONArray jsonArray = response.getJSONArray(0);
                for(int i = 0; i < response.length();i++){
                    try {
                        Log.e(TAG,"get here already");
                        JSONObject league = response.getJSONObject(i);
                        String league_name = league.getString("leagueId");
                        leagueItems.add(new LeagueItem(league_name,userId));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setAdapter(leagueItemAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }


    public void openCreateDialogue(){
        DialogFragment create_dialogue = new createDialogue();
        create_dialogue.show(getSupportFragmentManager(),"create dialogue");

    }


    public void openJoinDialogue(){
        DialogFragment join_dialogue = new joinDialogue();
        join_dialogue.show(getSupportFragmentManager(),"join a league");
    }

    @Override
    public void notify(String name,String userid) {

        leagueItems.add(new LeagueItem(name,userid));
        leagueItemAdapter.notifyDataSetChanged();

    }

}
