package com.awesome.presenters;

import com.awesome.views.LoginView;

import java.util.ArrayList;

/**
 * Created by profiralexandr on 19/07/16.
 */
public class LoginPresenter {

    public void performLogin(String username, String password, LoginView loginView) {
        loginView.onLoggedInSuccessfully(username);
    }

    public void cancelLastOperation() {
    }

    public void getPublicProfiles(LoginView loginView) {
        loginView.onPublicProfilesLoaded(new ArrayList<String>());
    }
}
