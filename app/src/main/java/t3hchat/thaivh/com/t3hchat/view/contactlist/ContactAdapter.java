package t3hchat.thaivh.com.t3hchat.view.contactlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import t3hchat.thaivh.com.t3hchat.R;
import t3hchat.thaivh.com.t3hchat.model.ContactDto;

/**
 * Created by thais on 2/11/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private List<ContactDto> userDtoList;
    private LayoutInflater inflater;

    public ContactAdapter(Context context, List<ContactDto> userDtoList) {
        this.context = context;
        this.userDtoList = userDtoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactDto contactDto = userDtoList.get(position);
        holder.tvName.setText(contactDto.getName());
        Glide.with(context).load(contactDto.getImg()).into(holder.avt);
    }

    @Override
    public int getItemCount() {
        return userDtoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private CircleImageView avt;
        public ViewHolder(View itemView) {
            super(itemView);
            initComponent(itemView);
        }

        private void initComponent(View view) {
            tvName = view.findViewById(R.id.tv_name);
        }
    }

}
