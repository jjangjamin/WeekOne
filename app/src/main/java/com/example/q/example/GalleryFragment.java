package com.example.q.example;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_STREAM;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ImageView imageView;

    private Uri imageUri;
    private String imageFilePath;
    Button imagebutton;
    Button button;
    Button button2;
    TextView cameratext;
    private static final int REQUEST_OPEN_RESULT_CODE = 0;
    private static final int RESULT_LOAD_IMAGE= 1;
    private final int CAMERA_RESULT=1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        imageView = (ImageView)v. findViewById(R.id.imageView);
        imagebutton = (Button)v.findViewById(R.id.imagebutton);
        button = (Button)v.findViewById(R.id.buttoncamera);
        cameratext = (TextView)v.findViewById(R.id.cameratext);
        button2 = (Button)v.findViewById(R.id.button2);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(60);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        cameratext.startAnimation(anim);

        cameratext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameratext.clearAnimation();
                    }
                }
        );
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_OPEN_RESULT_CODE);

            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                    dispatchTakenPictureIntent();
                }
                else{
                    if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                        Toast.makeText(getActivity().getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_RESULT);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(EXTRA_STREAM,imageUri);
                startActivity(Intent.createChooser(shareIntent,"Share Using"));
            }
        });


        imageView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Toast.makeText(getActivity().getApplicationContext(), "ImageView long pressed!", Toast.LENGTH_SHORT).show();
                //zoomImage();
                return true;
            }
        });
        return v;
    }

    private void dispatchTakenPictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_RESULT);
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakenPictureIntent();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        super.onActivityResult(requestCode,resultCode,resultData);
        if(requestCode ==  REQUEST_OPEN_RESULT_CODE&&resultCode == Activity.RESULT_OK){
            Uri uri = null;
            if(resultData!=null){
                uri = resultData.getData();
                imageUri = resultData.getData();
                try {
                    Bitmap bitmap = getBitmapFromUri(uri);
                    imageView.setImageBitmap(bitmap);
                } catch(IOException e){
                    e.printStackTrace();
                }
                /*Glide.with(this)
                        .load(uri)
                        .into(imageView);*/
            }
        }
        if (resultCode == Activity.RESULT_OK&&requestCode==CAMERA_RESULT){
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            /*Bundle extras = resultData.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);*/
            imageView.setImageBitmap(rotate(bitmap,exifDegree));
        }


    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException{
        ParcelFileDescriptor parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
