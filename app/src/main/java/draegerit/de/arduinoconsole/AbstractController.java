package draegerit.de.arduinoconsole;

import android.support.v7.app.AppCompatActivity;

public abstract class AbstractController<T extends AppCompatActivity> {

    private T activity;

    public AbstractController(T activity) {
        this.activity = activity;
    }

    public abstract void registerComponents();

    public T getActivity() {
        return activity;
    }
}

