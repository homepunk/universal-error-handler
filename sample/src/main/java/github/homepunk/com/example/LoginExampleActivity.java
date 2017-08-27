package github.homepunk.com.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import github.homepunk.com.example.interfaces.LoginExamplePresenter;
import github.homepunk.com.example.interfaces.LoginExampleView;
import github.homepunk.com.universalerrorhandler.HandleManager;
import github.homepunk.com.universalerrorhandler.models.UniversalFieldType;

import static github.homepunk.com.universalerrorhandler.models.UniversalAction.ON_FOCUS_MISS;
import static github.homepunk.com.universalerrorhandler.models.UniversalAction.ON_TEXT_CHANGE;
import static github.homepunk.com.universalerrorhandler.models.UniversalFieldType.EMAIL;
import static github.homepunk.com.universalerrorhandler.models.UniversalFieldType.PASSWORD;

public class LoginExampleActivity extends AppCompatActivity implements LoginExampleView {
    EditText emailEditText;
    EditText passwordEditText;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;

    private LoginExamplePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_example);
        init();

        HandleManager.setFieldsHandleListener((targetType, isSuccess, error) -> {
            if (!isSuccess) {
                switch (targetType) {
                    case EMAIL: {
                        showError(error);
                        break;
                    }
                    case PASSWORD: {
                        showError(error);
                        break;
                    }
                }
            }
        });
        HandleManager.setRequestsHandleListener((requestCode, isSuccess, error) -> {});

        HandleManager.target(emailEditText, EMAIL).handleOnAction(ON_FOCUS_MISS);
        HandleManager.target(passwordEditText, PASSWORD).handleOnAction(ON_TEXT_CHANGE)
                .setOnFailListener(failMessage -> passwordInputLayout.setError(failMessage))
                .setOnSuccessListener(() -> passwordInputLayout.setError(""));
    }

    @Override
    protected void onDestroy() {
        presenter.terminate();
        super.onDestroy();
    }

    public void handleErrors(@UniversalFieldType int errorType) {
        switch (errorType) {
            case EMAIL: {
                emailInputLayout.setError("Email can't be empty");
                break;
            }
            case PASSWORD: {
                passwordInputLayout.setError("Password can't be empty");
                break;
            }
        }
    }


    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private void init() {
        emailEditText = findViewById(R.id.auth_login_email_edit_text);
        passwordEditText = findViewById(R.id.auth_login_password_edit_text);
        emailInputLayout = findViewById(R.id.auth_login_email_input_layout);
        passwordInputLayout = findViewById(R.id.auth_login_password_input_layout);
        presenter = new LoginExampleActivityPresenter();
        presenter.bind(this);
    }
}
