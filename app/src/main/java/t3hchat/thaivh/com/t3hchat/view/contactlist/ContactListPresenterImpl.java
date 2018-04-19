package t3hchat.thaivh.com.t3hchat.view.contactlist;

import android.util.Log;

/**
 * Created by thais on 2/4/2018.
 */

public class ContactListPresenterImpl implements ContactListPresenter {
    private String TAG = this.getClass().getSimpleName();

    private ContactListView view;

    public ContactListPresenterImpl(ContactListView view) {
        Log.d(TAG, "ContactListPresenterImpl");
        this.view = view;
    }

    @Override
    public void getContact() {

    }
}
