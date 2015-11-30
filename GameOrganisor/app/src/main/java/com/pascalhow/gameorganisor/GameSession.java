package com.pascalhow.gameorganisor;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GameSession extends ListActivity {

    private int m_numberOfCourts = 0;
    private List<Player> m_playerList = new ArrayList<>();
    private List<Court> m_courtList = new ArrayList<>();
    private GameManager m_gameManager;
    private CourtArrayAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_session);

        LoadSession();
    }

    /**
     * This method loads the session data and initialises:
     * Number of courts
     * Player List - May contain new players
     * Court List Information e.g Which player is playing on a particular court
     */
    private void LoadSession()
    {
        //  Get the data from previous MinActivity intent
        Bundle bundle = getIntent().getExtras();
        this.m_numberOfCourts = bundle.getInt("numberOfCourts_text");
        this.m_playerList = bundle.getParcelableArrayList("ListOfPlayers");
        this.m_courtList = bundle.getParcelableArrayList("PlayersOnCourt");

        //  If it is a new session, court list will be null so do not load anything until GameManager organises a game
        if(m_courtList != null) {
            this.m_adapter = new CourtArrayAdapter();
            setListAdapter(m_adapter);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("playerList", (ArrayList<Player>) m_playerList);
        intent.putExtra("PlayersOnCourt", (ArrayList<Court>) m_courtList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //  View a specific game's result
        Intent myIntent = new Intent(GameSession.this, GameResult.class);
        startActivity(myIntent);
    }

    /**
     * This method is called when the organise button is called
     * @param view
     */
    public void OrganiseGame(View view)
    {
        m_gameManager = new GameManager(m_playerList, m_numberOfCourts);
        m_courtList = m_gameManager.organiseNextGame();

        m_adapter = new CourtArrayAdapter();
        setListAdapter(m_adapter);
    }

    private class CourtArrayAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return m_courtList.size();
        }

        @Override
        public Court getItem(int index) {
            // TODO Auto-generated method stub
            return m_courtList.get(index);
        }

        @Override
        public long getItemId(int index) {
            // TODO Auto-generated method stub
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.current_game_row, null);
            }

            Court court = m_courtList.get(index);

            if (court != null) {
                TextView courtNumber_textView = (TextView) view.findViewById(R.id.courtNumber_textView);

                //  The court index already indicates the court number
                courtNumber_textView.setText("Court " + (index + 1));

                TextView player1 = (TextView) view.findViewById(R.id.player1_textView);
                TextView player2 = (TextView) view.findViewById(R.id.player2_textView);
                TextView player3 = (TextView) view.findViewById(R.id.player3_textView);
                TextView player4 = (TextView) view.findViewById(R.id.player4_textView);

                //  Add the player names to each TextView
                List<Player> playersOnCourt = court.getPlayers();
                player1.setText(playersOnCourt.get(0).getPlayerName());
                player2.setText(playersOnCourt.get(1).getPlayerName());
                player3.setText(playersOnCourt.get(2).getPlayerName());
                player4.setText(playersOnCourt.get(3).getPlayerName());
            }
            return view;
        }
    }
}
