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
public class LeaderBoardAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private final List<User> user;
    private final Context context;

    /**
     * Creates a LinkAdapter with the provided arraylist of Link objects.
     *
     * @param user    arraylist of link object.
     * @param context   context of the activity used for inflating layout of the viewholder.
     */
    public LeaderBoardAdapter(List<User> user, Context context) {
        this.user = user;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(context, LayoutInflater.from(context).inflate(R.layout.leaderboard_item_link,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bindThisData(user.get(position));
//        String userName = user.get(position).getUserName();
    }

    @Override
    public int getItemCount() {
        // Returns the size of the recyclerview that is the list of the arraylist.
        return user.size();
    }

}
