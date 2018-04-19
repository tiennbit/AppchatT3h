package t3hchat.thaivh.com.t3hchat;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by thais on 2/11/2018.
 */

public class App extends Application {


    private static App instance;


    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}
