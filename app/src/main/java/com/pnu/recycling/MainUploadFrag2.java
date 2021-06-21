package com.pnu.recycling;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class MainUploadFrag2 extends Fragment {
    final int SELECT_IMAGE = 1;
    ArrayList<String> selectedImagesPaths; // Paths of the image(s) selected by the user.
    boolean imagesSelected = false; // Whether the user selected at least an image or not.

    // trash page was change by changeTrashPage
    private ViewFlipper changeTrashPage;

    // Button, layout, imageView
    private ImageButton btn_select_image = null;
    private ImageButton btn_upload_image = null;
    private ImageView imageView = null;
    private TextView trashName = null;
    private TextView responseText = null;
    private LinearLayout slidingLayout = null;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_frag2, container, false);

        // this is for changing recycle method
        changeTrashPage = (ViewFlipper)view.findViewById(R.id.trashPageArray);

        // print selected image at activity
        imageView = view.findViewById(R.id.imageView);
        slidingLayout = (LinearLayout) view.findViewById(R.id.drawer);

        // used in sliding layout
        trashName = (TextView) view.findViewById(R.id.responseTrash);
        responseText = view.findViewById(R.id.responseText);

        // select image button
        btn_select_image = view.findViewById(R.id.btnSelectIMG);
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View view) {
                trashName.setText("How to Recycle Trash Safely");
                responseText.setText("");
                changeTrashPage.setDisplayedChild(0);
                selectImage(view);
            }
        });

        // upload image button
        btn_upload_image = view.findViewById(R.id.btnUploadImage);
        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectServer(view);
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void connectServer(View v) {
        if (!imagesSelected) {
            responseText.setText("업로드하기 위한 이미지를 선택해주세요.");
            return;
        }
        responseText.setText("이미지 업로드 중");

        // postUrl = gcp server's address
        String postUrl = "http://000.000.000.000:000/";

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < selectedImagesPaths.size(); i++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                // Read BitMap by file path.
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagesPaths.get(i), options);
                // resize image into small (224*224)
                bitmap = bitmap.createScaledBitmap(bitmap, 224, 224, true);
                // compress the image by 70%
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            }catch(Exception e){
                responseText.setText("선택 되어진 파일은 이미지가 아닙니다.");
                return;
            }
            byte[] byteArray = stream.toByteArray();

            // set name of image file
            multipartBodyBuilder.addFormDataPart("image" + i, "Android_Flask_" + i + ".jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray));
        }

        RequestBody postBodyImage = multipartBodyBuilder.build();
        postRequest(postUrl, postBodyImage);
    }

    // part of sending the image to server
    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                Log.d("FAIL", e.getMessage());

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                getActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        TextView responseText = view.findViewById(R.id.responseText);
                        responseText.setText("GCP 서버의 전원이 꺼져 있습니다.");
                        Toast.makeText(getActivity(), "실험이 끝나고, 서버 유지 비용으로 인하여 인식 서버를 제거하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            // Part of response
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        TextView responseText = view.findViewById(R.id.responseText);
                        try {
                            String trash = response.body().string();
                            responseText.setText("");
                            trashName.setText("↑↑↑ "+ trash +" ↑↑↑");
                            if (trash.equals("캔")) {
                                changeTrashPage.setDisplayedChild(1);
                            }
                            else if (trash.equals("계란껍질")) {
                                changeTrashPage.setDisplayedChild(2);
                            }
                            else if (trash.equals("비닐봉지")) {
                                changeTrashPage.setDisplayedChild(3);
                            }
                            else if (trash.equals("페트병")) {
                                changeTrashPage.setDisplayedChild(4);
                            }
                            else { // Medicine 약인 경우
                                changeTrashPage.setDisplayedChild(5);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    // select image method
    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    // check whether image is really checked or not
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && null != data) {
                // When a single image is selected.
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);

                String currentImagePath;
                selectedImagesPaths = new ArrayList<>();
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    currentImagePath = getPath(getActivity(), uri);
                    Log.d("ImageDetails", "Single Image URI : " + uri);
                    Log.d("ImageDetails", "Single Image Path : " + currentImagePath);
                    selectedImagesPaths.add(currentImagePath);
                    imagesSelected = true;
                }
            } else {
                Toast.makeText(getActivity(), "이미지가 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "이미지 선택 파트에 오류가 생겼습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // Implementation of the getPath() method and all its requirements is taken from the StackOverflow Paul Burke's answer: https://stackoverflow.com/a/20559175/5426539
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
