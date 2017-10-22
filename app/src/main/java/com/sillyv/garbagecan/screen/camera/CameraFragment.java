package com.sillyv.garbagecan.screen.camera;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sillyv.garbagecan.R;
import com.sillyv.garbagecan.data.Repository;
import com.sillyv.garbagecan.util.FilesUtils;
import com.sillyv.garbagecan.util.camera.Camera2BasicFragment;
import com.sillyv.garbagecan.util.camera.CameraOldBasicFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Vasili on 09/11/2016.
 *
 */
public abstract class CameraFragment
        extends Fragment
        implements CameraContract.View {

    static final String SAMSUNG = "samsung";
    @SuppressWarnings("unused")
    private static final String TAG = "SB.CameraPreviewFrag";
    private static final String PREFIX_ARG = "FILE_PREFIX";
    private static final String BACK_CAMERA_ARG = "BACK_CAMERA";
    private static final String MINIMUM_RATIO = "MINIMUM_PREVIEW_RATIO";
    private static final String MAXIMUM_RATIO = "MAXIMUM_PREVIEW_RATIO";
    private static int width;
    private static int height;
    /*
     * This will be prefixed to the filenames of the captured photos, located in the external storage
     * (not the public one), with the date and time, in the jpeg format
     */
    protected String filePrefix;

    /*
     * A boolean flag to determine whether the back-facing camera (true) will be used or the
     * front-facing camera
     */
    protected boolean useCameraBackFacing;

    protected double minimumPreviewRatio;

    protected double maximumPreviewRatio;

    /*
     * This is the output file for the captured picture.
     */
    protected File mFile;

    /*
     * This is a semaphore to make the use of the file object mFile thread-safe
     * acquire this semaphore before saving file using mFile and release it after using the saved file
     */
    protected Semaphore fileUsed = new Semaphore(1, true);

    private Boolean flashOn;
    private int score = 0;
    private CameraContract.Presenter presenter;
    private ProgressBar progressBar;
    private PublishSubject<FileUploadEvent> subject = PublishSubject.create();
    private List<View> buttons;
    private ImageView smallPhotoPreview;
    private View smallPhotoPreviewHint;

    public static CameraFragment newInstance(
            double minPreviewRatio,
            double maxPreviewRatio, int width, int height) {
        CameraFragment.width = width;
        CameraFragment.height = height;
        CameraFragment newFragment;
        if (Build.VERSION.SDK_INT < 21) { //old Camera api
            newFragment = CameraOldBasicFragment.newInstance();
        } else { //new camera2 api
            newFragment = Camera2BasicFragment.newInstance();
        }
        Bundle args = new Bundle();
        args.putString(PREFIX_ARG, "garbageCANPhoto");
        args.putBoolean(BACK_CAMERA_ARG, true);
        args.putDouble(MINIMUM_RATIO, minPreviewRatio);
        args.putDouble(MAXIMUM_RATIO, maxPreviewRatio);
        assert newFragment != null;
        newFragment.setArguments(args);
        return newFragment;
    }

    private void toggleFlash() {
        if (Build.MANUFACTURER.equals(SAMSUNG)) {
            toggleFlashSamsung();
        } else {
            toggleFlashRegular();
        }

    }

    private void toggleFlashRegular() {
        if (flashOn == null) {
            flashOn = true;
        } else if (flashOn) {
            flashOn = false;
        } else {
            flashOn = null;
        }
        setFlashStatus();

    }

    private void toggleFlashSamsung() {
        flashOn = !flashOn;
        setFlashStatus();
    }

    protected void setFlashStatus() {
        if (flashOn == null) {
            setFlashAuto();
        } else if (flashOn) {
            turnFlashOn();
        } else {
            turnFlashOff();
        }
    }

    protected void bindViewElements(View view) {
        bindFlashToggle(view.findViewById(R.id.flash_button));
        bindSettingsButton(view);
        presenter = new CameraPresenter(this, Repository.getInstance(getActivity()));
        presenter.subscribeToEvents();
        View.OnClickListener onClickListener = view1 -> {
            switch (view1.getId()) {
                case R.id.meh_button:
                    score = 1;
                    break;
                case R.id.happy_button:
                    score = 0;
                    break;
                case R.id.sad_button:
                    score = 2;
                    break;
            }
            takePicture();
        };
        view.findViewById(R.id.meh_button).setOnClickListener(onClickListener);
        view.findViewById(R.id.happy_button).setOnClickListener(onClickListener);
        view.findViewById(R.id.sad_button).setOnClickListener(onClickListener);
        progressBar = view.findViewById(R.id.progress_bar);
        buttons = new ArrayList<>();
        buttons.add(view.findViewById(R.id.meh_button));
        buttons.add(view.findViewById(R.id.happy_button));
        buttons.add(view.findViewById(R.id.sad_button));
        smallPhotoPreview = view.findViewById(R.id.small_preview_of_taken_photo);
        smallPhotoPreview.setOnClickListener(view12 -> presenter.navigateToAdditionalInfo());
        smallPhotoPreviewHint = view.findViewById(R.id.small_preview_of_taken_photo_hint);
    }

    private void bindSettingsButton(View view) {
        view.findViewById(R.id.settings_button)
                .setOnClickListener(view1 -> presenter.navigateToSettings());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    protected abstract void setFlashAuto();

    protected abstract void turnFlashOn();

    protected abstract void turnFlashOff();

    @Override
    public boolean isFlashOn() {
        return flashOn;
    }

    protected void bindFlashToggle(ImageView flashToggle) {
        flashOn = false;
        flashToggle.setVisibility(View.VISIBLE);
        if (Build.MANUFACTURER.equals(SAMSUNG)) {
            flashToggle.setOnClickListener(v -> {
                toggleFlashSamsung();
                if (flashOn) {
                    flashToggle.setImageResource(R.drawable.ic_flash_on_yellow);
                } else {
                    flashToggle.setImageResource(R.drawable.ic_flash_off_gray);
                }
            });
        } else {
            flashToggle.setOnClickListener(v -> {
                toggleFlashRegular();
                if (flashOn == null) {
                    flashToggle.setImageResource(R.drawable.ic_flash_auto_blue);
                } else if (flashOn) {
                    flashToggle.setImageResource(R.drawable.ic_flash_on_yellow);
                } else {
                    flashToggle.setImageResource(R.drawable.ic_flash_off_gray);
                }
            });
        }
    }

    /**
     * Create a File for saving an image
     */
    protected File getOutputMediaFile() {
        return FilesUtils.getFile(getContext(), FilesUtils.PICTURES_STRING_BEAN_TEMP, filePrefix);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        filePrefix = (args != null) ? args.getString(PREFIX_ARG) : "";
        useCameraBackFacing = args == null || args.getBoolean(BACK_CAMERA_ARG);
        minimumPreviewRatio = (args != null) ? args.getDouble(MINIMUM_RATIO) : -1;
        maximumPreviewRatio = (args != null) ? args.getDouble(MAXIMUM_RATIO) : -1;
    }

    @Override
    public void onResume() {
        super.onResume();

        initializeCameraPreview();
    }

    protected abstract void initializeCameraPreview();

    protected void notifyPhotoSaved() {
        presenter.notifyPhotoSaved(getContext(), new FileUploadEvent(mFile, score));
//        subject.onNext(new FileUploadEvent(mFile, score));
    }

    @Override
    public Observable<FileUploadEvent> getSavedFile() {
        return subject;
    }

    @Override
    public void displayThankYouDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.thank_you_message)
                    .setPositiveButton(R.string.restart, (dialog, id) -> displayButtons())
                    .setNegativeButton(R.string.exit, (dialog, id) -> getActivity().finish());
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void displayButtons() {
        for (View button : buttons) {
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void notifyImageBeingSent(int happinessFromButton, File file) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(happinessFromButton, android.graphics.PorterDuff.Mode.MULTIPLY);

//        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideButtons() {
        for (View button : buttons) {
            button.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLastPhotoTaken(File file) {
        smallPhotoPreview.setVisibility(View.VISIBLE);
        Picasso.with(getContext())
                .load(file)
                .config(Bitmap.Config.RGB_565)
//                .resize(56, 56)
                .into(smallPhotoPreview, new Callback() {
                    @Override
                    public void onSuccess() {
                        smallPhotoPreviewHint.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

}
