package com.aarstrand.zindre.pokechecklist;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RegisterTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_tab,container,false);
        PokeCheckListDbHelper dbHelper = PokeCheckListDbHelper.getInstance(getContext());
        Cursor c = dbHelper.getPokemon(getArguments().getInt(HuntPagerAdapter.ARG_NUMBER));
        c.moveToFirst();
        ((ImageView)view.findViewById(R.id.register_image)).setImageBitmap(PokeCheckListDbHelper.convertFromBlobToBitmap(c.getBlob(PokeCheckListDbHelper.POKEMON_IMAGE)));
        ((TextView)view.findViewById(R.id.register_name)).setText(c.getString(PokeCheckListDbHelper.POKEMON_NAME));
        ((TextView)view.findViewById(R.id.register_number)).setText("#"+c.getInt(PokeCheckListDbHelper.POKEMON_NUMBER));
        return view;
    }
}
