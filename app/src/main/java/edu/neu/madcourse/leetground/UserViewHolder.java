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

public class UserViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView tvUserName;
    public TextView tvUserPoints;
    public TextView tvUserRank;

    public UserViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        this.tvUserRank = itemView.findViewById(R.id.user_rank);
        this.tvUserName = itemView.findViewById(R.id.user_name);
        this.tvUserPoints = itemView.findViewById(R.id.user_score);

    }

    public void bindThisData(User theUserToBind) {
        tvUserRank.setText("#" + String.valueOf(theUserToBind.getUserRank()));
        // sets the name of the link to the name textview of the viewholder.
        tvUserName.setText(theUserToBind.getUserName());
        // sets the url of the person to the url textview of the viewholder.
        tvUserPoints.setText(String.valueOf(theUserToBind.getUserPoints()));
    }

}
