package edu.neu.madcourse.leetground;

public class LeagueItem  {
    private String leaguename;
    private String user_number;

    public LeagueItem(String mleaguename,String muser_number){
        leaguename = mleaguename;
        user_number = muser_number;
    }

    public String getLeaguename(){
        return leaguename;
    }

    public String getUser_number(){
        return user_number;
    }
}
