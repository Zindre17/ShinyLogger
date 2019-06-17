package com.aarstrand.zindre.pokechecklist.db.models;

import android.graphics.Bitmap;

import com.aarstrand.zindre.pokechecklist.tools.old.Tools;
public class Catch {

    private int _id;
    private int number;
    private String nickname;
    private String game;
    private String method;
    private boolean shinyCharm;
    private int attempts;
    private int chain;
    private double odds;
    private Bitmap image;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setGame(int game){
        this.game = Tools.translateGame(game);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isShinyCharm() {
        return shinyCharm;
    }

    public void setShinyCharm(boolean shinyCharm) {
        this.shinyCharm = shinyCharm;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getChain() {
        return chain;
    }

    public void setChain(int chain) {
        this.chain = chain;
    }

    public double getOdds() {
        return odds;
    }

    public void setOdds(double odds) {
        this.odds = odds;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
