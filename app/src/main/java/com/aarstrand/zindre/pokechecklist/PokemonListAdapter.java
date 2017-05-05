package com.aarstrand.zindre.pokechecklist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Zindre on 25-Dec-16.
 */
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
    private Bitmap grid;
    private int size;
    private Bitmap pokemonThumbnail;

    public PokemonListAdapter(Context context){
        super();
        adapterContext = context;
        dbHelper = new PokeCheckListDbHelper(context);
        list = dbHelper.getAllPokemon();
        grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen1);
        size = grid.getWidth()/10;

        //caught = dbHelper.getCaughtPokemon();
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

        list.moveToFirst();
        list.move(position);
        int left = (list.getInt(3)-1)* size;
        int top = (list.getInt(2)-1)*size;
        if(position+1 == 151)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen1);
        if(position+1 == 152)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen2);
        if(position+1 == 251)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen2);
        if(position+1 == 252)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen3);
        if(position+1 == 386)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen3);
        if(position+1 == 387)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen4);
        if(position+1 == 493)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen4);
        if(position+1 == 494)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen5);
        if(position+1 == 649)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen5);
        if(position+1 == 650)
            grid = BitmapFactory.decodeResource(adapterContext.getResources(),R.drawable.gen6);
        pokemonThumbnail = Bitmap.createBitmap(grid,left,top, size,size);
        TextView textView = pokemonHolder.pokemon_name;
        textView.setText(list.getString(1));
        ImageButton imageButton = pokemonHolder.pokeball;
        ImageView imageView = pokemonHolder.thumbnail;
        TextView textView1 = pokemonHolder.pokemon_number;
        textView1.setText(String.valueOf(list.getInt(0)));
        //TODO: fix this: imageView.setImageDrawable(pokemon.getImage());
        imageView.setImageBitmap(pokemonThumbnail);

    }
    @Override
    public int getItemCount(){
        return list.getCount();
    }


}

