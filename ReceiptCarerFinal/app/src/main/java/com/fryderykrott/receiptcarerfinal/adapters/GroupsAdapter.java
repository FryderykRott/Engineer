package com.fryderykrott.receiptcarerfinal.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fryderykrott.receiptcarerfinal.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.Utils;
import com.fryderykrott.receiptcarerfinal.alertdialogs.GroupAddingAlertDialog;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class GroupsAdapter  extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    ArrayList<Group> groups;
    Context context;

    GroupsAdapter(Context context){
        this.context = context;
        groups = Utils.user.getGroups();
    }

    @NonNull
    @Override
    public GroupsAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group, parent, false);
        GroupsAdapter.GroupViewHolder vh = new GroupsAdapter.GroupViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.GroupViewHolder holder, int position) {
        final Group group = groups.get(position);

//        holder.group_image_background.getBackground().setColorFilter(group.getIcon_color(), PorterDuff.Mode.SRC_ATOP);
        setColorOfGroup(holder, group);
        holder.group_name_text_view.setText(group.getName());
        holder.group_number_of_receipts_text_view.setText(String.format("%s paragonów", group.getNumberOfReceipts()));
        holder.group_sum_price_text_view.setText(String.format("%s PLN", group.getSumOfAllReceipts()));

        holder.group_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groups.remove(group);
                reloadGroupsInDatabase();
                ((MainActivity) context).showSnackBar("Pomyślnię usunięto grupę!");
            }
        });

        holder.group_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupAddingAlertDialog.OnGroupEditionCallBackListener l_edition = new GroupAddingAlertDialog.OnGroupEditionCallBackListener() {
                    @Override
                    public void onGroupEditionCallBackListener(Dialog dialog, Group group) {
                        reloadGroupsInDatabase();
                    }
                };

                GroupAddingAlertDialog ad = new GroupAddingAlertDialog(context, null, l_edition);
                ad.setToEditGroup(group);
                ad.show();

            }
        });
    }

    private void reloadGroupsInDatabase() {
        ((MainActivity) context).setProgressView(true);
        Database.getInstance().reloadGroupsOfCurrentUser(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((MainActivity) context).showSnackBar("Udało się wykonać operację!");
                ((MainActivity) context).setProgressView(false);
                notifyDataSetChanged();
            }
        });
    }

    private void setColorOfGroup(GroupViewHolder holder, Group group) {
        Drawable icon = holder.group_image_icon.getDrawable();
        switch (group.getColor()){
            case(1):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_1));
                break;
            case (2):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_2));
                break;
            case (3):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_3));
                break;
            case (4):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_4));
                break;
            case (5):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_5));
                break;
            case (6):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_6));
                break;
            default:
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_basic));
                break;
        }


    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {
        //        Group
        View group_container;

        ImageView group_image_icon;
        ImageView group_delete_button;

        TextView group_name_text_view;
        TextView group_number_of_receipts_text_view;
        TextView group_sum_price_text_view;


        public GroupViewHolder(View itemView) {
            super(itemView);

            group_container = itemView.findViewById(R.id.card_group_container);

            group_image_icon = itemView.findViewById(R.id.card_group_icon);
            group_delete_button = itemView.findViewById(R.id.delete_group_image_view);

            group_name_text_view = itemView.findViewById(R.id.card_group_name);
            group_sum_price_text_view = itemView.findViewById(R.id.card_group_total_sum);
            group_number_of_receipts_text_view = itemView.findViewById(R.id.card_group_number_of_receipts);
        }
    }

}
