package com.pascalhow.gameorganisor;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {

    public int numberOfCourt = 1;
    private int minNumberOfPlayers = 4;
    private List<Player> m_playerList = null;
    private List<Court> m_courtList = null;
    private PlayerArrayAdapter m_adapter;
    private int maxCourts = 10;
    private int minCourts = 1;
    private int numberOfPlayersPerCourt = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_playerList = new ArrayList<>();

        m_adapter = new PlayerArrayAdapter();
        setListAdapter(m_adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Player player = m_adapter.getPlayer(position);

        Toast.makeText(MainActivity.this, player.getPlayerName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is called when the Add button is clicked
     */
    public void btn_addPlayer(View view) {
        TextView newPlayerText = (TextView) findViewById(R.id.newplayer_edittextview);
        String newPlayerName = newPlayerText.getText().toString();

        CheckBox maleCheckBox = (CheckBox) findViewById(R.id.malePlayer_checkBox);
        Gender gender;

        if ((newPlayerName != null) && (!newPlayerName.isEmpty())) {
            if (maleCheckBox.isChecked()) {
                gender = Gender.MALE;
            } else {
                gender = Gender.FEMALE;
            }

            //  Update our list of players
            updatePlayerList(newPlayerName, gender);

            TextView listTitle_textView = (TextView) findViewById(R.id.listTitle_textview);
            listTitle_textView.setText("List of Players (" + m_playerList.size() + ")");

            //  Clear the edit text field
            clearEditText();
        } else {
            Toast.makeText(MainActivity.this, "Player name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method clears the edit text field
     */
    private void clearEditText() {
        EditText newPlayerText = (EditText) findViewById(
                R.id.newplayer_edittextview);
        newPlayerText.setText("");
    }

    /**
     * This method gets called when the Go button is pressed
     */
    public void btn_Go(View view) {
        if (m_playerList.size() < minNumberOfPlayers) {
            Toast.makeText(MainActivity.this, "Minimum 4 Players", Toast.LENGTH_SHORT).show();
        } else {
            TextView numberOfCourts_text = (TextView) findViewById(R.id.numberOfCourts_text_view);

            //  Get the number of courts
            int numberOfCourts = Integer.parseInt(numberOfCourts_text.getText().toString());

            //  Pass the number of courts as input parameter for the new intent
            Intent myIntent = new Intent(MainActivity.this, GameSession.class);
            myIntent.putExtra("numberOfCourts_text", numberOfCourts);
            myIntent.putExtra("ListOfPlayers", (ArrayList<Player>) m_playerList);
            myIntent.putExtra("PlayersOnCourt", (ArrayList<Court>) m_courtList);

            //  We expect to go back and forth to update the list of players
            startActivityForResult(myIntent, 1);
        }
    }

    /**
     * This method is called when the + button is clicked
     */
    public void increment(View view) {
        if (numberOfCourt >= maxCourts) {
            Toast.makeText(getApplicationContext(), "You cannot select more than " + maxCourts + " courts", Toast.LENGTH_SHORT).show();
        }
        if (m_playerList.size() < ((numberOfCourt + 1) * numberOfPlayersPerCourt)) {
            Toast.makeText(this, "Too many courts for number of players", Toast.LENGTH_SHORT).show();
        } else {
            numberOfCourt++;
        }

        TextView numberOfCourtsText = (TextView) findViewById(R.id.numberOfCourts_text_view);
        numberOfCourtsText.setText("" + numberOfCourt);
    }

    /**
     * This method is called when the - button is clicked
     */
    public void decrement(View view) {
        if (numberOfCourt <= minCourts) {
            Toast.makeText(getApplicationContext(), "You cannot select less than " + minCourts + " courts", Toast.LENGTH_SHORT).show();
        } else {
            numberOfCourt--;
        }

        TextView numberOfCourtsText = (TextView) findViewById(R.id.numberOfCourts_text_view);
        numberOfCourtsText.setText("" + numberOfCourt);
    }

    /**
     * This method updates the list of players playing during the session
     */
    private void updatePlayerList(String name, Gender gender) {
        Player player = new Player(name, gender);
        m_playerList.add(player);
        m_adapter.notifyDataSetChanged();
    }

    /**
     * This method toggles between the male checkbox and the female checkbox
     *
     * @param view
     */
    public void chkBox_maleCheckChanged(View view) {
        CheckBox femaleCheckBox = (CheckBox) findViewById(R.id.femalePlayer_checkBox);

        if (femaleCheckBox.isChecked()) {
            femaleCheckBox.setChecked(false);
        }
    }

    /**
     * This method toggles between the male checkbox and the female checkbox
     *
     * @param view
     */
    public void chkBox_femaleCheckChanged(View view) {
        CheckBox maleCheckBox = (CheckBox) findViewById(R.id.malePlayer_checkBox);

        if (maleCheckBox.isChecked()) {
            maleCheckBox.setChecked(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //  Update current list of players as some players are currently playing
                m_playerList = data.getParcelableArrayListExtra("playerList");
                m_courtList = data.getParcelableArrayListExtra("PlayersOnCourt");
            }
        }
    }

    public enum Gender {MALE, FEMALE}

    private class PlayerArrayAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return m_playerList.size();
        }

        @Override
        public Player getItem(int index) {
            return m_playerList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        public Player getPlayer(int index) {
            return m_playerList.get(index);
        }

        @Override
        public View getView(int index, View view, ViewGroup parent) {

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.player_row, null);
            }

            Player player = m_playerList.get(index);

            if (player != null) {
                //  Get the playerName text and the player icon from the player_row layout
                TextView playerName_textView = (TextView) view.findViewById(R.id.playerName);
                ImageView playerImage = (ImageView) view.findViewById(R.id.player_icon);

                if (playerName_textView != null) {
                    //  Set the corresponding name
                    playerName_textView.setText(player.getPlayerName());

                    //  Set the corresponding player icon based on gender
                    if (player.getGender() == Gender.MALE) {
                        playerImage.setImageResource(R.drawable.male);
                    } else {
                        playerImage.setImageResource(R.drawable.female);
                    }
                }
            }

            return view;
        }
    }
}