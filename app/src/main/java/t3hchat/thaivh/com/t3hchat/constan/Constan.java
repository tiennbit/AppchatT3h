package t3hchat.thaivh.com.t3hchat.constan;

import java.util.regex.Pattern;

/**
 * Created by thais on 3/9/2018.
 */

public class Constan {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
