package com.pascalhow.gameorganisor;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by pascalh on 26/10/2015.
 */
public class Court implements Parcelable {
    public static final Parcelable.Creator<Court> CREATOR
            = new Parcelable.Creator<Court>() {
        public Court createFromParcel(Parcel in) {
            return new Court(in);
        }

        public Court[] newArray(int size) {
            return new Court[size];
        }
    };
    private int courtNumber = 0;
    private List<Player> playerList;

    public Court() {

    }

    /**
     * Use when reconstructing Court object from parcel
     * This will be used only by the 'CREATOR'
     *
     * @param in a parcel to read this object
     */
    public Court(Parcel in) {
        this.courtNumber = in.readInt();
        playerList = in.createTypedArrayList(Player.CREATOR);
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
        out.writeInt(this.courtNumber);
        out.writeTypedList(playerList);
    }

    /**
     * This method stores the list of players playing on a particular court
     *
     * @param playerList
     */
    public void addPlayers(List<Player> playerList) {
        this.playerList = playerList;
    }

    /**
     * This method returns the list of players playing on a particular court
     *
     * @return playerArray
     */
    public List<Player> getPlayers() {
        return this.playerList;
    }

    /**
     * This method returns the court number
     *
     * @return courtNumber
     */
    public int getCourtNumber() {
        return this.courtNumber;
    }

    /**
     * This method saves the court number
     *
     * @param number
     */
    public void setCourtNumber(int number) {
        this.courtNumber = number;
    }
}
