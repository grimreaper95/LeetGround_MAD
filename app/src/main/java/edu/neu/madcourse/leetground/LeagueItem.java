package edu.neu.madcourse.leetground;

public class LeagueItem  {
    private String leagueName;
    private int leagueId;
    private String userNumber;

    public LeagueItem(String leagueName, int leagueId, String userNumber){
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

    public String getUser_number(){
        return userNumber;
    }
}
