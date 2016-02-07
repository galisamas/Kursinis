package lt.vu.mif.www.kursinis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;

public class FormActivityFragment extends Fragment {


    private final String uniqueC = "uniqueC";
    private final String location = "location";
    private final String qRcode = "QRcode";
    private final String snumber = "snumber";
    private final String name = "name";
    private final String surname = "surname";

    public FormActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.form_fragment, container, false);

        return v;
    }

    public void executeButton(){
        ParseObject testObject = new ParseObject("Lankomumas");
        testObject.put(uniqueC, "bar");
        testObject.put(location, "bar");
        testObject.put(qRcode, "bar");
        testObject.put(snumber, "bar");
        testObject.put(name, "bar");
        testObject.put(surname, "bar");
        testObject.saveInBackground();
    }

}
