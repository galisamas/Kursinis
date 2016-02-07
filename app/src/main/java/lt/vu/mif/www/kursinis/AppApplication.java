package lt.vu.mif.www.kursinis;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        ParseCrashReporting.enable(this);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
//        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
