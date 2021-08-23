package uk.co.scribbleapps.accesscaretest.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import uk.co.scribbleapps.accesscaretest.util.PhotoHelper;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = "ProfileViewModelTAG";

    public ProfileViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public Bitmap getImage(String username) {
        PhotoHelper photoHelper = new PhotoHelper();
        String photoFileName = String.format("%s_%s", username, "accessProfilePhoto.jpg");
        File photoFile = photoHelper.getPhotoFile(getApplication(), photoFileName);
        Bitmap takenImage;
        try {
            takenImage = photoHelper.resetImageRotation(getApplication(), photoHelper.getFileProvider(getApplication(), username));
            Log.d(TAG, "resetImageRotation successful");
        } catch (IOException e) {
            // If we were unable to resize and rotate the image, just show it as is
            takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            Log.e(TAG, "resetImageRotation IOException: " + e.getMessage());
        }
        return takenImage;
    }

}