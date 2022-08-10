package edu.neu.madcourse.leetground;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private final String userName;
    private int userPoints;
    private int userRank;
    private int easySolved;
    private int medium;
    private int hardSolved;

    /**
     * Constructs a user object with the specified userName and userPoints.
     *
     * @param userName - userName of the user
     * @param userPoints -  points of the user
     * @param userRank -  rank of the user
     */
    public User(String userName, int userPoints, int userRank) {
        this.userRank = userRank;
        this.userName = userName;
        this.userPoints = userPoints;
    }

    protected User(Parcel in) {
        userName = in.readString();
        userPoints = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public String getUserName() {
        return userName;
    }

    public int getUserRank() {
        return userRank;
    }

    public int getUserPoints() {
        return userPoints;
    }

//    public void setUserPoints(int score) {
//        this.userPoints = score;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeInt(userPoints);
    }
}
