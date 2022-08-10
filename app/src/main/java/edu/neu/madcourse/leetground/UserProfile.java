package edu.neu.madcourse.leetground;

public class UserProfile {
    private String name;
    private String userName;
    private String password;
    private String email;
    private String jwtToken;
    private boolean isReminderOn;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    private int coins;

    public UserProfile(String jwtToken, String name, String userName, String password,
                       String email, Boolean isReminderOn,
                       int coins) {
        this.jwtToken = jwtToken;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.coins = coins;
        this.isReminderOn = isReminderOn;
    }

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
