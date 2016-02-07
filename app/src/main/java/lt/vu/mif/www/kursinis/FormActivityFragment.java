package lt.vu.mif.www.kursinis;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

public class FormActivityFragment extends Fragment implements View.OnClickListener{

    private final String location = "location";
    private final String qRcode = "QRcode";
    private final String snumber = "snumber";
    private final String name = "name";
    private final String surname = "surname";

    EditText nrText, nameText, surnameText;
    Button execButton;

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

    public void executeButton(){
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

    @Override
    public void onClick(View v) {
        if(!nrText.getText().toString().matches(""))
            executeButton();
        else
            Snackbar.make(v, getString(R.string.noStudentNrText), Snackbar.LENGTH_LONG).show();
    }
}
