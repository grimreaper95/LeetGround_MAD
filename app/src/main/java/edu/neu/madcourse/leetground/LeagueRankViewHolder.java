package edu.neu.madcourse.leetground;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An implementation of the recyclerview viewholder that is created specifically with respect to the
 * item_link.xml file. The aim of this class is to provide each item in the recyclerview to the
 * adapter, another important purpose of this class is to expose the TextViews in the xml file as
 * java objects for binding the data.
 */

public class LeagueRankViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView tvLeagueName;
    public TextView tvLeagueRank;

    public LeagueRankViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        this.tvLeagueName = itemView.findViewById(R.id.league_rank_league_name);
        this.tvLeagueRank = itemView.findViewById(R.id.league_rank_league_rank);
    }

    public void bindThisData(LeagueRank theLeagueRankToBind) {
        tvLeagueRank.setText("#" + String.valueOf(theLeagueRankToBind.getLeagueRank()));
        tvLeagueName.setText(theLeagueRankToBind.getLeagueName());
    }

}
