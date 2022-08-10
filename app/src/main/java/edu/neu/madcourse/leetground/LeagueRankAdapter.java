package edu.neu.madcourse.leetground;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is a recyclerview adapter class, the purpose of this class is to act as a bridge between the
 * collection (arraylist) and the view (recyclerview). This class provides 3 methods that are
 * utilised for binding the data to the view. The explanation of each method is provided in comments
 * within the methods.
 */
public class LeagueRankAdapter extends RecyclerView.Adapter<LeagueRankViewHolder> {

    private final List<LeagueRank> leagueRank;
    private final Context context;

    /**
     * Creates a LinkAdapter with the provided arraylist of Link objects.
     *
     * @param leagueRank    arraylist of leagueRank object.
     * @param context   context of the activity used for inflating layout of the viewholder.
     */
    public LeagueRankAdapter(List<LeagueRank> leagueRank, Context context) {
        this.leagueRank = leagueRank;
        this.context = context;
    }

    @NonNull
    @Override
    public LeagueRankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeagueRankViewHolder(context,
                LayoutInflater.from(context).inflate(R.layout.leaguerank_item_link,
                        parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueRankViewHolder holder, int position) {
        holder.bindThisData(leagueRank.get(position));
    }

    @Override
    public int getItemCount() {
        // Returns the size of the recyclerview that is the list of the arraylist.
        return leagueRank.size();
    }

}
