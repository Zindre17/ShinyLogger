package com.aarstrand.zindre.pokechecklist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
/**
 * Created by Zindre on 25-Dec-16.
 */
public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pokemon_name;
        public TextView pokemon_number;
        public ImageButton pokeball;
        public ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            pokemon_name = (TextView) itemView.findViewById(R.id.pokemon_name);
            pokemon_number = (TextView) itemView.findViewById(R.id.pokemon_number);
            pokeball = (ImageButton) itemView.findViewById(R.id.pokeball);
            thumbnail = (ImageView) itemView.findViewById(R.id.pokemon_image);
        }
    }
    //TODO: Fix this so it reads the db for data
    private List<PokemonAsListItem> pokemonItems;
    private Context adapterContext;

    public PokemonListAdapter(Context context, List<PokemonAsListItem> pokemons){
        adapterContext = context;
        pokemonItems = pokemons;
    }
    private Context getContext(){
        return adapterContext;
    }
    @Override
    public  PokemonListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View pokemonListView = inflater.inflate(R.layout.pokemon_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(pokemonListView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(PokemonListAdapter.ViewHolder viewHolder, int position){
        PokemonAsListItem pokemon = pokemonItems.get(position);

        TextView textView = viewHolder.pokemon_name;
        textView.setText(pokemon.getName());
        ImageButton imageButton = viewHolder.pokeball;
        ImageView imageView = viewHolder.thumbnail;
        if(pokemon.isCaught()){
            imageButton.setImageResource(R.drawable.pokeball_48);
        }else{
            imageButton.setImageResource(R.drawable.pokeballGray_48);
        }
        TextView textView1 = viewHolder.pokemon_number;
        textView1.setText(pokemon.getNumber());
        //TODO: fix this: imageView.setImageDrawable(pokemon.getImage());

    }
    @Override
    public int getItemCount(){
        return pokemonItems.size();
    }
}

