package edu.neu.madcourse.leetground;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Store extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShopItemAdapter shopItemAdapter;
    private ArrayList<ShopItem> shopItems;
    private RequestQueue requestQueue;
    private static final String TAG = "Store";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_layout);
        recyclerView = findViewById(R.id.store_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopItems = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJSONshopitems();
    }

    public void parseJSONshopitems(){
        String url = "https://fakestoreapi.com/products";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // get json objet to get data
                //JSONArray jsonArray = response.getJSONArray(0);
                for(int i = 0; i < response.length();i++){
                    try {
                        Log.e(TAG,"get here already");
                        JSONObject item = response.getJSONObject(i);
                        String item_title = item.getString("title");
                        Integer item_price = item.getInt("price");
                        String item_imageurl = item.getString("image");
                        shopItems.add(new ShopItem(item_title,item_price.toString(),item_imageurl));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                shopItemAdapter = new ShopItemAdapter(Store.this,shopItems);
                recyclerView.setAdapter(shopItemAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
}
