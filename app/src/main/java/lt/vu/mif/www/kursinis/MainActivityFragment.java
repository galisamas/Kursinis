package lt.vu.mif.www.kursinis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MainActivityFragment extends Fragment implements View.OnClickListener{

    Button lank, klaus;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        lank = (Button) v.findViewById(R.id.lankomumas);
        lank.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
//        openFormActivity("dada");
        if (v.getId() == R.id.lankomumas && isNetworkAvailable())
            if (checkUniqueCode()) {
                scanQR();
            } else {
                Snackbar.make(v, getString(R.string.alreadyInClass), Snackbar.LENGTH_LONG).show();
            }
        else
            Snackbar.make(v, getString(R.string.internetConn), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK)
            openFormActivity(data.getStringExtra("SCAN_RESULT"));
    }

    private void scanQR() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    private void openFormActivity(String contents) {
        Intent intent = new Intent(getActivity(), FormActivity.class);
        intent.putExtra(Constants.qr_result, contents);
        startActivity(intent);
    }

    private boolean checkUniqueCode(){
        String uniqueC = UniqueCodeGenerator.generate(getActivity());
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.table_name);
        query.whereEqualTo(Constants.unique_code, uniqueC);
        try {
            query.getFirst();
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
