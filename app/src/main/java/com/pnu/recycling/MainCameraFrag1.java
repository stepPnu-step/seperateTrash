package com.pnu.recycling;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class MainCameraFrag1 extends Fragment {
    // for using error message
    private static final String TAG = "Main Camera Activity";

    // camera Variable
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private ImageButton captureButton;
    private Boolean isCapture = false;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_frag1, container, false);
        // Don't turn off phone's screen during using the phone
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getActivity(), mCamera);
        preview = (FrameLayout) view.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        // Add a listener to the Capture button
        captureButton = view.findViewById(R.id.button_capture);

        captureButton.setOnClickListener(view -> {
            // get an image from the camera
            try {
                // if you don't capture yet.
                if (isCapture == false) {
                    captureButton.setImageResource(R.drawable.ic_back);
                    mCamera.takePicture(null, null, mPicture);
                    isCapture = true;
                }
                // if you capture by camera
                else {
                    captureButton.setImageResource(R.drawable.ic_capture);
                    restartCameraAndPreview();
                    isCapture = false;
                }

            } catch (Exception e) {
                closeCamera();
            }

        });

        return view;
    }

    // restart the camera's preview
    private void restartCameraAndPreview() {
        mCamera.stopPreview();
        preview.removeView(mPreview);
        mCamera.release();
        mCamera = null;
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        preview.addView(mPreview);
    }

    // for making camera instance
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.d(TAG, "Error making camera instance");
        }
        return c; // returns null if camera is unavailable
    }

    // function for setting the camera preview
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private final SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Turn 90 degree Preview
            mCamera.setDisplayOrientation(90);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // get camera info
            Camera.Parameters params = mCamera.getParameters();

            // turn on focus function
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            // end to set camera
            mCamera.setParameters(params);
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
                Log.d(TAG, "카메라 기능 해제");
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            mCamera.startPreview();
        }
    }

    // save the image into phone's internal memory
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                // turn 90 degree and save the image
                Matrix mtx = new Matrix();
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                mtx.postRotate(90);
                Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);

                String outUriStr = MediaStore.Images.Media.insertImage(
                        getActivity().getContentResolver(),
                        rotatedBMP,
                        "Captured Image",
                        "Captured Image using Camera.");

                if (outUriStr == null) {
                    Log.d("SampleCapture", "Image insert failed.");
                    return;
                } else {
                    Uri outUri = Uri.parse(outUriStr);
                    getActivity().sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outUri));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // end the camera
    private void closeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera = null;
            mPreview.surfaceDestroyed(mPreview.getHolder());
            mPreview.getHolder().removeCallback(mPreview);
            mPreview.destroyDrawingCache();
            preview.removeView(mPreview);
        }
    }
}
