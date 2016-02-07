package lt.vu.mif.www.kursinis;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;

public class FormActivityFragment extends Fragment implements View.OnClickListener{

    private final String uniqueC = "uniqueCode";
    private final String location = "location";
    private final String qRcode = "QRcode";
    private final String snumber = "snumber";
    private final String name = "name";
    private final String surname = "surname";

    EditText nrText, nameText, surnameText;

    public FormActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.form_fragment, container, false);
        nrText = (EditText) v.findViewById(R.id.nrText);
        nameText = (EditText) v.findViewById(R.id.nameText);
        surnameText = (EditText) v.findViewById(R.id.surnameText);
        return v;
    }

    public void executeButton(){
        ParseObject testObject = new ParseObject("Lankomumas"); // TODO iskelt
        testObject.put(uniqueC, "bar");
        testObject.put(location, "bar"); // TODO add location
        testObject.put(qRcode, getActivity().getIntent().getStringExtra(Constants.qr_result));
        testObject.put(snumber, nrText.getText());
        testObject.put(name, nameText.getText());
        testObject.put(surname, surnameText.getText());
        testObject.saveInBackground();
    }

    @Override
    public void onClick(View v) {
        if(!nrText.getText().toString().matches(""))
            executeButton();
        else
            Snackbar.make(v, getString(R.string.noStudentNrText), Snackbar.LENGTH_LONG).show();
    }
}
