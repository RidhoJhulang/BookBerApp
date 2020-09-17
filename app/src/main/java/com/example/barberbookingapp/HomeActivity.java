package com.example.barberbookingapp;

import android.app.AlertDialog;
import androidx.annotation.NonNull;

import com.example.barberbookingapp.Fragments.UserFrgament;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.barberbookingapp.Common.Common;
import com.example.barberbookingapp.Fragments.HomeFragment;
import com.example.barberbookingapp.Fragments.ShoppingFragment;
import com.example.barberbookingapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        if (getIntent() != null)
        {
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN,false);
            if (isLogin)
            {
                dialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null)
                        {
                            DocumentReference currentUser = userRef.document(user.getPhoneNumber());
                            currentUser.get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful())
                                        {
                                            DocumentSnapshot userSnapShot = task.getResult();
                                            if (!userSnapShot.exists())
                                            {
                                                showUpdateDialog(user.getPhoneNumber());
                                            }
                                            else
                                            {
                                                Common.currentUser = userSnapShot.toObject(User.class);
                                                bottomNavigationView.setSelectedItemId(R.id.action_home);
                                            }
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                        }
                                    });
                        }
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if (menuItem.getItemId() == R.id.action_shopping)
                    fragment = new ShoppingFragment();
                else if (menuItem.getItemId() == R.id.action_user)
                    fragment = new UserFrgament();
                return loadFragment(fragment);
            }
        });
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    private void showUpdateDialog(final String phoneNumber) {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setTitle("One more step!");
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information,null);

        Button btn_update = (Button)sheetView.findViewById(R.id.btn_update);
        final TextInputEditText edt_name = (TextInputEditText)sheetView.findViewById(R.id.edt_name);
        final TextInputEditText edt_address = (TextInputEditText)sheetView.findViewById(R.id.edt_address);

        btn_update.setOnClickListener(v -> {

            if (!dialog.isShowing())
                dialog.show();

            final User user = new User(edt_name.getText().toString(),
                    edt_address.getText().toString(),
                    phoneNumber);
            userRef.document(phoneNumber)
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        bottomSheetDialog.dismiss();
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Common.currentUser = user;
                        bottomNavigationView.setSelectedItemId(R.id.action_home);
                        Toast.makeText(HomeActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        bottomSheetDialog.dismiss();
                        Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}
