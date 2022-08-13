package edu.neu.madcourse.leetground;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeagueItemAdapter extends RecyclerView.Adapter<LeagueItemAdapter.LeagueItemViewHolder> {

    private Context mcontext;
    private ArrayList<LeagueItem> mleagueItems;

    public LeagueItemAdapter(Context context,ArrayList<LeagueItem> leagueItems){
        mcontext = context;
        mleagueItems = leagueItems;
    }

    @NonNull
    @Override
    public LeagueItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.league_item,parent,false);
        return new LeagueItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueItemViewHolder holder, int position) {
        LeagueItem currentItem = mleagueItems.get(position);
        String league_name = currentItem.getLeaguename();
        int user_number = currentItem.getUser_number();

        holder.league_name.setText(league_name);
        holder.user_num.setText(String.valueOf(user_number));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LeaguesActivity) v.getContext()).onClickCalled(holder.league_name.getText().toString()
                , String.valueOf(mleagueItems.get(position).getLeagueId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mleagueItems.size();
    }

    public class LeagueItemViewHolder extends RecyclerView.ViewHolder{
        public TextView league_name;
        public TextView user_num;
        public LeagueItemViewHolder(@NonNull View itemView) {

            // to get the item (league name and number of users)
            super(itemView);
            league_name = itemView.findViewById(R.id.item_name);
            user_num = itemView.findViewById(R.id.num_user);
        }
    }


}
