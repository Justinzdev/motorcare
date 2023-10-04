package com.example.myproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreProfileFragment extends Fragment {

    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Integer mParam3;

    public StoreProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreProfileFragment newInstance(String param1, String param2, Integer param3) {
        StoreProfileFragment fragment = new StoreProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_store_profile, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        String storeNameData = mParam1;
        String storeInfo = mParam2;
        Integer storeCountUse = mParam3;

        TextView storeName = view.findViewById(R.id.storeName);
        TextView storeInfomation = view.findViewById(R.id.storeInfo);
        TextView storeServiceCount = view.findViewById(R.id.serviceCount);
        Button backButton = view.findViewById(R.id.backButton);
        storeName.setText(storeNameData);
        storeInfomation.setText(storeInfo);
        storeServiceCount.setText("มีผู้ใช้บริการแล้ว " + storeCountUse + " ครั้ง");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .hide(StoreProfileFragment.this)
                        .commit();

                ((HomeActivity) requireActivity()).setAllowClickMap(true);
            }
        });
    }
}