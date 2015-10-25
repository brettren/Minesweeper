package com.brett.minesweeper;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

/**
 * Created by Brett on 15/10/21.
 */
public class TileAdapter extends ArrayAdapter<Tile> {

    //===================FIELDS====================

    private final Activity mContext;
    private final List<Tile> mTiles;

    //===================CONSTRUCTORS====================

    public TileAdapter(Activity context, List<Tile> tiles) {
        super(context, R.layout.list_item_tile, tiles);
        mContext = context;
        mTiles = tiles;
    }

    //===================OVERRIDEN METHODS====================
    @Override
    public View getView(final int position, View rootView, ViewGroup parent) {
        View view = rootView;

        if (view == null){
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.list_item_tile, parent, false);

            TileHolder holder = new TileHolder();
            holder.btn = (Button) view.findViewById(R.id.list_item_tile);
            view.setTag(holder);
        }

        TileHolder holder = (TileHolder) view.getTag();
        Tile tile = mTiles.get(position);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).onButtonClickListener(position);
            }
        });

        holder.btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MainActivity) mContext).onButtonLongClickListener(position);
                return true;
            }
        });

        switch (tile.getStatus()){
            case FLAG:
                holder.btn.setText("F");
                holder.btn.setTextColor(Color.RED);
                break;
            case UNCOVERED:
                if (tile.isMine()){
                    holder.btn.setText("M");
                }else{
                    holder.btn.setText("" + tile.getAdjacentMinesCount());
                }
                holder.btn.setTextColor(Color.WHITE);
                break;
            case COVERED:
                holder.btn.setText("");
                break;
        }
        return view;
    }

    //===================INNER CLASSES/ LISTENERS/ ENUMS====================
    static class TileHolder{
        Button btn;
    }

    //===================INNER CUSTOMIZE LISTENER===========================
    public interface TileListener {
        public void onButtonClickListener(int position);
        public void onButtonLongClickListener(int position);
    }
}
