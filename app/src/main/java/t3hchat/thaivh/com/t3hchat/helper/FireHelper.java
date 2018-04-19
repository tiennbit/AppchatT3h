package t3hchat.thaivh.com.t3hchat.helper;

import com.google.firebase.auth.FirebaseAuth;

import t3hchat.thaivh.com.t3hchat.model.User;

/**
 * Created by thais on 3/10/2018.
 */

public class FireHelper {
    public static String getCurUserId () {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
