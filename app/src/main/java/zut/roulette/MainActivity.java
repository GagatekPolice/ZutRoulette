package zut.roulette;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import zut.roulette.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment;

    private FragmentTransaction fragmentTransaction;

    private static final String LOGIN_FRAGMENT = "login_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginFragment = new LoginFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, loginFragment, LOGIN_FRAGMENT);
        fragmentTransaction.commit();
    }
}
