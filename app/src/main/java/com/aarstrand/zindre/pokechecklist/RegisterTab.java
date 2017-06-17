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
        final PokeCheckListDbHelper dbHelper = new PokeCheckListDbHelper(getContext());
        Cursor pokemon = dbHelper.getPokemon(getArguments().getInt(HuntPagerAdapter.ARG_NUMBER));
        pokemon.moveToFirst();
        ((ImageView)view.findViewById(R.id.register_image)).setImageBitmap(PokeCheckListDbHelper.convertFromBlobToBitmap(pokemon.getBlob(PokeCheckListDbHelper.POKEMON_IMAGE)));
        ((TextView)view.findViewById(R.id.register_name)).setText(pokemon.getString(PokeCheckListDbHelper.POKEMON_NAME));
        ((TextView)view.findViewById(R.id.register_number)).setText("#"+pokemon.getInt(PokeCheckListDbHelper.POKEMON_NUMBER));

        Cursor games = dbHelper.getGames(pokemon.getInt(PokeCheckListDbHelper.POKEMON_GEN));
        SimpleCursorAdapter gamesSpinnerAdapter = new SimpleCursorAdapter(getContext(),android.R.layout.simple_spinner_item,games,new String[]{PokeCheckListContract.Game.COLOUMN_NAME_GAME},new int[]{android.R.id.text1},0);
        gamesSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Spinner gameSpinner = (Spinner)view.findViewById(R.id.register_game_spinner);
        gameSpinner.setAdapter(gamesSpinnerAdapter);
        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.requestFocus();
                methods = dbHelper.getMethods(((Cursor)(parent.getSelectedItem())).getInt(PokeCheckListDbHelper.GAME_GEN));
                methodSpinnerAdapter = new SimpleCursorAdapter(getContext(),android.R.layout.simple_spinner_item,methods,new String[]{PokeCheckListContract.Method.COLOUMN_NAME_METHOD},new int[]{android.R.id.text1},0);
                methodSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                methodSpinner.setAdapter(methodSpinnerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        methods = dbHelper.getMethods(((Cursor)(gameSpinner.getSelectedItem())).getInt(PokeCheckListDbHelper.GAME_GEN));
        methodSpinnerAdapter = new SimpleCursorAdapter(getContext(),android.R.layout.simple_spinner_item,methods,new String[]{PokeCheckListContract.Method.COLOUMN_NAME_METHOD},new int[]{android.R.id.text1},0);
        methodSpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        methodSpinner = (Spinner)view.findViewById(R.id.register_method_spinner);
        methodSpinner.setAdapter(methodSpinnerAdapter);

        final EditText encounters = (EditText)view.findViewById(R.id.register_edit_encounters);
        encounters.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        encounters.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //Clear focus here from edittext
                    encounters.clearFocus();
                }
                return false;
            }
        });

        (view.findViewById(R.id.register_switch)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    v.performClick();
            }
        });

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
