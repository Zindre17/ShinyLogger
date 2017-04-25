package com.aarstrand.zindre.pokechecklist;

/**
 * Created by Zindre on 29-Dec-16.
 */
public class Catch {
    String odds,location,game;
    int number, attempts;

    public Catch(){
    }
    public Catch(int number,int attempts,String odds, String game,String location){
        this.number = number;
        this.attempts = attempts;
        this.odds = odds;
        this.game = game;
        this.location = location;
    }

    public String getOdds() {
        return this.odds;
    }

    public String getLocation() {
        return this.location;
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

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
