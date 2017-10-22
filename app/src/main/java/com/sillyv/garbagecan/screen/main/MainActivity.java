package com.sillyv.garbagecan.screen.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sillyv.garbagecan.R;
import com.sillyv.garbagecan.screen.navigation.Navigator;
import com.sillyv.garbagecan.util.RxDialog;

public class MainActivity
        extends AppCompatActivity
        implements MainContract.View {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainContract.Presenter presenter = new MainPresenter(this);
        Navigator.getInstance().attach(getSupportFragmentManager(), R.id.container);
        presenter.init(this, savedInstanceState);

    }

    public void displayCamera() {
        Navigator.getInstance().openCamera(this);
    }

    @Override
    public void displayApology() {
        RxDialog.newBuilder(this)
                .withTitle("Permissions denied")
                .withMessage(
                        "All permissions are required to use this app, we are sorry for the inconvenience.")
                .withPositiveMessage("Goodbye")
                .build()
                .show()
                .doOnEvent((aBoolean, throwable) -> finish())
                .subscribe();
    }

    @Override
    public void onBackPressed() {
        Navigator.getInstance().onBackPressed(this);
    }
}
