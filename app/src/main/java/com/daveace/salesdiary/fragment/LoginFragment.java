package com.daveace.salesdiary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.PRODUCTS;
import static com.daveace.salesdiary.interfaces.Constant.USERS;
import static com.daveace.salesdiary.util.StringUtil.clear;
import static com.daveace.salesdiary.util.StringUtil.fieldsAreValid;

public class LoginFragment extends BaseFragment {

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.username)
    AppCompatTextView username;
    @BindView(R.id.email)
    TextInputEditText emailInput;
    @BindView(R.id.password)
    TextInputEditText passwordInput;
    @BindView(R.id.logInButton)
    AppCompatButton logInButton;
    @BindView(R.id.sign_up_text)
    TextView signUpText;

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
        return R.layout.fragment_login;
    }

    @Override
    public CharSequence getTitle() {
        return "";
    }

    private void initUI() {
        logInButton.setOnClickListener(view -> logInUser());

        signUpText.setOnClickListener(view ->
                replaceFragment(new SignUpFragment(), false, null)
        );
    }

    private void logInUser() {

        if (!fieldsAreValid(getActivity(), emailInput, passwordInput)) {
            return;
        }
        setLoading(true);
        logInButton.setEnabled(false);
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnFailureListener(error -> {
                    setLoading(false);
                    Snackbar.make(rootView, error.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                })
                .addOnCompleteListener(task -> {
                    setLoading(false);
                    logInButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        Snackbar.make(rootView, getString(R.string.auth_successful), Snackbar.LENGTH_LONG)
                                .show();
                        checkProductCatalog();
                        clear(emailInput, passwordInput);
                    }
                });
    }

    private void checkProductCatalog() {
        String userId = getUserId();
        FireStoreHelper.getInstance().readDocsFromSubCollection(USERS, userId, PRODUCTS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getDocuments().isEmpty())
                            replaceFragment(new InventoryFragment(), false, null);
                        else
                            replaceFragment(new ProductCatalogFragment(), false, null);
                    }
                });
    }
}