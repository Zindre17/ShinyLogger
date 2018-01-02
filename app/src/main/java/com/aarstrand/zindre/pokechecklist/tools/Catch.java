package com.aarstrand.zindre.pokechecklist.tools;


public class Catch {
    int number, attempts;
    boolean odds;
    private String method, game, nickname;

    public Catch(){
    }
    public Catch(int number,int attempts,boolean odds, String game, String nickname){
        this.number = number;
        this.attempts = attempts;
        this.odds = odds;
        this.game = game;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean getOdds() {
        return this.odds;
    }

    public String getGame() {
        return this.game;
    }

    public int getNumber() {
        return this.number;
    }

    public int getAttempts() {
        return this.attempts;
    }

    public void setOdds(boolean odds) {
        this.odds = odds;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method){
        this.method = method;
    }
}
