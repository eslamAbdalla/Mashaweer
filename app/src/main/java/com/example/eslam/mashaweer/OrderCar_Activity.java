package com.example.eslam.mashaweer;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OrderCar_Activity extends AppCompatActivity {


//    ProgressBar progressBar ;
    TextView Current , Total;
    private String[] imageUrls ;
//            = new String[]{
////            "https://firebasestorage.googleapis.com/v0/b/mashaweer-21020.appspot.com/o/Ab%2FAb.jpg?alt=media&token=62a6aca4-1b25-47b1-be58-9d63f3bcc514"
//    } ;







    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference carsRef = db.collection("Cars");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_car_);


//        progressBar = (ProgressBar)findViewById(R.id.currentImageProgressBar);

        Current = (TextView)findViewById(R.id.current_image_no);
        Total = (TextView)findViewById(R.id.total_image_no);

        String plateNo = Car_Request.SelectedCarPlateNo;

        carsRef
                .whereEqualTo("platNo",plateNo)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String cars = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Cars car = documentSnapshot.toObject(Cars.class);
//                            cars.setDocumentId(documentSnapshot.getId());

                            String userId = car.getUserID();
                            String brand = car.getBrand();
                            String model = car.getModel();
                            String color = car.getColor();
                            String year = car.getYear();
                            String plate = car.getPlatNo();
                            String gov = car.getGov();
                            String city = car.getCity();
                            List<String> imagesList = car.getImageList();

                            imageUrls = new String[imagesList.size()];

                            for (int i = 0; i < imagesList.size(); i++){

                                imageUrls[i] = imagesList.get(i);
                            }



                        }


                        final ViewPager viewPager = findViewById(R.id.pager);
                        ViewPagerAdapter adapter = new ViewPagerAdapter(OrderCar_Activity.this,imageUrls);
                        viewPager.setAdapter(adapter);

                        int current = viewPager.getCurrentItem();

                        double progress = (100.0 *(current+1) / imageUrls.length);
//                        progressBar.setProgress((int) progress);

                        bottomProgressDots(0);

                        Current.setText(""+(current+1));
                        Total.setText(""+imageUrls.length);


                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
                            }

                            @Override
                            public void onPageSelected(int pos) {

                                bottomProgressDots(pos);


                                int current = viewPager.getCurrentItem();

                                double progress = (100.0 *(current+1) / imageUrls.length);
//                                progressBar.setProgress((int) progress);



                                Current.setText(""+(current+1));
                                Total.setText(""+imageUrls.length);


                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });








    }


    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[imageUrls.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.light_green_600), PorterDuff.Mode.SRC_IN);
        }
    }



}
