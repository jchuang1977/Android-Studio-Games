package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;


/**
 * a popup dialog
 */
public class PopupDialog extends AppCompatDialogFragment {

    /**
     * title of popup dialogue
     */
    private String title = "default";

    /**
     * positive reply option of popup dialogue
     */
    private String positiveReply = "OK";

    /**
     * negative reply option of popup dialogue
     */
    private String negativeReply = null;

    /**
     * message of popup dialogue
     */
    private String message = "None";

    /**
     * object for performing actions based on user feedback
     */
    private DialogAction actionObject = null;

    /**
     * setter for title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * setter for positive reply
     */
    void setReply(String reply) {
        this.positiveReply = reply;
    }

    /**
     * setter for negative reply
     */
    void setNegativeReply(String reply){
        this.negativeReply = reply;
    }

    /**
     * setter for message
     */
    void setMessage(String message) {
        this.message = message;
    }

    /**
     * setter for action object
     */
    void setActionObject(DialogAction action){
        this.actionObject = action;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(this.title);
        dialog.setMessage(this.message);
        dialog.setPositiveButton(this.positiveReply, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogueInt, int x) {
                if(actionObject != null){
                    actionObject.positiveAction();
                }
            }

        });
        if(negativeReply != null){
            dialog.setNegativeButton(this.negativeReply, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogueInt, int x) {
                    if(actionObject != null){
                        actionObject.negativeAction();
                    }
                }
            });
        }
        return dialog.create();
    }
}