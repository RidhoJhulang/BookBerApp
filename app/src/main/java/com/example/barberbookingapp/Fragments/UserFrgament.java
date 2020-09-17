package com.example.barberbookingapp.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.barberbookingapp.Common.Common;
import com.example.barberbookingapp.MainActivity;
import com.example.barberbookingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */

public class UserFrgament extends Fragment {

    AlertDialog dialog;
    Unbinder unbinder;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_phone)
    TextView txt_phone;
    @BindView(R.id.sign_out)
    Button signOut;
    @OnClick(R.id.sign_out)
    void SignOut(){
        firebaseSignOut();
    }



    public UserFrgament() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_frgament, container, false);
        unbinder = ButterKnife.bind(this, view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getUserInformation();
        }
        return view;

    }

    private void getUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            txt_name.setText(Common.currentUser.getName());
            txt_phone.setText(Common.currentUser.getPhoneNumber());
        }
    }

    private void firebaseSignOut() {

        androidx.appcompat.app.AlertDialog.Builder confirmDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("Logout!")
                .setMessage("Do you really want to logout?")
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss()).setPositiveButton("OK", (dialogInterface, i) -> Logout(true));
        confirmDialog.show();

    }

    private void Logout(boolean b) {
        FirebaseAuth.getInstance().signOut();
        Intent logout = new Intent(getActivity(), MainActivity.class);
        logout.putExtra(Common.IS_LOGIN, false);
        startActivity(logout);
        getActivity().finish();
    }

}
