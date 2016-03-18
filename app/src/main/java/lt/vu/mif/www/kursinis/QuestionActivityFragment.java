package lt.vu.mif.www.kursinis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class QuestionActivityFragment extends Fragment {// TODO: kalusimui: https://parse.com/questions/updating-a-field-without-retrieving-the-object-first

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.question_fragment, container, false);

        return v; // testi nuo 8to
    }
}
