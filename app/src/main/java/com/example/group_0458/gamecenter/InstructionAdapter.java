package com.example.group_0458.gamecenter;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

/**
 * class for managing UI representation of instructions
 */
class InstructionAdapter extends ArrayAdapter<Object> {
    /*
     * local resource for InstructionAdapter
     */
    private int localResource;

    /*
     * local context for InstructionAdapter
     */
    private Context localContext;

    /**
     * Default constructor for the InstructionAdapter
     *
     * @param cont   context for creating new user adapter
     * @param res    id for localResource of user
     * @param instructions collection of scores of a single user
     */
    InstructionAdapter(Context cont, int res, ArrayList instructions) {
        super(cont, res, instructions);
        localContext = cont;
        localResource = res;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutModifier = LayoutInflater.from(localContext);
        convertView = layoutModifier.inflate(localResource, parent, false);
        SimpleImmutableEntry<Integer, String> item =
                (SimpleImmutableEntry<Integer, String>)getItem(position);
        ImageView image = (ImageView)convertView.findViewById(R.id.instructionsImage);
        image.setImageResource(item.getKey());
        TextView text = (TextView) convertView.findViewById(R.id.instructionsText);
        text.setText(item.getValue());
        return convertView;
    }
}