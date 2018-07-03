package com.example.q.example;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LightFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LightFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LightFragment newInstance(String param1, String param2) {
        LightFragment fragment = new LightFragment();
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
    private TabLayout tabLayout;
    private ImageView image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_light, container, false);
        final AnimationDrawable animation = new AnimationDrawable();
        Button startlight = (Button)v.findViewById(R.id.startlight);
        Button stoplight = (Button)v.findViewById(R.id.stoplight);
        startlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.start();
            }
        });
        stoplight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.stop();
            }
        });
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight2), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight3), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight4), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight5), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight6), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight7), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight8), 50);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight), 5);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight2), 5);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight3), 5);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight4), 5);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight5), 5);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight6), 5);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight7), 5);
        animation.addFrame(getResources().getDrawable(R.drawable.glasslight8), 5);
        ImageView imageAnim = (ImageView)v.findViewById(R.id.imagelight);
        imageAnim.setBackgroundDrawable(animation);


        return v;
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
