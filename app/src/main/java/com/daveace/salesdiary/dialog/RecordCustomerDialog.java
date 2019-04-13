package com.daveace.salesdiary.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.SubCollectionPath;
import com.daveace.salesdiary.customview.SignatureDrawer;
import com.daveace.salesdiary.entity.Customer;
import com.daveace.salesdiary.store.FireStoreHelper;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import butterknife.BindView;

import static com.daveace.salesdiary.interfaces.Constant.CUSTOMERS;
import static com.daveace.salesdiary.interfaces.Constant.USERS;
import static com.daveace.salesdiary.util.StringUtil.fieldsAreValid;

public class RecordCustomerDialog extends BaseDialog {

    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.customerName)
    AppCompatEditText customerNameInput;
    @BindView(R.id.email)
    AppCompatEditText emailInput;
    @BindView(R.id.company)
    AppCompatEditText companyEditText;
    @BindView(R.id.phone)
    AppCompatEditText phoneInput;

    @BindView(R.id.clearSignature)
    AppCompatImageButton clearButton;
    @BindView(R.id.signature)
    SignatureDrawer signatureDrawer;
    @BindView(R.id.done)
    AppCompatButton doneButton;
    private FirebaseAuth fbAuth;
    private OnDoneClickListener onDoneClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        fbAuth = FirebaseAuth.getInstance();
        initUI();
        return view;
    }

    private void initUI() {
        signatureDrawer.setOnTouchEndListener(ended -> {
            if (!ended)
                clearButton.setVisibility(View.VISIBLE);
        });

        clearButton.setOnClickListener(button -> clearSignature());
        doneButton.setOnClickListener(button -> saveCustomer());
    }

    @Override
    int getLayout() {
        return R.layout.dialog_record_customer;
    }

    private void clearSignature() {
        signatureDrawer.clear();
        clearButton.setVisibility(View.INVISIBLE);
    }

    private void saveCustomer() {
        if (!fieldsAreValid(getActivity(), customerNameInput,
                emailInput, companyEditText, phoneInput))
            return;

        Customer customer = Customer.getInstance(
                customerNameInput.getText().toString(),
                emailInput.getText().toString(),
                phoneInput.getText().toString(),
                companyEditText.getText().toString()
        );

        String signaturePath = signatureDrawer.save();
        customer.setSignaturePath(signaturePath);
        String userId = fbAuth.getCurrentUser().getUid();
        SubCollectionPath metaData = new SubCollectionPath(
                USERS,
                userId,
                CUSTOMERS,
                customer.getId(),
                customer);

        FireStoreHelper.getInstance().addDocumentToSubCollection(metaData, rootView);
        onDoneClickListener.passCustomerId(customer.getId());
        dismiss();
    }

    public void setOnDoneClickListener(OnDoneClickListener onDoneClickListener) {
        this.onDoneClickListener = onDoneClickListener;
    }

    public interface OnDoneClickListener {
        void passCustomerId(String id);
    }

}