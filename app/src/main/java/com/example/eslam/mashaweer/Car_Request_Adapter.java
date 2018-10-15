package com.example.eslam.mashaweer;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Car_Request_Adapter extends RecyclerView.Adapter<Car_Request_Adapter.ViewHolder> {

    private ArrayList<String> carBrandName = new ArrayList<>();
    private ArrayList<String> carModel = new ArrayList<>();
    private ArrayList<String> carYear = new ArrayList<>();
    private ArrayList<String> carImageUrl = new ArrayList<>();
    private ArrayList<String> platNo = new ArrayList<>();
    private Context mContext;

    private LayoutInflater mInflater;
    private Car_Request_Adapter.ItemClickListener mClickListener;



    // data is passed into the constructor
    public Car_Request_Adapter(Context context, ArrayList<String> carBrandName , ArrayList<String> carModel,ArrayList<String> carYear,ArrayList<String> plateNo,ArrayList<String> carImage) {

        this.carBrandName = carBrandName ;
        this.carModel = carModel ;
        this.carImageUrl = carImage;
        this.carYear = carYear ;
        this.platNo = plateNo ;
        mContext = context ;



        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public Car_Request_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.car_requst_item_list, parent, false);
        return new Car_Request_Adapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(Car_Request_Adapter.ViewHolder holder, int position) {

        String uploadImageUrl = this.carImageUrl.get(position);
        holder.brandName.setText(this.carBrandName.get(position));
        holder.model.setText(this.carModel.get(position));
        holder.year.setText(this.carYear.get(position));

        Picasso.with(mContext).load(uploadImageUrl).fit().centerCrop().into(holder.carImageView);
    }
    // total number of rows
    @Override
    public int getItemCount() {
        return carBrandName.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView brandName,model,year;
        ImageView carImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.cr_brandName);
            model = itemView.findViewById(R.id.cr_model);
            year = itemView.findViewById(R.id.cr_year);
            carImageView = itemView.findViewById(R.id.carImage);




            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return platNo.get(id);
    }




    // allows clicks events to be caught
    void setClickListener(Car_Request_Adapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}