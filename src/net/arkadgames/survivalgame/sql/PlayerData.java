package net.arkadgames.survivalgame.sql;

public class PlayerData {

    private boolean saveInDB;
    private boolean lastWin;
    private int play;
    private int win;
    private int kill;
    private long playTime;
    private int rank;

    public PlayerData() {
        this.saveInDB = false;
        this.lastWin = false;
        this.play = 0;
        this.win = 0;
        this.kill = 0;
        this.playTime = 0;
    }

    public boolean isLastWin() {
        return lastWin;
    }

    public void setLastWin(boolean lastWin) {
        this.lastWin = lastWin;
    }

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    protected void setCanSaveInDB(boolean value) {
        this.saveInDB = value;
    }

    public boolean canSaveInDB() {
        return this.saveInDB;
    }

    protected void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "saveInDB=" + this.saveInDB +
                ", lastWin=" + this.lastWin +
                ", play=" + this.play +
                ", win=" + this.win +
                ", kill=" + this.kill +
                ", playTime=" + this.playTime +
                ", rank=" + this.rank +
                '}';
    }
}
