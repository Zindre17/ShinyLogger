package com.aarstrand.zindre.pokechecklist.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.aarstrand.zindre.pokechecklist.R;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListContract;
import com.aarstrand.zindre.pokechecklist.db.PokeCheckListDbHelper;
import com.aarstrand.zindre.pokechecklist.tools.Tools;

public class GridViewAdapter extends BaseAdapter {
    private PokeCheckListDbHelper dbHelper;
    private Cursor caught;
    private Context context;

    public GridViewAdapter(Context context) {
        dbHelper = PokeCheckListDbHelper.getInstance(context);
        caught = dbHelper.getCaughtPokemon();
        this.context = context;
    }

    @Override
    public int getCount() {
        return caught.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        caught.moveToPosition(position);
        return caught.getInt(caught.getColumnIndex(PokeCheckListContract.Catch._ID));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridview;

        if(convertView==null){
            gridview = new View(context);
            gridview = inflater.inflate(R.layout.gridview_item,parent,false);

            TextView encounters = (TextView)gridview.findViewById(R.id.gridview_encounters);
            TextView method = (TextView)gridview.findViewById(R.id.gridview_method);
            ImageView img = (ImageView)gridview.findViewById(R.id.gridview_img);

            caught.moveToPosition(position);
            img.setImageBitmap(dbHelper.getPokemonImage(caught.getInt(caught.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_NUMBER))));
            encounters.setText(String.valueOf(caught.getInt(caught.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_ATTEMPTS))));
            System.out.println(caught.getString(caught.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_METHOD)));
            method.setText(caught.getString(caught.getColumnIndex(PokeCheckListContract.Catch.COLOUMN_NAME_METHOD)));

        }else{
            gridview = convertView;
        }
        return gridview;
    }
}
