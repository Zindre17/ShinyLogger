package com.aarstrand.zindre.pokechecklist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonHolder>{


    public static class PokemonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView pokemon_name;
        private TextView pokemon_number;
        private ImageButton pokeball;
        private ImageView thumbnail;
        public PokemonHolder(View itemView) {
            super(itemView);
            pokemon_name = (TextView) itemView.findViewById(R.id.pokemon_name);
            pokemon_number = (TextView) itemView.findViewById(R.id.pokemon_number);
            pokeball = (ImageButton) itemView.findViewById(R.id.pokeball);
            thumbnail = (ImageView) itemView.findViewById(R.id.pokemon_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }

    }

    private Context adapterContext;
    private PokeCheckListDbHelper dbHelper;
    private Cursor list;

    public PokemonListAdapter(Context context){
        super();
        adapterContext = context;
        dbHelper = new PokeCheckListDbHelper(context);
        list = dbHelper.getAllPokemon();
    }

    public void resumeDbTransaction() {
        list = dbHelper.getAllPokemon();
    }

    private Context getContext(){
        return adapterContext;
    }

    @Override
    public PokemonHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View pokemonListView = inflater.inflate(R.layout.pokemon_list_item, parent, false);
        return new PokemonHolder(pokemonListView);
    }

    @Override
    public void onBindViewHolder(PokemonHolder pokemonHolder, int position){

        //move the cursor to the correct row
        list.move(position-list.getPosition());

        //retrieve the image from the database and put it in the image view
        ImageView imageView = pokemonHolder.thumbnail;
        imageView.setImageBitmap(convertFromBlobToBitmap(list.getBlob(2)));

        //set the name of the pokemon to one of the textviews
        TextView textView = pokemonHolder.pokemon_name;
        textView.setText(list.getString(1));

        //set the number of the pokemon to the other textview
        TextView textView1 = pokemonHolder.pokemon_number;
        textView1.setText(adapterContext.getResources().getString(R.string.numbersign)+String.valueOf(list.getInt(0)));

        //set the imagebutton based on the boolean(int) "caught" column
        final ImageButton imageButton = pokemonHolder.pokeball;
        if(list.getInt(3)==1){
            imageButton.setImageDrawable(ContextCompat.getDrawable(adapterContext,R.drawable.pokeball));
        }else {
            imageButton.setImageDrawable(ContextCompat.getDrawable(adapterContext,R.drawable.pokeball_gray));
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: open a fragment with the option of adding a pokemon to your caught db. Also update the pokedex db
            }
        });
    }

    private Bitmap convertFromBlobToBitmap(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob,0,blob.length);
    }

    @Override
    public int getItemCount(){
        return list.getCount();
    }

    public void closeDbTransaction(){
        list.close();
        dbHelper.close();
    }

}

