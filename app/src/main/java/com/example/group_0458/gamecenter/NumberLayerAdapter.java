package com.example.group_0458.gamecenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * class for managing number layer UI representations
 */
class NumberLayerAdapter extends ArrayAdapter<NumberLayer> {
    /*
     * local resource
     */
    private int localResource;

    /*
     * local context
     */
    private Context localContext;

    /*
     * number layer
     */
    private NumberLayer numLayer;

    /*
     * buttons
     */
    private List<Button> numButtons = new ArrayList<>();

    /**
     * Constructor
     *
     * @param cont  context
     * @param res   id for localResource
     * @param numbers array of numbers that have to be drawn
     */
    NumberLayerAdapter(Context cont, int res, NumberLayer[] numbers) {
        super(cont, res, numbers);
        localContext = cont;
        localResource = res;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutModifier = LayoutInflater.from(localContext);
        convertView = layoutModifier.inflate(localResource, parent, false);
        numLayer = getItem(position);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = (Resources.getSystem().getDisplayMetrics().widthPixels - 40)/
                numLayer.getSize();
        params.height = params.width;
        return modifyView(position, convertView, params);
    }

    /**
     * Return modified view
     *
     * @param position given position
     * @param convertView given view
     * @param params given layout parameters
     * @return modified view
     */
    private View modifyView(int position, View convertView, LayoutParams params){
        numLayer = getItem(position);
        int[] drawables = numLayer.getDrawables();
        for(int i = 0; i < numLayer.getSize(); i++) {
            Button newButton = new Button(localContext);
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            linearLayout.addView(newButton, params);
            newButton.setBackgroundResource(drawables[i]);
            NumEntry num = numLayer.getNumber(i);
            if(num != null) {
                if(!num.isEmpty()) {
                    newButton.setText(Integer.toString(num.getValue()));
                    newButton.setTextSize(25f);
                    newButton.setTextColor(Color.WHITE);
                }
                else{
                    newButton.setText("");
                }
            }
        }
        return convertView;
    }
}