package com.fryderykrott.receiptcarerfinal.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.utils.Utils;

import java.util.ArrayList;

public class MiniGroupAdapter extends RecyclerView.Adapter<MiniGroupAdapter.GroupViewHolder> {

    ArrayList<Group> groups;
    Context context;

    OnGroupChoosen listener;
    CardView selectedGroup;

    public MiniGroupAdapter(Context context){
        this.context = context;
        groups = Utils.user.getGroups();
    }


    @NonNull
    @Override
    public MiniGroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group, parent, false);
        MiniGroupAdapter.GroupViewHolder vh = new MiniGroupAdapter.GroupViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MiniGroupAdapter.GroupViewHolder holder, int position) {
        final Group group = groups.get(position);

//        holder.group_image_background.getBackground().setColorFilter(group.getIcon_color(), PorterDuff.Mode.SRC_ATOP);
        setColorOfGroup(holder, group);
        holder.group_name_text_view.setText(group.getName());
        holder.group_number_of_receipts_text_view.setText(String.format("%s paragonów", group.getNumberOfReceipts()));
        holder.group_sum_price_text_view.setVisibility(View.INVISIBLE);

        holder.group_delete_button.setVisibility(View.GONE);

        holder.group_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO zaznaczanie grup
                if(selectedGroup != null)
                    selectedGroup.setCardBackgroundColor(context.getColor(R.color.colorWhite));

                holder.group_container.setCardBackgroundColor(context.getColor(R.color.color_choosen_group));
                selectedGroup = holder.group_container;

                listener.onGroupChoosen(group);
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

    public void setOnNewGroupClickListener(OnGroupChoosen listener) {
        this.listener = listener;
    }

    public interface OnGroupChoosen{
        void onGroupChoosen(Group group);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        //        Group
        CardView group_container;

        ImageView group_image_icon;
        ImageView group_delete_button;

        TextView group_name_text_view;
        TextView group_number_of_receipts_text_view;
        TextView group_sum_price_text_view;


        public GroupViewHolder(View itemView) {
            super(itemView);

            group_container = itemView.findViewById(R.id.card_group_container);

            group_image_icon = itemView.findViewById(R.id.card_receipt_group_icon);
            group_delete_button = itemView.findViewById(R.id.delete_group_image_view);

            group_name_text_view = itemView.findViewById(R.id.card_receipt_name);
            group_sum_price_text_view = itemView.findViewById(R.id.card_receipt_total_sum);
            group_number_of_receipts_text_view = itemView.findViewById(R.id.card_receipt_date_of_creation);
        }
    }

}

