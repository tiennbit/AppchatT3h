package t3hchat.thaivh.com.t3hchat.view.contactlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import t3hchat.thaivh.com.t3hchat.R;
import t3hchat.thaivh.com.t3hchat.base.BaseFragment;
import t3hchat.thaivh.com.t3hchat.model.ContactDto;

/**
 * Created by thais on 2/17/2018.
 */

public class ContactListFragment extends BaseFragment implements ContactListView {

    private String TAG = this.getClass().getSimpleName();
    private RecyclerView rcvContact;
    private ContactAdapter contactAdapter;
    private ProgressBar prbLoading;
    private ContactListPresenterImpl userListPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userListPresenter = new ContactListPresenterImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userListPresenter.getContact();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_contact_list;
    }

    @Override
    public void initLizeComponent() {
        rcvContact = rootView.findViewById(R.id.rcv_contact);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcvContact.setLayoutManager(llm);
        //rcvContact.setLayoutManager(new LinearLayoutManager(getContext()));
        prbLoading = rootView.findViewById(R.id.prb_loading);
    }

    @Override
    public void registerListener() {

    }

    @Override
    public void getContactSuccess(List<ContactDto> contacts) {
        Log.d(TAG, "getContactSuccess: " + contacts.size());
        if (contactAdapter == null) {
            contactAdapter = new ContactAdapter(getContext(), contacts);
            rcvContact.setAdapter(contactAdapter);
        } else {
            // setData again
            contactAdapter.notifyDataSetChanged();
        }
        rcvContact.setVisibility(View.VISIBLE);
        prbLoading.setVisibility(View.GONE);

    }

    @Override
    public void getContactFailure(String error) {
        Log.d(TAG, "getContactFailure: " + error);
        prbLoading.setVisibility(View.GONE);
        showError(error);
    }
}
