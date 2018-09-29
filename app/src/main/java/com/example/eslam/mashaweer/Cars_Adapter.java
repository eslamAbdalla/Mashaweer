package com.example.eslam.mashaweer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Cars_Adapter extends RecyclerView.Adapter<Cars_Adapter.ViewHolder> {

    private ArrayList<String> carBrandName = new ArrayList<>();
    private ArrayList<String> carModel = new ArrayList<>();
    private ArrayList<String> carBrandYear = new ArrayList<>();
    private Context mContext;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
   public Cars_Adapter(Context context, ArrayList<String> carBrandName , ArrayList<String> carModel, ArrayList<String> carBrandYear) {

       this.carBrandName = carBrandName ;
       this.carModel = carModel ;
       this.carBrandYear = carBrandYear;
       mContext = context ;



        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cars_item_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       holder.brandName.setText(this.carBrandName.get(position));
       holder.model.setText(this.carModel.get(position));
       holder.year.setText(this.carBrandYear.get(position));



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return carBrandName.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandName,model,year;


        public ViewHolder(View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.tvCarBrand);
            model = itemView.findViewById(R.id.tvCarModel);
            year = itemView.findViewById(R.id.tvCarYear);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return carBrandName.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}