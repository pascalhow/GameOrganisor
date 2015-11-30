package com.pascalhow.gameorganisor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by pascalh on 29/10/2015.
 * <p/>
 * The Role of the Game Manager is to take in a list of players
 * Shuffle the incoming list
 * Push them in a queue along with existing players already waiting in the queue
 * The players waiting in the queue are then allocated to available courts
 */
public class GameManager {
    private static BlockingQueue m_queue = new ArrayBlockingQueue(1024);
    private List<Player> playerList = new ArrayList<>();
    private int numberOfPlayersPerCourt = 4;
    private int numberOfFreeCourts = 0;

    /**
     * This is the GameManager constructor
     *
     * @param playerList
     * @param numberOfFreeCourts
     */
    public GameManager(List<Player> playerList, int numberOfFreeCourts) {
        //  PROBLEM IS HERE WITH NUMBER OF FREE COURTS!!!!!
        this.playerList = playerList;
        this.numberOfFreeCourts = numberOfFreeCourts;
    }

    /**
     * This method resets and clears the GameManager
     */
    public void reset() {
        this.playerList.clear();
        this.m_queue.clear();
    }

    /**
     * This method shuffles new players and pushes them to a blocking queue
     * It then allocates the first 4 players from the queue to a court
     */
    public List<Court> organiseNextGame() {

        List<Court> list = new ArrayList<>();

        sendPlayersToQueue(playerList);

        //  What if I have more courts than players???
        for (int i = 0; i < numberOfFreeCourts; i++) {

            //  Submit list of players next on court
            Court nextOnCourt = new Court();
            nextOnCourt.addPlayers(getPlayersFromQueue());

            list.add(nextOnCourt);
        }

        return list;
    }

    /**
     * This method takes in a list of players
     *
     * @param list List of players to be added to the queue
     */
    private void sendPlayersToQueue(List<Player> list) {
        for (int i = 0; i < list.size(); i++) {
            try {
                m_queue.put(list.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method extracts the first 4 players from the queue
     *
     * @return First 4 players from the waiting queue
     */
    private List<Player> getPlayersFromQueue() {
        List<Player> playerList = new ArrayList<>();

        //  Only extract a group of players if there are at least 4 players waiting in the queue
        if (m_queue.remainingCapacity() >= numberOfPlayersPerCourt) {
            for (int i = 0; i < numberOfPlayersPerCourt; i++) {
                try {
                    //  Add the 4 players removed from the queue to the players list next on court
                    playerList.add((Player) m_queue.take());

                    //  Set the player at that index as 'Playing'
                    playerList.get(i).isPlaying = true;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        return playerList;
    }
}
