package com.fryderykrott.receiptcarerfinal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.fryderykrott.receiptcarerfinal.MainView.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.chips.MiniBasicChipContainer;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.Model.Tag;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.Model.Group;
import com.fryderykrott.receiptcarerfinal.Services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ReceiptViewHolder> {

    ArrayList<Receipt> receipts;
    MainActivity context;
    NavController navControler;

    public ReceiptsAdapter(MainActivity context, NavController navControler){

        this.context = context;
        if(Utils.user == null)
            receipts = new ArrayList<>();
        else
            receipts = Utils.user.getReceipts();

        this.navControler = navControler;
    }

    boolean isSerchable = false;
    public ReceiptsAdapter(MainActivity activity, NavController navController, ArrayList<Receipt> filtredReceipts) {
        this.context = activity;

        receipts = filtredReceipts;
        this.navControler = navController;
        isSerchable = true;
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
        holder.tagsChipGroup.removeAllViews();

        holder.receipt_name.setText(receipt.getName());
        holder.receipt_date_of_creation.setText(receipt.getDateOfCreation());
        holder.receipt_total_sum.setText(receipt.getSumTotal() + " PLN");

        if(isSerchable)
            holder.receipt_delete_button.setVisibility(View.GONE);
        else
            holder.receipt_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Utils.user.getReceipts().remove(receipt);
                    Database.getInstance().reloadGroupsOfCurrentUser(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            context.showSnackBar("Pomyślnie usunięto paragon");
                        }
                    });
                    notifyDataSetChanged();
                }
        });

        holder.receipt_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.setReceiptToEdit(receipt);
                navControler.navigate(R.id.navigation_editing_receipt);
            }
        });
        setWarrantyDate(holder, receipt);

//        dodaj chip grupy
        Group group = Utils.findGroupById(receipt.getGroupID());
        MiniBasicChipContainer miniGroupChipContainer = new MiniBasicChipContainer(context, group);
        holder.tagsChipGroup.addView(miniGroupChipContainer.getMiniChip());

//        dodaj chipy tagów
        ArrayList<String> tagsID = receipt.getTagsID();
        Tag tag;
        MiniBasicChipContainer miniBasicChipContainer;

        for(int i = 0; i < tagsID.size(); i++){
            tag = Utils.findTagById(tagsID.get(i));
            miniBasicChipContainer = new MiniBasicChipContainer(context, "#" + tag.getName());
            holder.tagsChipGroup.addView(miniBasicChipContainer.getMiniChip());
        }

//        Bitmap thumbnailBitmap = receipt.somethingDifferentImagesAsBitmap().get(0);
//        holder.receipt_thumbnail.setImageBitmap(thumbnailBitmap);

        if(!receipt.getImages_as_base64().isEmpty()){
            Picasso.get()
                    .load(receipt.getImages_as_base64().get(0))
                    .resize(400, 400)
                    .centerCrop()
                    .into(holder.receipt_thumbnail);
        }

    }

    private void setWarrantyDate(ReceiptViewHolder holder, Receipt receipt) {
        Date dateOfWarrantyEnd = Utils.formatStringToDate(receipt.getDateOfEndOfWarrant());
        if(dateOfWarrantyEnd == null){
            holder.receipt_warranty_date.setText("Brak gwarancji");
            return;
        }
        int daysToWarrantyEnd = Utils.convertDateToDays(dateOfWarrantyEnd);

        if(daysToWarrantyEnd >= 4000)
            holder.receipt_warranty_date.setText("Nieograniczona");
        else
            holder.receipt_warranty_date.setText("Pozostało: " +daysToWarrantyEnd + " dni");

        if(daysToWarrantyEnd <= 7)
        {
            holder.receipt_warranty_date.setTextColor(context.getColor(R.color.red));
        }
        else if(daysToWarrantyEnd <= 31){
            holder.receipt_warranty_date.setTextColor(context.getColor(R.color.orange));
        }
        else
            holder.receipt_warranty_date.setTextColor(context.getColor(R.color.green));

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

    @Override
    public int getItemCount() {
        return receipts.size();
    }



    public class ReceiptViewHolder extends RecyclerView.ViewHolder {

        View receipt_container;

        ImageView receipt_thumbnail;
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
