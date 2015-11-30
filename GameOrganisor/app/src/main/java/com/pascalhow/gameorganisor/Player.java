package com.pascalhow.gameorganisor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pascalh on 18/09/2015.
 */
public class Player implements Parcelable {

    public static final Parcelable.Creator<Player> CREATOR
            = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
    public boolean isPlaying = false;
    public boolean isInQueue = false;
    private String name = "";
    private int score = 0;
    private int numberOfGamesPlayed = 0;
    private MainActivity.Gender gender;

    public Player(String name, MainActivity.Gender gender) {
        this.name = name;
        this.gender = gender;
    }

    /**
     * Use when reconstructing Player object from parcel
     * This will be used only by the 'CREATOR'
     *
     * @param in a parcel to read this object
     */
    public Player(Parcel in) {
        this.name = in.readString();
        this.score = in.readInt();
        this.isPlaying = (in.readInt() != 0);
        this.numberOfGamesPlayed = in.readInt();

        try {
            gender = MainActivity.Gender.valueOf(in.readString());
        } catch (IllegalArgumentException x) {
            gender = null;
        }
    }

    /**
     * Define the kind of object that you gonna parcel,
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Actual object serialization happens here, Write object content
     * to parcel one by one, reading should be done according to this write order
     *
     * @param out   parcel
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.name);
        out.writeInt(this.score);
        out.writeInt(isPlaying ? 1 : 0);
        out.writeInt(this.numberOfGamesPlayed);
        out.writeString((this.gender == null) ? "" : this.gender.name());
    }

    /**
     * This method returns the player's name
     *
     * @return
     */
    public String getPlayerName() {
        return this.name;
    }

    /**
     * This method sets the player's name
     *
     * @param name
     */
    public void setPlayerName(String name) {
        this.name = name;
    }

    /**
     * This method returns the player's sex
     *
     * @return
     */
    public MainActivity.Gender getGender() {
        return this.gender;
    }

    /**
     * This method sets the player's sex
     *
     * @param gender
     */
    public void setGender(MainActivity.Gender gender) {
        this.gender = gender;
    }

    /**
     * This method returns the number of games played by the player
     *
     * @return
     */
    public int getNumberOfGamesPlayed() {
        return this.numberOfGamesPlayed;
    }

    /**
     * This method sets the number of games the player played
     *
     * @param count
     */
    public void setNumberOfGamesPlayed(int count) {
        this.numberOfGamesPlayed = count;
    }
}
