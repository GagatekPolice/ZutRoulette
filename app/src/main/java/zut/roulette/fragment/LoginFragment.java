package zut.roulette.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import loginflow.app.database.DatabaseHelper;
import zut.roulette.R;
import zut.roulette.request.PostUser;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private final static String LOGIN_PATTERN = "[a-zA-Z1-9]+";

    private final static Pattern loginPattern = Pattern.compile(LOGIN_PATTERN);

    private EditText etTypedLogin;
    private Button btnLogin;
    private TextView tvValidityInfo;
    private ProgressBar prbLogin;

    private AsyncTask postUserAsync;

    public LoginFragment() {
    }


    public static LoginFragment newInstance(String param1, String param2) {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTypedLogin = view.findViewById(R.id.login_fragment_et_login);
        prbLogin = view.findViewById(R.id.login_fragment_progressBar);
        btnLogin = view.findViewById(R.id.login_fragment_btn_login);
        tvValidityInfo = view.findViewById(R.id.login_fragment_tv_validity_status);
        btnLogin.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_fragment_btn_login) {
            tvValidityInfo.setText("");
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

            final String login = String.valueOf(etTypedLogin.getText());


            final boolean isLoginValid = isDataValid(login);


            if (isLoginValid) {
                prbLogin.setVisibility(View.VISIBLE);

                postUserAsync = new PostUser(etTypedLogin.getText().toString(),databaseHelper).execute();


            }
        }
    }

    private boolean isDataValid(String data) {
        Matcher matcher = LoginFragment.loginPattern.matcher(data);
        if (matcher.matches()) {
            return true;
        }
        String validityInfo = tvValidityInfo.getText() + Objects.requireNonNull(getContext()).getString(R.string.invalid_login);
        tvValidityInfo.setText(validityInfo);

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (postUserAsync != null){
            postUserAsync.cancel(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (postUserAsync != null){
            postUserAsync.cancel(true);
        }
    }

}
