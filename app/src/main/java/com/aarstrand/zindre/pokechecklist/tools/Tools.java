package com.aarstrand.zindre.pokechecklist.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.widget.ArrayAdapter;
import com.aarstrand.zindre.pokechecklist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tools {

    public final static int DEX_COUNT = 721;

    private static Map<String,Integer> gameMap;
    private static SparseArray<String> methodMap;

    public static Bitmap convertFromBlobToBitmap(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob,0,blob.length);
    }

    public static boolean isShinyCharmAvailable(String game){
        String[] a = new String[]{
                "Black 2",
                "White 2",
                "X",
                "Y",
                "Omega Ruby",
                "Alpha Sapphire",
                "Sun",
                "Moon"
        };
        for(String s:a){
            if(game.toLowerCase().equals(s.toLowerCase()))
                return true;
        }
        return false;
    }

    public static String[] getAvailableGames(int gen){
        if(gameMap==null){
            makeGameMap();
        }
        return makeGamesStringArray(getStartingPoint(gen));
    }


    public static String[] getAvailableMethods(String game){
        if(methodMap==null){
            makeMethodMap();
        }

        return makeMethodStringArray(game);
    }

    private static int getStartingPoint(int gen) {
        switch (gen) {
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 3;
            case 4:
                return 8;
            case 5:
                return 13;
            case 6:
                return 17;
            case 7:
                return 21;
            default:
                return 0;
        }
    }

    private static String[] makeGamesStringArray(int startingPoint) {
        String[] gameNames = new String[]{
                "Gold",
                "Silver",
                "Crystal",
                "Ruby",
                "Sapphire",
                "Emerald",
                "FireRed",
                "LeafGreen",
                "Diamond",
                "Pearl",
                "Platinum",
                "SoulSilver",
                "HeartGold",
                "Black",
                "White",
                "Black 2",
                "White 2",
                "X",
                "Y",
                "Omega Ruby",
                "Alpha Sapphire",
                "Sun",
                "Moon"
        };
        ArrayList<String> s = new ArrayList<>();
        int count = 0;
        for (String game : gameNames) {
            if (count >= startingPoint) {
                s.add(game);
            }
            count++;
        }

        return s.toArray(new String[0]);
    }

    private static String[] makeMethodStringArray(String game) {
        String[] methodString = getMethods(game.toLowerCase()).split(" ");
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < methodString.length; i++) {
            switch (methodString[i]) {
                case "re":
                    a.add("Random Encounter");
                    break;
                case "fr":
                    a.add("Fossil Revival");
                    break;
                case "pr":
                    a.add("PokeRadar");
                    break;
                case "hh":
                    a.add("Horde Hunting");
                    break;
                case "dn":
                    a.add("DexNav");
                    break;
                case "cf":
                    a.add("Chain Fishing");
                    break;
                case "b":
                    a.add("Breeding");
                    break;
                case "e":
                    a.add("Evolution");
                    break;
            }
        }
        return a.toArray(new String[0]);
    }

    private static String getMethods(String gameName) {
        int gen = gameMap.get(gameName);
        return methodMap.get(gen);
    }


    private static void makeGameMap() {
        gameMap = new HashMap<>();
        fillGameMap(gameMap);
    }

    private static void makeMethodMap() {
        methodMap = new SparseArray<>();
        fillMethodMap(methodMap);
    }

    private static void fillGameMap(Map gameMap) {
        gameMap.put("gold", 21);
        gameMap.put("silver", 21);
        gameMap.put("crystal", 22);
        gameMap.put("ruby", 31);
        gameMap.put("sapphire", 31);
        gameMap.put("emerald", 32);
        gameMap.put("firered", 33);
        gameMap.put("leafgreen", 33);
        gameMap.put("diamond", 41);
        gameMap.put("pearl", 41);
        gameMap.put("platinum", 42);
        gameMap.put("heartgold", 43);
        gameMap.put("soulsilver", 43);
        gameMap.put("black", 51);
        gameMap.put("white", 51);
        gameMap.put("black 2", 52);
        gameMap.put("white 2", 52);
        gameMap.put("x", 61);
        gameMap.put("y", 61);
        gameMap.put("omega ruby", 62);
        gameMap.put("alpha sapphire", 62);
        gameMap.put("sun", 71);
        gameMap.put("moon", 71);
    }

    private static void fillMethodMap(SparseArray methodMap) {

        methodMap.put(21, "re b e");
        methodMap.put(22, "re b e");
        methodMap.put(31, "re b e fr");
        methodMap.put(32, "re b e fr");
        methodMap.put(33, "re b e fr");
        methodMap.put(41, "re b e fr pr");
        methodMap.put(42, "re b e fr pr");
        methodMap.put(43, "re b e fr pr");
        methodMap.put(51, "re b e fr");
        methodMap.put(52, "re b e fr");
        methodMap.put(61, "re b e fr pr cf hh");
        methodMap.put(62, "re b e fr dn cf hh");
        methodMap.put(71, "re b e fr cf");
    }
}
