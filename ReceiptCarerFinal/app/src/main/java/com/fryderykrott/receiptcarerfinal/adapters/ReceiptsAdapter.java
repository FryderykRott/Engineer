package com.fryderykrott.receiptcarerfinal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.fryderykrott.receiptcarerfinal.chips.MiniBasicChipContainer;
import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.model.Tag;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

import io.grpc.okhttp.internal.Util;

public class ReceiptsAdapter  extends RecyclerView.Adapter<ReceiptsAdapter.ReceiptViewHolder> {

    ArrayList<Receipt> receipts;
    Context context;

    public ReceiptsAdapter(Context context){
        this.context = context;
//        ArrayList<Group> groups = Utils.user.getGroups();
//
//        ArrayList<Receipt> receiptsGroup;
        if(Utils.user == null)
            receipts = new ArrayList<>();
        else
            receipts = Utils.user.getReceipts();
//
//        for(int i = 0; i < groups.size(); i++){
//            receiptsGroup = groups.get(i).getReceipts();
//               receipts.addAll(receiptsGroup);
//        }
    }


    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_receipt, parent, false);
        ReceiptViewHolder vh = new ReceiptViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        final Receipt receipt = receipts.get(position);

//        holder.group_image_background.getBackground().setColorFilter(group.getIcon_color(), PorterDuff.Mode.SRC_ATOP);
        setColorOfGroup(holder, Utils.findGroupById(receipt.getGroupID()));

        holder.receipt_name.setText(receipt.getName());
        holder.receipt_date_of_creation.setText(receipt.getDateOfCreation());
        holder.receipt_total_sum.setText(receipt.getSumTotal() + " PLN");

        holder.receipt_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Utils.findGroupById(receipt.getGroupID()).getReceipts().remove(receipt);
//
//                reloadGroupsInDatabase();
//                ((MainActivity) context).showSnackBar("Pomyślnię usunięto paragon!");
            }
        });

        holder.receipt_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GroupAddingAlertDialog.OnGroupEditionCallBackListener l_edition = new GroupAddingAlertDialog.OnGroupEditionCallBackListener() {
//                    @Override
//                    public void onGroupEditionCallBackListener(Dialog dialog, Group group) {
//                        reloadGroupsInDatabase();
//                    }
//                };
//
//                GroupAddingAlertDialog ad = new GroupAddingAlertDialog(context, null, l_edition);
//                ad.setToEditGroup(receipt);
//                ad.show();

            }
        });

        holder.receipt_warranty_date.setText(receipt.getDateOfEndOfWarrant());

        ArrayList<String> tagsID = receipt.getTagsID();
        Tag tag;
        MiniBasicChipContainer miniBasicChipContainer;

        for(int i = 0; i < tagsID.size(); i++){
            tag = Utils.findTagById(tagsID.get(i));
            miniBasicChipContainer = new MiniBasicChipContainer(context, tag.getName());
            holder.tagsChipGroup.addView(miniBasicChipContainer.getMiniChip());
        }

        Bitmap thumbnailBitmap = receipt.somethingDifferentImagesAsBitmap().get(0);
        holder.receipt_thumbnail.setImageBitmap(thumbnailBitmap);
    }

    private void reloadGroupsInDatabase() {
        ((MainActivity) context).setProgressView(true);
        Database.getInstance().reloadGroupsOfCurrentUser(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                ((MainActivity) context).showSnackBar("Udało się wykonać operację!");
                ((MainActivity) context).setProgressView(false);
                notifyDataSetChanged();
            }
        });
    }

    private void setColorOfGroup(ReceiptViewHolder holder, Group group) {
        Drawable icon = holder.receipt_group_icon.getDrawable();
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
        return receipts.size();
    }



    public class ReceiptViewHolder extends RecyclerView.ViewHolder {

        View receipt_container;

        ImageView receipt_thumbnail;
        ImageView receipt_group_icon;
        ImageView receipt_delete_button;

        TextView receipt_name;
        TextView receipt_date_of_creation;
        TextView receipt_total_sum;
        TextView receipt_warranty_date;

        ChipGroup tagsChipGroup;


        public ReceiptViewHolder(View itemView) {
            super(itemView);

            receipt_container = itemView.findViewById(R.id.card_group_container);
            receipt_thumbnail = itemView.findViewById(R.id.card_receipt_thumbnail);
            receipt_group_icon = itemView.findViewById(R.id.card_receipt_group_icon);
            receipt_delete_button = itemView.findViewById(R.id.card_receipt_delete);

            receipt_name = itemView.findViewById(R.id.card_receipt_name);
            receipt_total_sum = itemView.findViewById(R.id.card_receipt_total_sum);
            receipt_date_of_creation = itemView.findViewById(R.id.card_receipt_date_of_creation);
            receipt_warranty_date = itemView.findViewById(R.id.card_receipt_warranty);

            tagsChipGroup = itemView.findViewById(R.id.card_receipt_chip_group);
            tagsChipGroup.removeAllViews();
        }
    }

}
