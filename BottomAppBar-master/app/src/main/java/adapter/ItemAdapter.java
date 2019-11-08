package adapter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.MyDialog;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogGroupCreationCallBackListener;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import interfaces.CustomItemClick;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements OnAlertDialogGroupCreationCallBackListener {

    public static final int GROUPS = 0;
    public static final int RECEIPTS = 1;
    public static final int SEARCHING = 2;
    public static final int SMALL_GROUPS = 3;

    private int adapter_type = GROUPS;
    
    public ArrayList<GroupReceipt> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<GroupReceipt> arrayList) {
        this.arrayList = arrayList;
    }

    ArrayList<GroupReceipt> arrayList;

    CustomItemClick customItemClick;
    OnNewGroupClickListener onNewGroupClickListener;

    GroupReceipt checkedGroup;
    Activity context;

    public ItemAdapter(Activity context, ArrayList<GroupReceipt> arrayList, int adapter_type) {
        this.arrayList = arrayList;
//        this.customItemClick = customItemClick;
        this.context = context;
        this.adapter_type = adapter_type;
//        TODO zrobic coś z podstawową grupą, wrzucić jakieś podstawowe grupy ktorych nie da się zmienić
        checkedGroup = arrayList.get(0);
    }

    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int adapter_type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        ItemAdapter.MyViewHolder vh = new ItemAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        makeGroupCard(group, holder);
        if(adapter_type == GROUPS){
            if(position == arrayList.size())
            {
                makeAddGroupCard(holder, this);
                return;
            }

            GroupReceipt group = arrayList.get(position);
            makeGroupCard(group, holder);
        }
        else if(adapter_type == SMALL_GROUPS){
            if(position == arrayList.size())
            {
                makeAddGroupCard(holder, this);
                return;
            }
            GroupReceipt group = arrayList.get(position);
            makeSmallGroupCard(group, holder);
        }
        else if(adapter_type == RECEIPTS){
            GroupReceipt receipt = arrayList.get(position);
            makeReceiptCard(receipt, holder);
        }
        else if(adapter_type == SEARCHING){
//            jeśli searching to trzeba sprawdzic typ i wtedy
            GroupReceipt group_or_receipt = arrayList.get(position);

            if(group_or_receipt.getType() == GroupReceipt.TYPE_GROUP){
                makeGroupCard(group_or_receipt, holder);
            }
            else
                makeReceiptCard(group_or_receipt, holder);

        }

    }

    MyViewHolder current_clicked;

    private void clickedItem(MyViewHolder holder) {
        if(current_clicked != null){
            current_clicked.small_group_container.setBackground(context.getDrawable(R.color.colorWhite));

        }
//
//        if( holder.wasClicked){
//            current_clicked.small_group_container.setBackground(context.getDrawable(R.color.colorWhite));
//            holder.wasClicked = false;
//            return;
//        }

        holder.wasClicked = true;
        current_clicked = holder;
        current_clicked.small_group_container.setBackground(context.getDrawable(R.color.add_group_background_clicked));
    }

    private void makeSmallGroupCard(GroupReceipt group, MyViewHolder holder) {
        holder.smallGroupViewShow();

        setGroupHolderItemClickListener(holder, group);
        holder.small_group_image_background.getBackground().setColorFilter(group.getIcon_color(), PorterDuff.Mode.SRC_ATOP);
        holder.small_group_name_text_view.setText(group.getName());
    }

    private void makeAddGroupCard(final MyViewHolder holder, final ItemAdapter adapter) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogInfoBuilder.buildGroupAddingAlertDialog(context, adapter).show();
            }
        });

        holder.addGroupViewShow();
    }

    private void makeGroupCard(final GroupReceipt group, MyViewHolder holder) {
        holder.groupViewShow();

        setGroupHolderItemClickListener(holder, group);
//        Drawable drawable = context.getDrawable(R.drawable.circle);
//        drawable.setTint(group.getIcon_color());
//        drawable.setTint(context.getColor(R.color.colorAccent_3));
//        holder.group_image_background.setBackground(drawable);
        holder.group_image_background.getBackground().setColorFilter(group.getIcon_color(), PorterDuff.Mode.SRC_ATOP);
        holder.group_name_text_view.setText(group.getName());
        holder.group_sum_price_text_view.setText(String.format("%s PLN", group.getMoney_sum()));
    }

    private void setGroupHolderItemClickListener(final MyViewHolder holder, final GroupReceipt group) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("dsa", "GRUPA KLIKNIETA I DODANA");
                if(adapter_type == SMALL_GROUPS)
                    clickedItem(holder);

                checkedGroup = group;
                if(onNewGroupClickListener != null)
                     onNewGroupClickListener.onNewGroupClick(checkedGroup);
            }
        });

    }

    private void makeReceiptCard(GroupReceipt group, MyViewHolder holder) {
        holder.receiptViewShow();
//
//        Glide.with(holder.itemView.getContext())
//                .load(group.getImages_as_base64())
//                .into(holder.imageViewReceipt);
//
//        ViewCompat.setTransitionName(holder.imageViewReceipt, group.getName());

        holder.receipt_name_text_view.setText(group.getName());
    }

    @Override
    public int getItemCount() {
        if(adapter_type == GROUPS || adapter_type == SMALL_GROUPS){
           return arrayList.size() + 1;
        }

        return arrayList.size();
    }

    public void setOnNewGroupClickListener(OnNewGroupClickListener ln){
        onNewGroupClickListener = ln;
    }

    @Override
    public void onAlertDialogGroupCreationCallBackListener(MyDialog dialog, GroupReceipt new_group) {
//        TODO dodać globalnie tę grupę
        arrayList.add(new_group);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public boolean wasClicked = false;
        Drawable folder_1;
        Drawable folder_2;
        Drawable folder_3;
        Drawable folder_4;

//        Receipt
        View receipt_container;

//          mała ikona z grupą do ktorej należy
        LinearLayout receipt_group_image_background;
        ImageView receipt_group_image_icon;

        TextView receipt_name_text_view;
        Chip warranty_chip;
        Chip price_chip;
        Chip calendar_chip;

        ImageView receipt_image_view;

//        Group
        View group_container;

        LinearLayout group_image_background;
        ImageView group_image_icon;

        TextView group_name_text_view;
        TextView group_sum_price_text_view;

//        SmallGroup
        View small_group_container;

        LinearLayout small_group_image_background;
        ImageView small_group_image_icon;

        TextView small_group_name_text_view;

//        AddGroup
        View add_group_container;

        public MyViewHolder(View itemView) {
            super(itemView);

            declarateReceiptContainer(itemView);
            declarateGroupContainer(itemView);
            declarateAddGroupContainer(itemView);
            declarateSmallGrouptContainer(itemView);
        }

//        deklarate stuff
        private void declarateReceiptContainer(View itemView) {
            receipt_container = itemView.findViewById(R.id.receipt_container);
            receipt_group_image_background = itemView.findViewById(R.id.receipt_group_image_background);
            receipt_group_image_icon = itemView.findViewById(R.id.reeceipt_group_image_icon);

            receipt_name_text_view = itemView.findViewById(R.id.receipt_name_text_view);
            warranty_chip = itemView.findViewById(R.id.warranty_chip);
            price_chip = itemView.findViewById(R.id.price_chip);
            calendar_chip = itemView.findViewById(R.id.calendar_chip);

            receipt_image_view = itemView.findViewById(R.id.receipt_image_view);
        }

        private void declarateGroupContainer(View itemView) {
            group_container = itemView.findViewById(R.id.group_container);

            group_image_background = itemView.findViewById(R.id.group_image_background);
            group_image_icon = itemView.findViewById(R.id.group_image_icon);

            group_name_text_view = itemView.findViewById(R.id.group_name_text_view);
            group_sum_price_text_view = itemView.findViewById(R.id.group_sum_price_text_view);

        }

        private void declarateSmallGrouptContainer(View itemView) {
            small_group_container = itemView.findViewById(R.id.small_group_container);

            small_group_image_background = itemView.findViewById(R.id.group_small_image_background);
            small_group_image_icon = itemView.findViewById(R.id.group_small_image_icon);

            small_group_name_text_view = itemView.findViewById(R.id.group_small_name_text_view);
        }

        private void declarateAddGroupContainer(View itemView) {
            add_group_container = itemView.findViewById(R.id.group_add_container);
        }

//        End of declarating
        public void groupViewShow(){
            add_group_container.setVisibility(View.GONE);
            group_container.setVisibility(View.VISIBLE);
            receipt_container.setVisibility(View.GONE);
            small_group_container.setVisibility(View.GONE);
        }

        public void receiptViewShow(){
            add_group_container.setVisibility(View.GONE);
            group_container.setVisibility(View.GONE);
            receipt_container.setVisibility(View.VISIBLE);
            small_group_container.setVisibility(View.GONE);
        }

        public void smallGroupViewShow(){
            add_group_container.setVisibility(View.GONE);
            group_container.setVisibility(View.GONE);
            receipt_container.setVisibility(View.GONE);
            small_group_container.setVisibility(View.VISIBLE);
        }

        public void addGroupViewShow(){
            add_group_container.setVisibility(View.VISIBLE);
            group_container.setVisibility(View.GONE);
            receipt_container.setVisibility(View.GONE);
            small_group_container.setVisibility(View.GONE);
        }
    }

}

