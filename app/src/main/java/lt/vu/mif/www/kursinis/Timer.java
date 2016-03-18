package lt.vu.mif.www.kursinis;

import android.os.Handler;
import android.os.Looper;

public class Timer extends Thread{
    public Handler timerHandler;
    Handler givenHandler;

    public Timer (Handler givenHandler){
        this.givenHandler = givenHandler;
    }
    @Override
    public void run()
    {
        Looper.prepare();

    }

}
