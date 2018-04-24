package com.sillyv.garbagecan.screen.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.sillyv.garbagecan.R;
import com.sillyv.garbagecan.data.preferences.Prefs;
import com.sillyv.garbagecan.screen.navigation.Navigator;
import com.sillyv.garbagecan.util.RxDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity
        extends AppCompatActivity
        implements MainContract.View {


    private MainContract.Presenter presenter;

    @NonNull
    private MainPresenter getMainPresenter() {
        return new MainPresenter(
                this,
                Navigator.getInstance(this),
                new RxPermissions(this),
                Prefs.get(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = getMainPresenter();
        presenter.init(savedInstanceState);
    }

    @Override
    public void displayApology() {
        RxDialog.newBuilder(this)
                .withTitle(getString(R.string.permissions_denied))
                .withMessage(getString(R.string.all_permissions_apology))
                .withPositiveMessage(getString(R.string.goodbye))
                .build()
                .show()
                .doOnEvent((aBoolean, throwable) -> finish())
                .subscribe();
    }

    @Override
    public void promptForSignUp(Bundle savedInstanceState) {
        RxDialog.newBuilder(this)
                .withTitle("SignUp")
                .withMessage("Would you like to provide your details so we could notify you of updates?")
                .withPositiveMessage("yes")
                .withNegativeMessage("no")
                .build()
                .show()
                .doOnEvent((aBoolean, throwable) -> presenter.handleSignUpResponse(aBoolean, savedInstanceState))
                .subscribe();

    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed(this);
    }
}
