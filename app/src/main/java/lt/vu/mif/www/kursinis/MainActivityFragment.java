package lt.vu.mif.www.kursinis;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
//        openFormActivity("labas");
        if (v.getId() == R.id.lankomumas)
            scanQR();
    }

    private void scanQR() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK)
            openFormActivity(data.getStringExtra("SCAN_RESULT"));
    }

    private void openFormActivity(String contents) {
        Intent intent = new Intent(getActivity(), FormActivity.class);
        intent.putExtra(Constants.qr_result, contents);
        startActivity(intent);
    }
}
