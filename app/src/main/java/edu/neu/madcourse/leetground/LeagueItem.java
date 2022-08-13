package edu.neu.madcourse.leetground;

public class LeagueItem  {
    private String leagueName;
    private int leagueId;
    private int userNumber;
    private int leagueSize;

    public LeagueItem(String leagueName, int leagueId, int userNumber){
        this.leagueName = leagueName;
        this.leagueId = leagueId;
        this.userNumber = userNumber;
    }

    public String getLeaguename(){
        return leagueName;
    }

    public int getLeagueId(){
        return leagueId;
    }

    public int getUser_number(){
        return userNumber;
    }
}
