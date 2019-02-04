package com.daveace.salesdiary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.entity.User;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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
    private FireStoreHelper fireStoreHelper;
    private FirebaseAuth fbAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        fireStoreHelper = FireStoreHelper.getInstance();
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
        user.setId(fbAuth.getCurrentUser().getUid());
        setLoading(true);
        fbAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnFailureListener(error -> {
                    setLoading(false);
                    Snackbar.make(rootView, error.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                })
                .addOnCompleteListener(task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        updateUserProfile(user);
                        addUser(fbAuth.getCurrentUser().getUid(), user);
                        Snackbar.make(rootView, getString(R.string.auth_successful), Snackbar.LENGTH_LONG)
                                .show();
                        replaceFragment(new InventoryFragment(), false, null);
                        clear(userName, email, password);
                    }
                });
    }

    private void updateUserProfile(User user) {
        FirebaseUser firebaseUser = fbAuth.getCurrentUser();
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
        fireStoreHelper.addDocument(USERS, userId, user);
    }

}
