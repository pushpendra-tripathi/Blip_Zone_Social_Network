package com.starlord.blipzone.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.starlord.blipzone.R;
import com.starlord.blipzone.utils.Permissions;

import java.util.Objects;


public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constant
    private static final int PHOTO_FRAGMENT_NUM = 1;

    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started.");

        Log.d(TAG, "onClick: launching camera.");

        if (((CreatePostActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
            if (((CreatePostActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
                Log.d(TAG, "onClick: starting camera");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launchCameraActivity.launch(cameraIntent);
            } else {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }


        return view;
    }

    private boolean isRootTask() {
        if (((CreatePostActivity) getActivity()).getTask() == 0) {
            return true;
        } else {
            return false;
        }
    }


    ActivityResultLauncher<Intent> launchCameraActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // your operation....
                        Log.d(TAG, "onActivityResult: done taking a photo.");
                        Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");

                        Bitmap bitmap;
                        assert data != null;
                        bitmap = (Bitmap) data.getExtras().get("data");

                        if (isRootTask()) {
                            try {
                                Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
                                Intent intent = new Intent(getActivity(), NextActivity.class);
                                intent.putExtra("selected_bitmap", bitmap);
                                startActivity(intent);
                                requireActivity().finish();
                            } catch (NullPointerException e) {
                                Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
                            }
                        } else {
                            try {
                                Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
                                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                                intent.putExtra("selected_bitmap", bitmap);
                                intent.putExtra("return_to_fragment", "Edit Profile");
                                startActivity(intent);
                                requireActivity().finish();
                            } catch (NullPointerException e) {
                                Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
                            }
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().overridePendingTransition(0, 0);
                    }
                }
            });

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (((CreatePostActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
                Log.d(TAG, "onClick: starting camera");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launchCameraActivity.launch(cameraIntent);
                getActivity().overridePendingTransition(0, 0);
            }
        }

    }
}