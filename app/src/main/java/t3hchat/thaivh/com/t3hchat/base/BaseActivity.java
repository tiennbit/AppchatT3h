package t3hchat.thaivh.com.t3hchat.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by thais on 2/4/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements ViewInitlizer {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int viewId = getContentViewId();
        if (viewId <= 0) {
            return;
        }
        setContentView(viewId);
        initLizeComponent();
        registerListener();
    }



    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


}
