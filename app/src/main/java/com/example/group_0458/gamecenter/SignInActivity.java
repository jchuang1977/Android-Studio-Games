package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import android.util.TypedValue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;
import java.util.List;

/**
 * Activity for user to sign in/sign up
 */
public class SignInActivity extends AppCompatActivity implements Iterable<User>{

    /**
     * minimum password length requirement
     */
    private final int MINIMUM_PASSWORD_LENGTH = 8;

    /**
     * The list of users
     */
    private List<User> users = new ArrayList<User>();

    /**
     * The current user
     */
    private static User curUser = null;

    /**
     * Return the current user
     *
     * @return current user
     */
    static String getCurrentUserName(){
        return curUser.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if(curUser == null) {
            setupLogInButton();
            setupRegisterButton();
            loadUsers();
        }
    }

    /**
     * Load the lists of users
     */
    private void loadUsers() {
        try {
            InputStream inputStream = this.openFileInput("Accounts.ser");
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                users = (ArrayList<User>) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " +
                    e.toString());
        }
    }

    /**
     * Save the lists of users
     */
    private void saveUsers() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput("Accounts.ser", MODE_PRIVATE));
            outputStream.writeObject(users);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Set up listener for login button
     */
    private void setupLogInButton() {
        Button logInButton = (Button) findViewById(R.id.logInButton);
        TextView infoText = (TextView) findViewById(R.id.welcomeText);
        EditText emailInputField = (EditText) findViewById(R.id.emailInputField);
        EditText passwordInputField = (EditText) findViewById(R.id.passwordInputField);
        logInButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logIn(emailInputField.getText().toString(),
                        passwordInputField.getText().toString())) {
                    String newString = "You're logged in!";
                    infoText.setText(newString);
                    infoText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    Intent newIntent = new Intent(SignInActivity.this,
                            GameSelectionActivity.class);
                    startActivity(newIntent);
                } else {
                    String newString = "Incorrect username or password";
                    infoText.setText(newString);
                    infoText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    createPopupMessage("Login failed", "OK",
                            "Incorrect username or password");
                }
            }
        }
        );
    }

    /**
     * Set up listener for registration button
     */
    private void setupRegisterButton() {
        Button signInButton = (Button) findViewById(R.id.RegisterButton);
        TextView infoText = (TextView) findViewById(R.id.welcomeText);
        EditText emailInputField = (EditText) findViewById(R.id.emailInputField);
        EditText passwordInputField = (EditText) findViewById(R.id.passwordInputField);
        signInButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordInputField.getText().toString().length() >= MINIMUM_PASSWORD_LENGTH &&
                        signUp(emailInputField.getText().toString(),
                                passwordInputField.getText().toString())) {
                    infoText.setText("Your account was successfully created");
                    infoText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    // save users after creating new user
                    saveUsers();
                } else {
                    if (passwordInputField.getText().toString().length() < MINIMUM_PASSWORD_LENGTH) {
                        createPopupMessage("Registration failed", "OK",
                                "Password that you entered is not long enough");
                    } else {
                        infoText.setText("user with name that you entered already exists");
                        infoText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        createPopupMessage("Registration failed", "OK",
                                "User with name that you entered already exists");
                    }
                }
            }
        }
        );
    }

    /**
     * Create popup message
     *
     * @param title tile of popup message
     * @param reply reply option
     * @param message a message
     */
    private void createPopupMessage(String title, String reply, String message) {
        PopupDialog dialog = new PopupDialog();
        dialog.setTitle(title);
        dialog.setReply(reply);
        dialog.setMessage(message);
        dialog.show(getSupportFragmentManager(), message);
    }

    /**
     * Return whether login was successful
     * Try to login with the name and password provided.
     *
     * @param name     the name of the user to log in
     * @param password the password entered
     * @return whether or not login is successful
     */
    private boolean logIn(String name, String password) {
        User userFound = findUser(name);

        if (userFound == null) {
            return false;
        } else {
            if (password.equals(userFound.getPassword())) {
                setCurrentUser(userFound);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Return whether sign up operation was successful
     * Return true when account is set up
     * Return false when there is existing user with the same name
     *
     * @param name     the name of the user
     * @param password the password of the user
     * @return whether sign up is successful
     */
    private boolean signUp(String name, String password) {
        if (findUser(name) == null) {
            User userToAdd = new User(name);
            userToAdd.setPassword(password);
            users.add(userToAdd);
            setCurrentUser(userToAdd);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the user with given name. If not found return null.
     *
     * @param name The name of the user to find
     * @return the user with given name. If not found return null.
     */
    private User findUser(String name) {
        Iterator<User> iter = getIterator();
        User re = null;
        while (iter.hasNext()) {
            User currentUser = iter.next();
            if (currentUser.getName().equals(name)) {
                re = currentUser;
                break;
            }
        }
        return re;
    }

    /**
     * Set the current user to user. Return whether the operation is successful
     *
     * @param user the user to be set as current user
     */
    private void setCurrentUser(User user) {
        curUser = user;
    }

    /**
     * Return the number of users stored
     *
     * @return the number of users stored
     */
    private int numUsers() {
        return users.size();
    }

    /**
     * Return the User at the given index
     *
     * @param index the index of User desired
     * @return User at the given index
     */
    User getUser(int index) {
        return users.get(index);
    }

    /**
     * Return whether or not account manager is done setting up the current user
     *
     * @return whether or not account manager is done setting up the current user
     */
    private boolean isReady() {
        return curUser != null;
    }

    /**
     * Allow client code to access iterator through static method
     *
     * @return iterator that iterates through the users
     */
    Iterator<User> getIterator() {
        return iterator();
    }

    @Override
    @NonNull
    public Iterator<User> iterator() {
        return new AccountIterator();
    }

    /**
     * AccountIterator iterates through the User stored
     */
    private class AccountIterator implements Iterator<User> {
        /**
         * The index of next User to return
         */
        int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex != numUsers();
        }

        @Override
        public User next() {
            User re = users.get(nextIndex);
            nextIndex++;
            return re;
        }
    }
}