package com.example.group_0458.gamecenter;

/*
Taken from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/CustomAdapter.java

This Class is an overwrite of the Base Adapter class
It is designed to aid setting the button sizes and positions in the GridView
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    /**
     * buttons used by custom adapter
     */
    private List<Button> mButtons = null;

    /**
     * column width and column height for tiles
     */
    private int mColumnWidth, mColumnHeight;

    /**
     * Constructor for Custom adapter
     *
     * @param buttons list of buttons
     * @param columnWidth width used for columns
     * @param columnHeight height used for columns
     */
    CustomAdapter(ArrayList<Button> buttons, int columnWidth, int columnHeight) {
        mButtons = buttons;
        mColumnWidth = columnWidth;
        mColumnHeight = columnHeight;
    }

    @Override
    public int getCount() {
        return mButtons.size();
    }

    @Override
    public Object getItem(int position) {
        return mButtons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            button = mButtons.get(position);
        } else {
            button = (Button) convertView;
        }
        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        button.setLayoutParams(params);
        return button;
    }
}
