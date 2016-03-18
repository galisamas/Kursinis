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
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;


public class MainActivityFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button lank, klaus;
    Spinner spinner;
    String uniqueC;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        lank = (Button) v.findViewById(R.id.lankomumas);
        klaus = (Button) v.findViewById(R.id.klausimas);
        lank.setOnClickListener(this);
        klaus.setOnClickListener(this);
        klaus.setEnabled(false);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lectures_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
        uniqueC = UniqueCodeGenerator.generate(getActivity());
        unlockQuestion();
        return v;
    }

    @Override
    public void onClick(View v) {
//        openFormActivity("dada");
        if( v.getId() == R.id.klausimas && isNetworkAvailable()){
            Intent intent = new Intent(getActivity(), QuestionActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.lankomumas && isNetworkAvailable())
            if (checkUniqueCode() == null) { // true
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

    private ParseQuery<ParseObject> checkUniqueCode(){
        String lectureCode = getResources().getStringArray(R.array.lectures_code_array)[spinner.getSelectedItemPosition()];
        String parseTableName = lectureCode + "_" + Constants.dateMocked + Constants.table_pref_l;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(parseTableName);
        query.whereEqualTo(Constants.unique_code, uniqueC);
        try {
            query.getFirst();
            return query;
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unlockQuestion();
    }

    private void unlockQuestion() {
        String lectureCode = getResources().getStringArray(R.array.lectures_code_array)[spinner.getSelectedItemPosition()];
        String parseTableName = lectureCode + "_" + Constants.dateMocked + Constants.table_pref_k;
        try {
            ParseQuery<ParseObject> queryTable = ParseQuery.getQuery(parseTableName);
            queryTable.getFirst();
            ParseQuery<ParseObject> queryStudent = checkUniqueCode();
            if(queryStudent != null){
                queryStudent.whereEqualTo(Constants.unique_code, uniqueC);
                Date date = new Date();
                long timeNow = date.getTime();
                long timeThen = timeNow - (3600 * 1000);   // 20 minutes. Time is in milliseconds
                Date queryDate = new Date();
                queryDate.setTime(timeThen);
                queryStudent.whereGreaterThanOrEqualTo(Constants.created_at, queryDate);
                queryStudent.getFirst();
                klaus.setEnabled(true);
            }
        } catch (ParseException e) {
            System.out.println("There is no class with that name");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
