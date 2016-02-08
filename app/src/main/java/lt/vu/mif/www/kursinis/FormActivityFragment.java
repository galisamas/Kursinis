package lt.vu.mif.www.kursinis;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FormActivityFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String location = "location";
    private final String qRcode = "QRcode";
    private final String snumber = "snumber";
    private final String name = "name";
    private final String surname = "surname";

    EditText nrText, nameText, surnameText;
    Button execButton;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double latitude;
    private double longitude;
    private String address;

    public FormActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.form_fragment, container, false);
        nrText = (EditText) v.findViewById(R.id.nrText);
        nameText = (EditText) v.findViewById(R.id.nameText);
        surnameText = (EditText) v.findViewById(R.id.surnameText);
        execButton = (Button) v.findViewById(R.id.execButton);
        execButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        if(!nrText.getText().toString().matches(""))
            executeButton();
        else
            Snackbar.make(v, getString(R.string.noStudentNrText), Snackbar.LENGTH_LONG).show();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) { // TODO veikia tik is antro karto prisijungus
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.d("CONNECTION", "ON CONNECTED -> " + (mLastLocation == null));
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                Log.d("LATITUDE", "" + latitude);
                Log.d("LONGITUDE", "" + longitude);

                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("GEOCODER", "FAILED");
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("CONNECTION", "SUSPENDED");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("CONNECTION", "FAILED");
    }

    private void executeButton(){
        ParseObject parseObject = new ParseObject(Constants.table_name);
        parseObject.put(Constants.unique_code, UniqueCodeGenerator.generate(getActivity()));
        parseObject.put(location, address);
        parseObject.put(qRcode, getActivity().getIntent().getStringExtra(Constants.qr_result));
        parseObject.put(snumber, nrText.getText().toString());
        parseObject.put(name, nameText.getText().toString());
        parseObject.put(surname, surnameText.getText().toString());
        parseObject.saveInBackground();
        Toast.makeText(getActivity(), getString(R.string.afterLogin), Toast.LENGTH_LONG).show();
        getActivity().finish();
    }
}
