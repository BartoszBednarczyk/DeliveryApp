package com.example.deliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * The type Recycler view adapter basket.
 */
public class RecyclerViewAdapterBasket extends RecyclerView.Adapter<RecyclerViewAdapterBasket.ViewHolderBasket>{

    private ArrayList<String> mBasketNames = new ArrayList<>();
    private ArrayList<String> mBasketPrices = new ArrayList<>();
    private Context mContext;

    private OnBasketItemClickListener mListener;

    /**
     * The interface On basket item click listener.
     */
    public interface OnBasketItemClickListener{
        /**
         * On item click.
         *
         * @param position the position
         */
        void onItemClick(int position);

        /**
         * On delete click.
         *
         * @param position the position
         */
        void onDeleteClick(int position);
    }

    /**
     * Set on basket item click listener.
     *
     * @param listener the listener
     */
    public void setOnBasketItemClickListener(OnBasketItemClickListener listener){
        mListener = listener;
    }

    /**
     * Instantiates a new Recycler view adapter basket.
     *
     * @param mBasketNames  the m basket names
     * @param mBasketPrices the m basket prices
     * @param mContext      the m context
     */
    public RecyclerViewAdapterBasket(ArrayList<String> mBasketNames, ArrayList<String> mBasketPrices, Context mContext) {
        this.mBasketNames = mBasketNames;
        this.mBasketPrices = mBasketPrices;
        this.mContext = mContext;
    }

    @Override
    public ViewHolderBasket onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_basketitem, parent, false);
        ViewHolderBasket holder = new ViewHolderBasket(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBasket holder, int position) {


        holder.basketName.setText(mBasketNames.get(position));
        holder.basketPrice.setText(mBasketPrices.get(position));
    }

    @Override
    public int getItemCount() {
        return mBasketNames.size();
    }

    /**
     * The type View holder basket.
     */
    public class ViewHolderBasket extends RecyclerView.ViewHolder{

        /**
         * The Basket name.
         */
        TextView basketName;
        /**
         * The Basket price.
         */
        TextView basketPrice;
        /**
         * The Basket button.
         */
        ImageButton basketButton;
        /**
         * The Parent layout.
         */
        RelativeLayout parentLayout;

        /**
         * Instantiates a new View holder basket.
         *
         * @param itemView the item view
         * @param listener the listener
         */
        public ViewHolderBasket(View itemView, final OnBasketItemClickListener listener) {
            super(itemView);

            basketName = itemView.findViewById(R.id.basket_name);
            basketPrice = itemView.findViewById(R.id.basket_price);
            basketButton = itemView.findViewById(R.id.basket_button);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            basketButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}