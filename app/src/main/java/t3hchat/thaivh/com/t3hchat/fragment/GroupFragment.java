package t3hchat.thaivh.com.t3hchat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import t3hchat.thaivh.com.t3hchat.R;
import t3hchat.thaivh.com.t3hchat.adapter.ListGroupsAdapter;
import t3hchat.thaivh.com.t3hchat.base.BaseFragment;
import t3hchat.thaivh.com.t3hchat.config.StaticConfig;
import t3hchat.thaivh.com.t3hchat.model.Group;
import t3hchat.thaivh.com.t3hchat.model.GroupDB;
import t3hchat.thaivh.com.t3hchat.view.AddGroupActivity;

/**
 * Created by thais on 3/9/2018.
 */

public class GroupFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerListGroups;
    public FragGroupClickFloatButton onClickFloatButton;
    private ArrayList<Group> listGroup;
    private ListGroupsAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final int CONTEXT_MENU_DELETE = 1;
    public static final int CONTEXT_MENU_EDIT = 2;
    public static final int CONTEXT_MENU_LEAVE = 3;
    public static final int REQUEST_EDIT_GROUP = 0;
    public static final String CONTEXT_MENU_KEY_INTENT_DATA_POS = "pos";

    LovelyProgressDialog progressDialog, waitingLeavingGroup;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_group;
    }

    @Override
    public void initLizeComponent() {
        recyclerListGroups =  rootView.findViewById(R.id.recycleListGroup);
        mSwipeRefreshLayout =  rootView.findViewById(R.id.swipeRefreshLayout);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerListGroups.setLayoutManager(layoutManager);
        // init  dialog
        progressDialog = new LovelyProgressDialog(getContext())
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_delete_group)
                .setTitle("Deleting....")
                .setTopColorRes(R.color.colorAccent);

        waitingLeavingGroup = new LovelyProgressDialog(getContext())
                .setCancelable(false)
                .setIcon(R.drawable.ic_dialog_delete_group)
                .setTitle("Group leaving....")
                .setTopColorRes(R.color.colorAccent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        listGroup = GroupDB.getInstance(getContext()).getListGroups();
        adapter = new ListGroupsAdapter(getContext(), listGroup);
        recyclerListGroups.setAdapter(adapter);
        onClickFloatButton = new FragGroupClickFloatButton();



        if(listGroup.size() == 0){
            //Ket noi server hien thi group
            mSwipeRefreshLayout.setRefreshing(true);
            getListGroup();
        }
    }

    @Override
    public void registerListener() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        listGroup.clear();
        ListGroupsAdapter.listFriend = null;
        GroupDB.getInstance(getContext()).dropDB();
        adapter.notifyDataSetChanged();
        getListGroup();

    }

    public void getListGroup() {
        FirebaseDatabase.getInstance().getReference().child("user/"+ StaticConfig.UID+"/group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    HashMap mapListGroup = (HashMap) dataSnapshot.getValue();
                    Iterator iterator = mapListGroup.keySet().iterator();
                    while (iterator.hasNext()){
                        String idGroup = (String) mapListGroup.get(iterator.next().toString());
                        Group newGroup = new Group();
                        newGroup.id = idGroup;
                        listGroup.add(newGroup);
                    }
                    getGroupInfo(0);
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getGroupInfo(final int indexGroup){
        if(indexGroup == listGroup.size()){
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }else {
            FirebaseDatabase.getInstance().getReference().child("group/"+listGroup.get(indexGroup).id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        HashMap mapGroup = (HashMap) dataSnapshot.getValue();
                        ArrayList<String> member = (ArrayList<String>) mapGroup.get("member");
                        HashMap mapGroupInfo = (HashMap) mapGroup.get("groupInfo");
                        for(String idMember: member){
                            listGroup.get(indexGroup).member.add(idMember);
                        }
                        listGroup.get(indexGroup).groupInfo.put("name", (String) mapGroupInfo.get("name"));
                        listGroup.get(indexGroup).groupInfo.put("admin", (String) mapGroupInfo.get("admin"));
                    }
                    GroupDB.getInstance(getContext()).addGroup(listGroup.get(indexGroup));
                    Log.d("GroupFragment", listGroup.get(indexGroup).id +": " + dataSnapshot.toString());
                    getGroupInfo(indexGroup +1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public class FragGroupClickFloatButton implements View.OnClickListener{

        Context context;
        public FragGroupClickFloatButton getInstance(Context context){
            this.context = context;
            return this;
        }

        @Override
        public void onClick(View view) {
            startActivity(new Intent(getContext(), AddGroupActivity.class));
        }
    }
}
