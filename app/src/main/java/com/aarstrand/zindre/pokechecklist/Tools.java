package com.aarstrand.zindre.pokechecklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Tools {

    public final static int DEX_COUNT = 721;

    public static Bitmap convertFromBlobToBitmap(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob,0,blob.length);
    }
}
