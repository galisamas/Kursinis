package lt.vu.mif.www.kursinis;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

public class FormActivity extends AppCompatActivity{

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.form_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Intent intent = getIntent();
//        Bundle extras = new Bundle();

//        extras.putString(Constants.qr_result,intent.getStringExtra(Constants.qr_result));
        Fragment fragment = new FormActivityFragment();
//        fragment.setArguments(extras);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment).commit();

    }
}
