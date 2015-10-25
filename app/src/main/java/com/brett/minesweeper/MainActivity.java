package com.brett.minesweeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TileAdapter.TileListener{

    private Board mBoard;
    private MainFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment = new MainFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment)
                    .commit();
        }
    }

    @Override
    public void onButtonClickListener(int position){
        mBoard = mFragment.getBoard();

        if (mBoard == null){
            return;
        }
        // if the status of game is not playing, nothing happen
        if (mBoard.getStatus() == Board.GameStatus.LOST){
            Toast.makeText(this, "You have lost", Toast.LENGTH_SHORT).show();
            mFragment.setResult(Board.GameStatus.LOST);
            return;
        }
        if (mBoard.getStatus() == Board.GameStatus.WIN){
            Toast.makeText(this, "Cong! You have won", Toast.LENGTH_SHORT).show();
            mFragment.setResult(Board.GameStatus.WIN);
            return;
        }

        Tile tile = mBoard.getTiles().get(position);

        switch (tile.getStatus()){
            case COVERED:
                mBoard.flipTile(position);
                mBoard.updateStatus();
                mFragment.getAdapter().notifyDataSetChanged(); // notify adapter to refresh
                break;
            case UNCOVERED:
            case FLAG:
                return;
        }

        if(mBoard.getStatus() == Board.GameStatus.LOST){
            Toast.makeText(this, "You have lost", Toast.LENGTH_SHORT).show();
            mFragment.setResult(Board.GameStatus.LOST);
        }
    }

    @Override
    public void onButtonLongClickListener(int position){
        mBoard = mFragment.getBoard();

        if (mBoard == null){
            return;
        }
        // if the status of game is not playing, nothing happen
        if (mBoard.getStatus() == Board.GameStatus.LOST){
            Toast.makeText(this, "You have lost", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mBoard.getStatus() == Board.GameStatus.WIN){
            Toast.makeText(this, "Cong! You have won", Toast.LENGTH_SHORT).show();
            return;
        }

        Tile tile = mBoard.getTiles().get(position);

        switch (tile.getStatus()){
            case COVERED:
            case FLAG:
                mBoard.setFlag(position);
                mFragment.getAdapter().notifyDataSetChanged(); // notify adapter to refresh
                break;
            case UNCOVERED:
                return;
        }

    }
}
