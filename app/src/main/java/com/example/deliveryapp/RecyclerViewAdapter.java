package com.example.deliveryapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The type Recycler view adapter.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param position the position
         */
        void onItemClick(int position);

        /**
         * On button click.
         *
         * @param position the position
         */
        void onButtonClick(int position);
    }

    /**
     * Set on item click listener.
     *
     * @param listener the listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }


    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mProductNames = new ArrayList<>();
    private ArrayList<String> mProductDescs = new ArrayList<>();
    private ArrayList<String> mProductPrices = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    /**
     * Instantiates a new Recycler view adapter.
     *
     * @param mProductNames  the m product names
     * @param mProductDescs  the m product descs
     * @param mProductPrices the m product prices
     * @param mImages        the m images
     * @param mContext       the m context
     */
    public RecyclerViewAdapter(ArrayList<String> mProductNames, ArrayList<String> mProductDescs, ArrayList<String> mProductPrices, ArrayList<String> mImages, Context mContext) {
        this.mProductNames = mProductNames;
        this.mProductDescs = mProductDescs;
        this.mProductPrices = mProductPrices;
        this.mImages = mImages;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.productImage);

        holder.productDesc.setText(mProductDescs.get(position));
        holder.productName.setText(mProductNames.get(position));
        holder.productPrice.setText(mProductPrices.get(position));


    }

    @Override
    public int getItemCount() {
        return mProductNames.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * The Product image.
         */
        CircleImageView productImage;
        /**
         * The Product name.
         */
        TextView productName, /**
         * The Product desc.
         */
        productDesc, /**
         * The Product price.
         */
        productPrice;
        /**
         * The Product button.
         */
        ImageButton productButton;
        /**
         * The Parent layout.
         */
        RelativeLayout parentLayout;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         * @param listener the listener
         */
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDesc = itemView.findViewById(R.id.product_desc);
            productPrice = itemView.findViewById(R.id.product_price);
            productButton = itemView.findViewById(R.id.product_button);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            productButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onButtonClick(position);
                        }
                    }
                }
            });
        }
    }



}


