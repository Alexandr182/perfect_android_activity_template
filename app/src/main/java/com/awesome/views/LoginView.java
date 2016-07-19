package com.awesome.views;

import java.util.ArrayList;

/**
 * Created by profiralexandr on 19/07/16.
 */
public interface LoginView {
    void onLoggedInSuccessfully(String userId);

    void onPublicProfilesLoaded(ArrayList<String> profileIds);
}
