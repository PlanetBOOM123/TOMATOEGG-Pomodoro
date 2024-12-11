package com.android.sql.bean;

public class UserBean {
    private int id;
    private String touxiang;
    private String account;
    private String password;
    private String gxmsg;//个性签名
    private int history_gold=0;//获得的金币
    private int gold=0;//金币

    public UserBean(int id, String touxiang, String account, String password, String gxmsg, int history_gold, int gold) {
        this.id = id;
        this.touxiang = touxiang;
        this.account = account;
        this.password = password;
        this.gxmsg = gxmsg;
        this.history_gold = history_gold;
        this.gold = gold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGxmsg() {
        return gxmsg;
    }

    public void setGxmsg(String gxmsg) {
        this.gxmsg = gxmsg;
    }

    public int getHistory_gold() {
        return history_gold;
    }

    public void setHistory_gold(int history_gold) {
        this.history_gold = history_gold;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
