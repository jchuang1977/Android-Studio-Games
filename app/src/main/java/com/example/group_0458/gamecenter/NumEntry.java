package com.example.group_0458.gamecenter;

import java.io.Serializable;
import java.util.Random;

/**
 * Board entry
 */
class NumEntry implements Comparable<NumEntry>, Serializable {

    /**
     * value of the board entry
     */
    private int value = -1;

    /**
     * indicator whether board entry is empty
     */
    private boolean empty = true;

    /**
     * indicator whether board entry is new
     */
    private boolean isNew = false;

    /**
     * Return whether board entry is new
     *
     * @return whether board entry is new
     */
    boolean getIsNew(){
        return this.isNew;
    }

    /**
     * Update indicator of whether board entry is new
     *
     * @param isNew new indicator of whether board entry is new
     */
    void setIsNew(boolean isNew){
        this.isNew = isNew;
    }

    /**
     * array of possible drawables
     */
    private int[] drawables = {R.drawable.num_colour1, R.drawable.num_colour2,
            R.drawable.num_colour3, R.drawable.num_colour4, R.drawable.num_colour5};

    /**
     * Return whether board entry is empty
     *
     * @return whether board entry is empty
     */
    boolean isEmpty(){
        return empty;
    }

    /**
     * Set whether board entry is empty
     *
     * @param emptiness new indicator of whether board entry is empty
     */
    void setEmptiness(boolean emptiness){
        this.empty = emptiness;
    }

    /**
     * Constructor for NumEntry
     *
     * @param nonEmptyLeft number of non-empty board entries left
     */
    NumEntry(int nonEmptyLeft){
        if(nonEmptyLeft > 0){
            value = 2;
            empty = false;
        }
    }

    /**
     * Constructor for NumEntry
     *
     * @param value value of the entry
     * @param empty the emptiness of the entry
     */
    NumEntry(int value, boolean empty){
        this.value = value;
        this.empty = empty;
    }

    /**
     * Set value of NumEntry to a random value from list of possible values
     */
    void setRandomValue(){
        Integer[] possibleValues = {2, 3, 5};
        value = possibleValues[new Random().nextInt(possibleValues.length)];
        empty = false;
        isNew = true;
    }

    /**
     * Set value of NumEntry to given value
     *
     * @param newValue given value
     */
    void setValue(int newValue){
        this.value = newValue;
    }

    /**
     * Return current value
     *
     * @return background identifier
     */
    int getValue(){
        return this.value;
    }

    /**
     * Return background identifier
     *
     * @return background identifier
     */
    int getBackground(){
        if(isNew){
            return drawables[4];
        }
        else if(empty){
            return drawables[0];
        }
        else if(value == 2){
            return drawables[2];
        }
        else if(value == 3){
            return drawables[1];
        }
        return drawables[3];
    }

    @Override
    public int compareTo(NumEntry other){
        if(this.value == other.getValue()){
            return 0;
        }
        else{
            return 1;
        }
    }
}