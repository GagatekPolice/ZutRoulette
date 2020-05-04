package zut.roulette.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zut.roulette.R;
import zut.roulette.database.DatabaseHelper;
import zut.roulette.request.GetUserChat;
import zut.roulette.request.PostUser;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private final static String LOGIN_PATTERN = "[a-zA-Z0-9]{3,32}";

    private final static Pattern loginPattern = Pattern.compile(LOGIN_PATTERN);

    private EditText etTypedLogin;
    private Button btnLogin;
    private TextView tvValidityInfo;
    private ProgressBar prbLogin;

    private DatabaseHelper databaseHelper;

    private AsyncTask postUserAsync;

    private Handler postHandler = new Handler();
    private Runnable nextScreenRunner = new Runnable() {
        @Override
        public void run() {
            postHandler.postDelayed(nextScreenRunner, 1000);
            if (String.valueOf(postUserAsync.getStatus()).equals("FINISHED")){
                if (databaseHelper.getChatId() != 0){
                    Log.i("ChatAPI", "FIND USER CHAT ");
                    prbLogin.setVisibility(View.INVISIBLE);
                    postHandler.removeCallbacks(nextScreenRunner);


                    Fragment newFragment = new ChatFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    transaction.replace(R.id.mainFragment, newFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
                } else {
                    Log.i("ChatAPI", "RETRY find chat ");
                    new GetUserChat(databaseHelper).execute();
                }

            }
        }
    };

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



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_fragment_btn_login) {
            hideSoftKeyboard(Objects.requireNonNull(getActivity()),etTypedLogin);
            tvValidityInfo.setText("");
            databaseHelper = new DatabaseHelper(getContext());
            if (prbLogin.isActivated()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.user_is_logged), Toast.LENGTH_LONG).show();
                // ToDo: usunąc to -----
                final String login = String.valueOf(etTypedLogin.getText());

                final boolean isLoginValid = isDataValid(login);

                if (isLoginValid) {
                    prbLogin.setVisibility(View.VISIBLE);

                    postUserAsync = new PostUser(etTypedLogin.getText().toString(), databaseHelper).execute();
                    nextScreenRunner.run();
                // ToDo: usunąc to ------
                }
            } else {
                final String login = String.valueOf(etTypedLogin.getText());

                final boolean isLoginValid = isDataValid(login);

                if (isLoginValid) {
                    prbLogin.setVisibility(View.VISIBLE);

                    postUserAsync = new PostUser(etTypedLogin.getText().toString(), databaseHelper).execute();
                    nextScreenRunner.run();

                }
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
        postHandler.removeCallbacks(nextScreenRunner);
        databaseHelper.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (postUserAsync != null){
            postUserAsync.cancel(true);
        }
        postHandler.removeCallbacks(nextScreenRunner);
    }

    private static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

}
