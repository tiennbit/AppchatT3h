package t3hchat.thaivh.com.t3hchat.view.contactlist;

import java.util.List;

import t3hchat.thaivh.com.t3hchat.model.ContactDto;

/**
 * Created by thais on 2/4/2018.
 */

public interface ContactListView {
    void getContactSuccess(List<ContactDto> contacts);
    void getContactFailure(String error);
}
