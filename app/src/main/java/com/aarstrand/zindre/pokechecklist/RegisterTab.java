package com.aarstrand.zindre.pokechecklist;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SwitchCompat;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;


public class RegisterTab extends Fragment {


    Cursor methods;
    SimpleCursorAdapter methodSpinnerAdapter;
    Spinner methodSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_tab,container,false);
        final PokeCheckListDbHelper dbHelper = PokeCheckListDbHelper.getInstance(getContext());
        Cursor pokemon = dbHelper.getPokemon(getArguments().getInt(HuntPagerAdapter.ARG_NUMBER));
        pokemon.moveToFirst();
        ((ImageView)view.findViewById(R.id.register_image)).setImageBitmap(Tools.convertFromBlobToBitmap(pokemon.getBlob(pokemon.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG))));
        ((TextView)view.findViewById(R.id.register_name)).setText(pokemon.getString(pokemon.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));
        ((TextView)view.findViewById(R.id.register_number)).setText("#"+pokemon.getInt(pokemon.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER)));

        final EditText encounters = (EditText)view.findViewById(R.id.register_edit_encounters);
        encounters.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });


        (view.findViewById(R.id.register_switch)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    v.performClick();
            }
        });

        Cursor c = dbHelper.getPokemon(getArguments().getInt(HuntPagerAdapter.ARG_NUMBER));
        c.moveToFirst();
        ((ImageView)view.findViewById(R.id.register_image)).setImageBitmap(Tools.convertFromBlobToBitmap(c.getBlob(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG))));
        ((TextView)view.findViewById(R.id.register_name)).setText(c.getString(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));
        ((TextView)view.findViewById(R.id.register_number)).setText("#"+c.getInt(c.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER)));
        return view;
    }

    public PopupWindow customDropDownMenu(){
        PopupWindow popupWindow = new PopupWindow(getContext());
        ListView items = new ListView(getContext());

        return null;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
