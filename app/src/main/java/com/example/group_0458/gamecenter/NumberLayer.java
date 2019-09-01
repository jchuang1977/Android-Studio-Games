package com.example.group_0458.gamecenter;

import java.io.Serializable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Number layer
 */
class NumberLayer implements Serializable{

    /**
     * list of numbers
     */
    private List<NumEntry> numbers;

    /**
     * constructor for the number layer
     *
     * @param givenNumbers given numbers
     */
    NumberLayer(List<NumEntry> givenNumbers){
        this.numbers = givenNumbers;
    }

    /**
     * Return number of elements in the number layer
     *
     * @return number of elements in the number layer
     */
    int getSize(){
        return this.numbers.size();
    }

    /**
     * Return values in number layer
     *
     * @return values in number layer
     */
    int[] getValues(){
        int[] result = new int[numbers.size()];
        for(int i = 0; i < numbers.size(); i++){
            result[i] = numbers.get(i).getValue();
        }
        return result;
    }

    /**
     * Return drawables that correspond to elements in number layer
     *
     * @return drawables corresponding to elements in number layer
     */
    int[] getDrawables(){
        int[] result = new int[numbers.size()];
        for(int i = 0; i < numbers.size(); i++){
            result[i] = numbers.get(i).getBackground();
        }
        return result;
    }

    /**
     * Default constructor
     */
    NumberLayer(){

    }

    /**
     * Return whether number layer has empty slots
     *
     * @return whether number layer has empty slots
     */
    boolean hasEmpty(){
        for(NumEntry n: numbers){
            if(n.isEmpty()){
                return true;
            }
        }
        return false;
    }

    /**
     * Return whether entry 1 and entry 2 can be concatenated
     *
     * @param num1 first given entry
     * @param num2 second given entry
     * @return whether entry 1 and entry 2 can be concatenated
     */
    private boolean canConcatenate(NumEntry num1, NumEntry num2){
        if(num1.getValue() == num2.getValue() && num1.getValue() >= 5){
            return true;
        }
        else if(num1.getValue() == 2 && num2.getValue() == 3){
            return true;
        }
        else if(num1.getValue() == 3 && num2.getValue() == 2){
            return true;
        }
        return false;
    }

    /**
     * Shift all the values left while concatenating compatible values
     */
    void concatLeft(){
        for(int i = 0; i < numbers.size() - 1; i++){
            NumEntry entry = numbers.get(i);
            NumEntry entry2 = numbers.get(i + 1);
            if(entry.getIsNew()){
                entry.setIsNew(false);
            }
            if(!entry.isEmpty() && !entry2.isEmpty() && canConcatenate(entry, entry2)){
                entry.setValue(entry.getValue() + entry2.getValue());
                if(entry.getValue() == 0){
                    entry.setEmptiness(true);
                }
                else{
                    entry.setEmptiness(false);
                }
                entry2.setEmptiness(true);
            }
            else if(entry.isEmpty() && !entry2.isEmpty()){
                entry.setValue(entry2.getValue());
                entry.setEmptiness(false);
                entry2.setEmptiness(true);
            }
        }
        if(numbers.get(numbers.size() - 1).getIsNew()){
            numbers.get(numbers.size() - 1).setIsNew(false);
        }
    }

    /**
     * Shift all the values right while concatenating compatible values
     */
    void concatRight(){
        for(int i = numbers.size() - 1; i > 0; i--){
            NumEntry entry = numbers.get(i);
            NumEntry entry2 = numbers.get(i - 1);

            if(entry.getIsNew()){
                entry.setIsNew(false);
            }
            if(!entry.isEmpty() && !entry2.isEmpty() && canConcatenate(entry, entry2)){
                entry.setValue(entry.getValue() + entry2.getValue());
                if(entry.getValue() == 0){
                    entry.setEmptiness(true);
                }
                else{
                    entry.setEmptiness(false);
                }
                entry2.setEmptiness(true);
            }
            else if(entry.isEmpty() && !entry2.isEmpty()){
                entry.setValue(entry2.getValue());
                entry.setEmptiness(false);
                entry2.setEmptiness(true);
            }
        }
        if(numbers.get(0).getIsNew()){
            numbers.get(0).setIsNew(false);
        }
    }

    NumEntry getNumber(int position){
        if(position >= 0 && position < numbers.size()){
            return numbers.get(position);
        }
        return null;
    }

    /**
     * Add new number to number layer
     *
     * @return whether number was added to number layer
     */
    boolean addNewNumber(){
        ArrayList<NumEntry> validNums = new ArrayList<>();
        for(int i = 0; i < numbers.size(); i++){
            if(numbers.get(i).isEmpty()){
                validNums.add(numbers.get(i));
            }
        }
        if(validNums.size() > 0) {
            validNums.get(new Random().nextInt(validNums.size())).setRandomValue();
            return true;
        }
        return false;
    }

    /**
     * Return entry at position
     *
     * @param position position of needed entry
     * @return entry at given position
     */
    NumEntry getEntry(int position){
        if(position >= 0 && position < numbers.size()){
            return numbers.get(position);
        }
        return null;
    }

    /**
     * Return data contained in number entries of this layer
     *
     * @return data from number entries of this layer
     */
    List<SimpleImmutableEntry<Integer, Boolean>> getData(){
        ArrayList<SimpleImmutableEntry<Integer, Boolean>> result = new ArrayList<
                SimpleImmutableEntry<Integer, Boolean>>();
        for(int i = 0; i < numbers.size(); i++){
            SimpleImmutableEntry<Integer, Boolean> newEntry = new
                    SimpleImmutableEntry<Integer, Boolean>(numbers.get(i).getValue(),
                    numbers.get(i).isEmpty());
            result.add(newEntry);
        }
        return result;
    }

    /**
     * Return greatest number from the number layer
     *
     * @return greatest number from the number layer
     */
    int biggestNum(){
        if(numbers.size() > 0) {
            int biggest = numbers.get(0).getValue();
            for (int i = 1; i < numbers.size(); i++) {
                if(numbers.get(i).getValue() > biggest){
                    biggest = numbers.get(i).getValue();
                }
            }
            return biggest;
        }
        return 0;
    }
}