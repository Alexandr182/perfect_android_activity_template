package com.awesome.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.awesome.R;
import com.awesome.db.RealmHelper;
import com.awesome.presenters.LoginPresenter;
import com.awesome.views.LoginView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private static final String TAG = "LoginActivity";

    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mLoginBT;
    private ImageView[] mPublicProfilesIVs;

    private RealmHelper mRealmHelper;
    private LoginPresenter mLoginPresenter;
    private TextWatcher mInputWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpData();
        setUpViews();
        loadData();
    }

    private void setUpData() {
        mRealmHelper = RealmHelper.getInstance();
        mLoginPresenter = new LoginPresenter();
        mPublicProfilesIVs = new ImageView[3];
        mPublicProfilesIVs[0] = (ImageView) findViewById(R.id.al_iv_profile1);
        mPublicProfilesIVs[1] = (ImageView) findViewById(R.id.al_iv_profile2);
        mPublicProfilesIVs[2] = (ImageView) findViewById(R.id.al_iv_profile3);
        mUsernameET = (EditText) findViewById(R.id.al_et_username);
        mPasswordET = (EditText) findViewById(R.id.al_et_password);
        mLoginBT = (Button) findViewById(R.id.al_bt_login);
        setUpInputWatcher();
    }

    private void setUpInputWatcher() {
        mInputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mLoginBT.setEnabled(checkIfFieldsAreValid());
            }
        };
    }

    private boolean checkIfFieldsAreValid() {
        return !TextUtils.isEmpty(mUsernameET.getText()) && !TextUtils.isEmpty(mPasswordET.getText());
    }

    private void setUpViews() {
        mPasswordET.addTextChangedListener(mInputWatcher);
        setUpUsernameET();
        setUpLoginBT();
    }

    private void setUpUsernameET() {
        String lastLoggedUsername = mRealmHelper.getLastLoggedUsername();
        mUsernameET.setText(lastLoggedUsername != null ? lastLoggedUsername : "");
        mUsernameET.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mUsernameET.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    private void setUpLoginBT() {
        mLoginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = mUsernameET.getText().toString();
        String password = mPasswordET.getText().toString();
        mLoginPresenter.performLogin(username, password, this);
    }

    private void setUpPublicUsersPhotos(List<String> photosUrls) {
        //GET first 3 photos and show them in image views
        int count = Math.min(3, photosUrls.size());
        for (int i = 0; i < count; i++) {
            Glide.with(this)
                    .load(photosUrls.get(i))
                    .into(mPublicProfilesIVs[i]);
        }
    }

    private void loadData() {
        mLoginPresenter.getPublicProfiles(this);
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.cancelLastOperation();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: give this method some funny purpose
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoggedInSuccessfully(String userId) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.USER_ID_KEY, userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPublicProfilesLoaded(ArrayList<String> photoUrls) {
        setUpPublicUsersPhotos(photoUrls);
    }

    private class UselessDataHolder {
        int id;
        String data;
    }

    public interface UselessInterface {

    }
}
