package edu.neu.madcourse.leetground;

public class LeagueRank {
    private String leagueName;
    private int leagueRank;

    public LeagueRank(String leagueName, int leagueRank) {
        this.leagueRank = leagueRank;
        this.leagueName = leagueName;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public int getLeagueRank() {
        return leagueRank;
    }

    public void setLeagueRank(int leagueRank) {
        this.leagueRank = leagueRank;
    }
}
