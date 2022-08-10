package edu.neu.madcourse.leetground;

public class UserProfile {
    private String name;
    private String userName;
    private String password;
    private boolean isReminderOn;
    private int coins;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isReminderOn() {
        return isReminderOn;
    }

    public void setReminderOn(boolean reminderOn) {
        isReminderOn = reminderOn;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
