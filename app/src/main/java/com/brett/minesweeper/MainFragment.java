package com.brett.minesweeper;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Brett on 15/10/21.
 */
public class MainFragment extends Fragment {

    //===================FIELDS====================
    public Board mBoard;
    public TileAdapter mAdapter;
    public TextView mTextViewResult;

    //===================METHODS====================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mainfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.newGame:
                newGame();
                return true;
            case R.id.validate:
                validate();
                return true;
            case R.id.cheat:
                cheat();
                return true;
            case R.id.intro:
                startActivity(new Intent(getActivity(), TutorialActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTextViewResult = (TextView)view.findViewById(R.id.textview_result);
        setResult(Board.GameStatus.PLAYING);

        mBoard = new Board();

        List<Tile> tiles = mBoard.getTiles();

        mAdapter = new TileAdapter(getActivity(), tiles);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) view.findViewById(R.id.gridview_tile);
        gridView.setAdapter(mAdapter);

        return view;
    }

    public Board getBoard(){
        return mBoard;
    }

    public TileAdapter getAdapter() {
        return mAdapter;
    }

    public void newGame() {
        // initailize tiles
        mBoard.newGame();
        mBoard.setGameStatus(Board.GameStatus.PLAYING);
        setResult(Board.GameStatus.PLAYING);
        mAdapter.notifyDataSetChanged();
    }

    public void validate() {
        // validate
        boolean result = mBoard.validate();
        if(result){
            Toast.makeText(getActivity(), "Cong! You have won", Toast.LENGTH_SHORT).show();
            setResult(Board.GameStatus.WIN);
        }
        else{
            Toast.makeText(getActivity(), "You have lost", Toast.LENGTH_SHORT).show();
            setResult(Board.GameStatus.LOST);
        }
    }

    public void cheat() {
        // cheat
        if(mBoard.getStatus() != Board.GameStatus.PLAYING) return;
        mBoard.cheat();
        mAdapter.notifyDataSetChanged();
    }

    public void setResult(Board.GameStatus status){
        if (status == Board.GameStatus.WIN){
            mTextViewResult.setText("Cong! You have won");
        }
        else if (status == Board.GameStatus.LOST){
            mTextViewResult.setText("You have lost");
        }
        else{
            mTextViewResult.setText("Playing...");
        }
    }
}
