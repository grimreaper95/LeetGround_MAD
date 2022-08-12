package edu.neu.madcourse.leetground;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>{

    private Context mContext;
    private ArrayList<ShopItem> mshopItems;

    public ShopItemAdapter(Context context,ArrayList<ShopItem> shopItems){
        mContext = context;
        mshopItems = shopItems;
    }

    @NonNull
    @Override
    public ShopItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.shop_item,parent,false);
        return new ShopItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemViewHolder holder, int position) {
        ShopItem Current_shopItem = mshopItems.get(position);
        String title = Current_shopItem.getTitle();
        String price = Current_shopItem.getPrice();
        String imageurl = Current_shopItem.getImageurl();

        holder.mtitle.setText(title);
        holder.mprice.setText("Coins:$"+price);
        Picasso.with(mContext).load(imageurl).fit().centerInside().into(holder.mimageview);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)(mContext);
                FragmentManager fm = activity.getSupportFragmentManager();
                Shop_Confirm_Dialogue alertDialog = new Shop_Confirm_Dialogue();
                alertDialog.getPrice(price,mContext);
                alertDialog.show(fm, "fragment_alert");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mshopItems.size();
    }

    public class ShopItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mimageview;
        public TextView mtitle;
        public TextView mprice;
        public Button button;
        private ShopItemAdapter shopItemAdapter;
        public ShopItemViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.buy_button);
            mimageview = itemView.findViewById(R.id.item_image);
            mtitle = itemView.findViewById(R.id.item_name);
            mprice = itemView.findViewById(R.id.item_price);
        }

    }
}
