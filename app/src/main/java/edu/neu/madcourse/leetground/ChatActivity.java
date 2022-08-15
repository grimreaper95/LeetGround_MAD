package edu.neu.madcourse.leetground;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Response;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    /* to do: get leagueId, useId, usernmae, league name*/
    String username;
    int leagueId;
    int userId=0;
    String leagueName="dummy league";
    Button button;
    List<MessageModel> chatMessages=new ArrayList<MessageModel>();
    Set<String> visitedMsg=new HashSet<String>();
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);

        View screenView = findViewById(R.id.relative_layout);
        // set background
        screenView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_whatsapp_wallpaper));

        Intent intent = getIntent();

        this.userId = Integer.parseInt(intent.getStringExtra("userId"));
        this.username = intent.getStringExtra("userName");
        Log.d("shashank username", this.username);
        this.leagueId = Integer.parseInt(intent.getStringExtra("leagueId"));
        this.leagueName  = intent.getStringExtra("leagueName");


        this.button= findViewById(R.id.sendMessage);

        adapter = new CustomAdapter(ChatActivity.this, chatMessages);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerView.setAdapter(adapter);
        this.requestQueue = Volley.newRequestQueue(this);

        getAllChats();

    }

    private void getAllChats() {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://finalproject-7a8ea-default-rtdb.firebaseio.com/");
        DatabaseReference ref = database.getReference("final_project");
        DatabaseReference stickerUserRelationRef = ref.child("chat_messages_for_league_" + leagueId);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("onDataChange is called!!");
                // adapter.setClickListener(MainActivity.this);
                //   chatMessages.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if(visitedMsg.contains(ds.getKey())){
                        continue;
                    }
                    System.out.println("Reading values");
                    visitedMsg.add(ds.getKey());
                    MessageModel value=ds.getValue(MessageModel.class);
                    System.out.println(" new chat message: "+value);
                    if(value.username.equals(username)){
                        value.messageType=CustomAdapter.MESSAGE_TYPE_OUT;
                    }
                    chatMessages.add(value);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Cannot retrive chats at this moment", Toast.LENGTH_LONG).show();
            }
        };

        stickerUserRelationRef.addValueEventListener(valueEventListener);

    }


    public void sendMessage(View v) {

        System.out.println("Message btn is clicked!!");
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://finalproject-7a8ea-default-rtdb.firebaseio.com/");
        DatabaseReference ref = database.getReference("final_project");
        DatabaseReference usersRef = ref.child("chat_messages_for_league_"+leagueId);
        EditText messageToBeSent=  findViewById(R.id.editTextMessageToBeSent);
        if(TextUtils.isEmpty(messageToBeSent.getText().toString())){
            Toast.makeText(this, "message is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("message to be sent: "+messageToBeSent.getText().toString());
        MessageModel chatMessage=new MessageModel(messageToBeSent.getText().toString(),CustomAdapter.MESSAGE_TYPE_IN, this.username);
        usersRef.push().setValue(chatMessage);
        messageToBeSent.setText("");
        sendNotification(leagueId);
    }

    private void sendNotification(int leagueId) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("body", "New text from league: "+leagueName);
        params.put("title","New message!!");

        JsonObjectRequest request_json = new JsonObjectRequest( "https://mad-backend-sprinboot-server.herokuapp.com/notification/user/"+userId+"/league/"+leagueId, new JSONObject(params),
                new com.android.volley.Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ChatActivity.this, "Couldn't notify other league members", Toast.LENGTH_LONG).show();

            }
        });
        requestQueue.add(request_json);
    }





}