package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.widget.ArrayAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.widget.TextView;
import android.view.ViewGroup;

/**
 * class for managing single user UI representation
 */
class UserAdapter extends ArrayAdapter<Object> {
    /*
     * local resource UserAdapter
     */
    private int localResource;

    /*
     * local context for UserAdapter
     */
    private Context localContext;

    /**
     * Default constructor for the UserAdapter
     *
     * @param cont   context for creating new user adapter
     * @param res    id for localResource of user
     * @param scores collection of scores of a single user
     */
    UserAdapter(Context cont, int res, ArrayList scores) {
        super(cont, res, scores);
        localContext = cont;
        localResource = res;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String userName = "default";
        String userScore = "no score";
        Score s = ((Score)getItem(position));
        userName = s.getOwner().getName() + ", " +
                s.getGameName();
        if(s.hasComplexity()){
            String complexityString = Integer.toString(s.getGameComplexity()) + "x" +
                    Integer.toString(s.getGameComplexity());
            userName += ", complexity: " + complexityString;
        }
        userScore = "Score: " + s.toString();

        LayoutInflater layoutModifier = LayoutInflater.from(localContext);
        convertView = layoutModifier.inflate(localResource, parent, false);
        TextView usernameText = (TextView) convertView.findViewById(R.id.userNameText);
        usernameText.setText(userName);
        TextView userScoreText = (TextView) convertView.findViewById(R.id.userScoreText);
        userScoreText.setText(userScore);
        return convertView;
    }
}