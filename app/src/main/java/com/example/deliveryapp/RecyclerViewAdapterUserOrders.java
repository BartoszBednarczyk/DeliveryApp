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
 * The type Recycler view adapter user orders.
 */
public class RecyclerViewAdapterUserOrders extends RecyclerView.Adapter<RecyclerViewAdapterUserOrders.ViewHolderUserOrders>{

    private ArrayList<String> mUserOrdersIDs = new ArrayList<>();
    private ArrayList<String> mUserOrdersNames = new ArrayList<>();
    private ArrayList<String> mUserOrdersLastNames = new ArrayList<>();
    private ArrayList<String> mUserOrdersAddresses = new ArrayList<>();
    private ArrayList<String> mUserOrdersZips = new ArrayList<>();
    private ArrayList<String> mUserOrdersCities = new ArrayList<>();
    private ArrayList<String> mUserOrdersProducts = new ArrayList<>();
    private ArrayList<String> mUserOrdersTotals = new ArrayList<>();
    private ArrayList<String> mUserOrdersStates = new ArrayList<>();
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
     * Instantiates a new Recycler view adapter user orders.
     *
     * @param mUserOrdersIDs       the m user orders i ds
     * @param mUserOrdersNames     the m user orders names
     * @param mUserOrdersLastNames the m user orders last names
     * @param mUserOrdersAddresses the m user orders addresses
     * @param mUserOrdersZips      the m user orders zips
     * @param mUserOrdersCities    the m user orders cities
     * @param mUserOrdersProducts  the m user orders products
     * @param mUserOrdersTotals    the m user orders totals
     * @param mUserOrdersStates    the m user orders states
     * @param mContext             the m context
     */
    public RecyclerViewAdapterUserOrders(ArrayList<String> mUserOrdersIDs, ArrayList<String> mUserOrdersNames,
                                         ArrayList<String> mUserOrdersLastNames, ArrayList<String>
                                                 mUserOrdersAddresses, ArrayList<String> mUserOrdersZips,
                                         ArrayList<String> mUserOrdersCities, ArrayList<String> mUserOrdersProducts,
                                         ArrayList<String> mUserOrdersTotals, ArrayList<String> mUserOrdersStates,
                                         Context mContext) {

        this.mUserOrdersIDs = mUserOrdersIDs;
        this.mUserOrdersNames = mUserOrdersNames;
        this.mUserOrdersLastNames = mUserOrdersLastNames;
        this.mUserOrdersAddresses = mUserOrdersAddresses;
        this.mUserOrdersZips = mUserOrdersZips;
        this.mUserOrdersCities = mUserOrdersCities;
        this.mUserOrdersProducts = mUserOrdersProducts;
        this.mUserOrdersTotals = mUserOrdersTotals;
        this.mUserOrdersStates = mUserOrdersStates;
        this.mContext = mContext;
    }

    @Override
    public ViewHolderUserOrders onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orderitem, parent, false);
        ViewHolderUserOrders holder = new ViewHolderUserOrders(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderUserOrders holder, int position) {

        holder.UserOrdersID.setText(mUserOrdersIDs.get(position));
        holder.UserOrdersName.setText(mUserOrdersNames.get(position));
        holder.UserOrdersLastName.setText(mUserOrdersLastNames.get(position));
        holder.UserOrdersAddress.setText(mUserOrdersAddresses.get(position));
        holder.UserOrdersZip.setText(mUserOrdersZips.get(position));
        holder.UserOrdersCity.setText(mUserOrdersCities.get(position));
        holder.UserOrdersProducts.setText(mUserOrdersProducts.get(position));
        holder.UserOrdersTotal.setText(mUserOrdersTotals.get(position));
        holder.UserOrdersState.setText(mUserOrdersStates.get(position));

    }

    @Override
    public int getItemCount() {
        return mUserOrdersNames.size();
    }

    /**
     * The type View holder user orders.
     */
    public class ViewHolderUserOrders extends RecyclerView.ViewHolder{

        /**
         * The User orders id.
         */
        TextView UserOrdersID;
        /**
         * The User orders name.
         */
        TextView UserOrdersName;
        /**
         * The User orders last name.
         */
        TextView UserOrdersLastName;
        /**
         * The User orders address.
         */
        TextView UserOrdersAddress;
        /**
         * The User orders zip.
         */
        TextView UserOrdersZip;
        /**
         * The User orders city.
         */
        TextView UserOrdersCity;
        /**
         * The User orders products.
         */
        TextView UserOrdersProducts;
        /**
         * The User orders total.
         */
        TextView UserOrdersTotal;
        /**
         * The User orders state.
         */
        TextView UserOrdersState;
        /**
         * The Parent layout.
         */
        RelativeLayout parentLayout;

        /**
         * Instantiates a new View holder user orders.
         *
         * @param itemView the item view
         * @param listener the listener
         */
        public ViewHolderUserOrders(View itemView, final OnBasketItemClickListener listener) {
            super(itemView);
            UserOrdersID = itemView.findViewById(R.id.textViewID);
            UserOrdersName = itemView.findViewById(R.id.textViewName);
            UserOrdersLastName = itemView.findViewById(R.id.textViewLastName);
            UserOrdersAddress = itemView.findViewById(R.id.textViewAddress);
            UserOrdersZip = itemView.findViewById(R.id.textViewZip);
            UserOrdersCity = itemView.findViewById(R.id.textViewCity);
            UserOrdersProducts = itemView.findViewById(R.id.textViewOrdersList);
            UserOrdersTotal = itemView.findViewById(R.id.textViewPrice);
            UserOrdersState = itemView.findViewById(R.id.textViewState);

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

        }
    }
}