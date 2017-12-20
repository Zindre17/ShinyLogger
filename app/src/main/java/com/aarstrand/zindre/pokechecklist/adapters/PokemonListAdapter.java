package com.aarstrand.zindre.pokechecklist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.aarstrand.zindre.pokechecklist.PokedexActivity;
import com.aarstrand.zindre.pokechecklist.R;
import com.aarstrand.zindre.pokechecklist.tools.Tools;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListContract;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;

import java.util.ArrayList;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonHolder> implements PokeCheckListDbHelper.DbListener{

    @Override
    public void onDataChanged() {
       //refresh();
    }

    public void setListener(PokedexActivity listener) {
        this.mListener = listener;
    }

    public interface PokemonListListener{
        void OnButtonClicked(int pos);
    }

    private Context adapterContext;

    private PokemonListListener mListener;
    private PokeCheckListDbHelper dbHelper;
    private Cursor list;
    private ArrayList<Integer> caught;
    public PokemonListAdapter(Context context){
        super();
        adapterContext = context;
        dbHelper = PokeCheckListDbHelper.getInstance(context);
        dbHelper.setListener(this);
        try{
            mListener = (PokemonListListener)context;
        }catch (ClassCastException e){

        }
        list = dbHelper.getAllPokemon();
        updateCaughtList();
    }

    private void updateCaughtList() {
        caught = new ArrayList<>();
        Cursor c = dbHelper.getCaughtNumbers();
        for(int i = 0;i < c.getCount();i++){
            c.moveToNext();
            caught.add(c.getInt(c.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER)));
        }
        c.close();
    }

    public static class PokemonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView pokemon_name;
        private TextView pokemon_number;
        private ImageButton pokeball;
        private ImageView thumbnail;
        private PokemonListener mListener;

        public interface PokemonListener{
            public void onPokemonClick();
        }

        public PokemonHolder(View itemView,Context context) {
            super(itemView);
            pokemon_name = (TextView) itemView.findViewById(R.id.pokemon_name);
            pokemon_number = (TextView) itemView.findViewById(R.id.pokemon_number);
            pokeball = (ImageButton) itemView.findViewById(R.id.pokeball);
            thumbnail = (ImageView) itemView.findViewById(R.id.pokemon_image);
            mListener = (PokemonListener)context;
        }

        @Override
        public void onClick(View v) {
            mListener.onPokemonClick();
        }

    }


    @Override
    public PokemonHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View pokemonListView = inflater.inflate(R.layout.pokemon_list_item, parent, false);
        return new PokemonHolder(pokemonListView,context);
    }

    @Override
    public void onBindViewHolder(final PokemonHolder pokemonHolder, int position){

        //move the cursor to the correct row
        list.move(position-list.getPosition());

        //retrieve the image from the database and put it in the image view
        ImageView imageView = pokemonHolder.thumbnail;
        imageView.setImageBitmap(Tools.convertFromBlobToBitmap(list.getBlob(list.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_PNG))));

        //set the name of the pokemon to one of the textviews
        TextView textView = pokemonHolder.pokemon_name;
        textView.setText(list.getString(list.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NAME)));

        //set the number of the pokemon to the other textview
        TextView textView1 = pokemonHolder.pokemon_number;
        int nr = list.getInt(list.getColumnIndex(PokeCheckListContract.Pokemon.COLOUMN_NAME_NUMBER));
        textView1.setText(String.format("#%03d",nr));

        //set the imagebutton based on the boolean(int) "caught" column
        final ImageButton imageButton = pokemonHolder.pokeball;
        if(caught.contains(position+1)){
            imageButton.setImageDrawable(ContextCompat.getDrawable(adapterContext,R.drawable.pokeball));
        }else {
            imageButton.setImageDrawable(ContextCompat.getDrawable(adapterContext,R.drawable.pokeball_gray));
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: open a fragment with the option of adding a pokemon to your caught db. Also update the pokedex db
                mListener.OnButtonClicked(pokemonHolder.getAdapterPosition());
            }
        });
    }


    public void refresh(){
        list = dbHelper.getAllPokemon();
        updateCaughtList();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return list.getCount();
    }

    public void closeDbTransaction(){
        list.close();
    }

}

