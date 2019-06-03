package com.daveace.salesdiary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.alert.ErrorAlert;
import com.daveace.salesdiary.alert.InformationAlert;
import com.daveace.salesdiary.entity.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.USERS;
import static com.daveace.salesdiary.util.StringUtil.clear;
import static com.daveace.salesdiary.util.StringUtil.fieldsAreValid;


public class SignUpFragment extends BaseFragment {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.username)
    TextInputEditText userName;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.signUpButton)
    AppCompatButton signUpButton;
    @BindView(R.id.logInText)
    TextView logInText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }


    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_signup;
    }

    @Override
    public CharSequence getTitle() {
        return "";
    }

    private void initUI() {
        logInText.setOnClickListener(view -> {
            replaceFragment(new LoginFragment(), false, null);
            clear(userName, email, password);
        });

        signUpButton.setOnClickListener(view -> signUpUser());
    }

    private void signUpUser() {
        if (!fieldsAreValid(getActivity(), userName, email, password)) {
            return;
        }
        User user = createUser();
        user.setId(getUserId());
        setLoading(true);
        signUpButton.setEnabled(false);
        getFirebaseAuth()
                .createUserWithEmailAndPassword(
                        user.getEmail(),
                        user.getPassword()
                )
                .addOnFailureListener(error -> {
                    setLoading(false);
                    signUpButton.setEnabled(true);
                    ErrorAlert
                            .Builder()
                            .setContext(getActivity())
                            .setRootView(rootView)
                            .setMessage(error.getMessage())
                            .build()
                            .show();
                })
                .addOnCompleteListener(task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        updateUserProfile(user);
                        addUser(getUserId(), user);
                        InformationAlert
                                .Builder()
                                .setContext(getActivity())
                                .setRootView(rootView)
                                .setMessage(getString(R.string.auth_successful))
                                .build()
                                .show();
                        replaceFragment(new InventoryFragment(), false, null);
                        clear(userName, email, password);
                    }
                });
    }

    private void updateUserProfile(User user) {
        FirebaseUser firebaseUser = getFirebaseAuth().getCurrentUser();
        UserProfileChangeRequest profile = new UserProfileChangeRequest
                .Builder()
                .setDisplayName(user.getName())
                .build();
        firebaseUser.updateProfile(profile)
                .addOnCompleteListener(getActivity(), task -> {
                    final String TAG = "Update User Profile:";
                    if (task.isSuccessful())
                        Log.d(TAG, "User display name updated.");
                    else
                        Log.e(TAG, task.getException().getMessage());
                });
    }

    private User createUser() {
        return new User(
                userName.getText().toString(),
                email.getText().toString(),
                password.getText().toString()
        );
    }

    private void addUser(String userId, User user) {
        user.setId(userId);
        getFireStoreHelper().addDocument(USERS, userId, user);
    }

}
