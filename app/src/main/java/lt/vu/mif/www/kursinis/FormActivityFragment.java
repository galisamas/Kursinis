package lt.vu.mif.www.kursinis;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseObject;

public class FormActivityFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String location = "location";
    private final String qRcode = "QRcode";
    private final String snumber = "snumber";
    private final String name = "name";
    private final String surname = "surname";

    EditText nrText, nameText, surnameText;
    Button execButton;

    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

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
    }

    @Override
    public void onClick(View v) {
        if(!nrText.getText().toString().matches(""))
            executeButton();
        else
            Snackbar.make(v, getString(R.string.noStudentNrText), Snackbar.LENGTH_LONG).show();
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) { // TODO spinduliu kazkokiu
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void executeButton(){
        ParseObject parseObject = new ParseObject(Constants.table_name);
        parseObject.put(Constants.unique_code, UniqueCodeGenerator.generate(getActivity()));
        parseObject.put(location, "bar"); // TODO add location
        parseObject.put(qRcode, getActivity().getIntent().getStringExtra(Constants.qr_result));
        parseObject.put(snumber, nrText.getText().toString());
        parseObject.put(name, nameText.getText().toString());
        parseObject.put(surname, surnameText.getText().toString());
        parseObject.saveInBackground();
        Toast.makeText(getActivity(), getString(R.string.afterLogin), Toast.LENGTH_LONG).show();
        getActivity().finish();
    }
}
