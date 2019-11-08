package adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.ReceiptCarerUtils.Utils;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.fragments.receipt.adding.ReceiptAddingActivity;

import java.util.ArrayList;

//zeby to zadzialalo to muszę poprosić aktywnosc o zdjecie tego pragmentu o zrobienie zdjecia i dostarczenie go do tego
// akurat paragonu i odswiezyc adapter
//1. popros aktywnosc o wykoanie akcji dodania
// W SUMIe to moge tak to napisac ze po powrocie wszystko bedzie dobrze ale chyba nie
// czyli trzeba na dana pozycja paragonów ddać nowe zdjęci i wrocic
//LUZ
public class ImageAdapter extends PagerAdapter {
    private int receipt_position;
    private Activity context;
    ArrayList<Bitmap> images;

    private boolean isClickable;

    OnNewPhotoCallbackListener listener;

    public ImageAdapter(Activity context,  ArrayList<Bitmap> images) {
        this.context = context;
        this.images = images;
        this.isClickable = false;
    }

    public ImageAdapter(Activity context,  ArrayList<Bitmap> images,int receipt_position, OnNewPhotoCallbackListener listener) {
        this.context = context;
        this.images = images;
        this.isClickable = true;
        this.receipt_position = receipt_position;
        this.listener = listener;

//        resizeImages();
    }

    private void resizeImages() {
        Bitmap bitmap;
        Bitmap new_bitmap;
        for (int i = 0; i < images.size(); i++){
            bitmap = images.get(i);
            new_bitmap = Bitmap.createScaledBitmap(
                    bitmap, bitmap.getWidth() / 2 , bitmap.getHeight() /2, false);
            images.set(i, new_bitmap);
        }
    }

    TextView information;
    ImageView add_photo_image;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_pager_item, container, false);

        ImageView imageView = view.findViewById(R.id.image);
        if(isClickable){
            if(position == images.size() ) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                     Możliwość dodania kolejnego zdjęcia lub paru
                        listener.onNewPhotoCallback(receipt_position);
                    }
                });
            }
            else {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialogInfoBuilder.builImagesViewFullScreenAlertDialog(context, (ReceiptAddingActivity) context, images, position).show();
                    }
                });
            }

        }

        information = view.findViewById(R.id.Information);
        add_photo_image = view.findViewById(R.id.add_photo_image);

        if(position == images.size() ){
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources() ,R.drawable.image_camera_add);
//            imageView.setImageBitmap(bitmap);
            setVisibilityOfInformationPanel(View.VISIBLE);
        }
        else{
            setVisibilityOfInformationPanel(View.INVISIBLE);
            imageView.setImageBitmap(Utils.RotateBitmap(getImageAt(position), 90));
        }

//        imageView.setImageDrawable(context.getDrawable(getImageAt(position)));
        container.addView(view);
        return view;
    }

    private void setVisibilityOfInformationPanel(int visibility){
        information.setVisibility(visibility);
        add_photo_image.setVisibility(visibility);
    }

    /*
    This callback is responsible for destroying a page. Since we are using view only as the
    object key we just directly remove the view from parent container
    */
    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
    /*
    Returns the count of the total pages
    */
    @Override
    public int getCount() {
        if(isClickable)
            return images.size() + 1;
        else
            return images.size();
    }

    /*
    Used to determine whether the page view is associated with object key returned by instantiateItem.
    Since here view only is the key we return view==object
    */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    private Bitmap getImageAt(int position) {
        return images.get(position);
    }

}
