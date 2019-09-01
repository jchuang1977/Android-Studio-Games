package com.example.group_0458.gamecenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Stack;

/**
 * Number layer manager
 */
class NumberLayerManager implements Serializable, Undoable{

    /**
     * rows used by number layer manager
     */
    private NumberLayer[] rows;

    /**
     * columns used by number layer manager
     */
    private NumberLayer[] cols;

    /**
     * collection of data used by undo operation
     */
    private Stack<ArrayList<SimpleImmutableEntry<Integer, Boolean>>> previousStates;

    /**
     * name of the game
     */
    private String gameName = "Fives";

    /**
     * Return name of the game
     *
     * @return name of the game
     */
    String getGameName(){
        return this.gameName;
    }

    /**
     * Set name of the game to new value
     *
     * @param newName new value for name of the game
     */
    void setGameName(String newName){
        this.gameName = newName;
    }

    /**
     * Return number of rows
     *
     * @return number of rows
     */
    int getNumRows(){
        return rows.length;
    }

    /**
     * Constructor for NumberLayerManager
     *
     * @param size size of a game board
     */
    NumberLayerManager(int size){
        previousStates = new Stack<ArrayList<SimpleImmutableEntry<Integer, Boolean>>>();
        rows = new NumberLayer[size];
        int nonEmptyLeft = 2;
        for (int i = 0; i < rows.length; i++){
             List<NumEntry> numbers = new ArrayList<NumEntry>();
             for(int j = 0; j < size; j++, numbers.add(new NumEntry(nonEmptyLeft--)));
             rows[i] = new NumberLayer(numbers);
        }
    }

    /**
     * Return number layers
     *
     * @return number layers
     */
    NumberLayer[] getNumberLayers(){
        return this.rows;
    }

    /**
     * Shift all the numbers on the board left
     * @return whether operation was successful
     */
    boolean shiftLeft(){
        addCurrentState();
        for(int i = 0; i < rows.length; i++){
            rows[i].concatLeft();
        }
        if(addEntryToLayer()){
            return true;
        }
        return false;
    }

    /**
     * Add new entry to board
     * @return whether operation was successful
     */
    private boolean addEntryToLayer(){
        ArrayList<NumberLayer> validLayers = new ArrayList<>();
        for(int i = 0; i < rows.length; i++){
            if(rows[i].hasEmpty()){
                validLayers.add(rows[i]);
            }
        }
        if(validLayers.size() > 0){
            validLayers.get(new Random().nextInt(validLayers.size())).addNewNumber();
            return true;
        }
        return false;
    }

    /**
     * Shift all the numbers on the board right
     * @return whether operation was successful
     */
    boolean shiftRight(){
        addCurrentState();
        for(int i = 0; i < rows.length; i++){
            rows[i].concatRight();
        }
        if(addEntryToLayer()){
            return true;
        }
        return false;
    }

    /**
     * Shift all the numbers on the board up
     * @return whether operation was successful
     */
    boolean shiftUp(){
        addCurrentState();
        updateColumns();
        for(int i = 0; i < cols.length; i++){
            cols[i].concatRight();
        }
        if(addEntryToLayer()){
            return true;
        }
        return false;
    }

    /**
     * Shift all the numbers on the board down
     * @return whether operation was successful
     */
    boolean shiftDown(){
        addCurrentState();
        updateColumns();
        for(int i = 0; i < cols.length; i++){
            cols[i].concatLeft();
        }
        if(addEntryToLayer()){
            return true;
        }
        return false;
    }

    /**
     * Add current state to collection of states
     */
    private void addCurrentState(){
        ArrayList<SimpleImmutableEntry<Integer, Boolean>> currentState = new
                ArrayList<SimpleImmutableEntry<Integer, Boolean>>();
        for(int i = 0; i < rows.length; i++) {
            ArrayList<SimpleImmutableEntry<Integer, Boolean>> rowData =
                    (ArrayList<SimpleImmutableEntry<Integer, Boolean>>)rows[i].getData();
            currentState.addAll(rowData);
        }
        previousStates.add(currentState);
    }

    /**
     * Update the list of board columns
     */
    private void updateColumns(){
        if(rows.length > 0) {
            cols = new NumberLayer[rows[0].getSize()];
            for (int j = 0; j < rows[0].getSize(); j++){
                ArrayList<NumEntry> nums = new ArrayList<>();
                for(int i = 0; i < rows.length; i++){
                    nums.add(rows[i].getEntry(j));
                }
                cols[j] = new NumberLayer(nums);
            }
        }
    }

    @Override
    public boolean undo(int times){
        if(hasPrevious(times)) {
            while (times > 0) {
                undo();
                times--;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean undo(){
        if(hasPrevious()) {
            ArrayList<NumberLayer> newCurrentState = new ArrayList<>();
            ArrayList<SimpleImmutableEntry<Integer, Boolean>> previousState = previousStates.pop();
            int dimension = rows[0].getSize();
            int index = 0;
            while (index != previousState.size()) {
                ArrayList<NumEntry> numEntries = new ArrayList<>();
                for (int i = 0; i < dimension; i++) {
                    SimpleImmutableEntry currentEntry = previousState.get(index);
                    numEntries.add(new NumEntry((Integer) currentEntry.getKey(),
                            (Boolean) currentEntry.getValue()));
                    index += 1;
                }
                newCurrentState.add(new NumberLayer(numEntries));
            }
            Object[] objs = newCurrentState.toArray();
            NumberLayer[] newNumLayer = new NumberLayer[objs.length];
            for(int j = 0; j < objs.length; j++){
                newNumLayer[j] = (NumberLayer) objs[j];
            }
            rows = newNumLayer;
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPrevious(){
        return !this.previousStates.isEmpty();
    }

    @Override
    public boolean hasPrevious(int times){
        return this.previousStates.size() >= times;
    }

    /**
     * Return how many moves were made by the player
     *
     * @return the number of moves that was made by the player
     */
    int getNumMoves(){
        return previousStates.size();
    }

    /**
     * Return greatest number from the board
     *
     * @return greatest number from the board
     */
    int getGreatestNumber(){
        if(rows.length > 0) {
            int biggest = rows[0].biggestNum();
            for (int i = 1; i < rows.length; i++) {
                if(rows[i].biggestNum() > biggest){
                    biggest = rows[i].biggestNum();
                }
            }
            return biggest;
        }
        return 0;
    }
}