package com.sgp.spendsync.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sgp.spendsync.Model.ModelTransaction;
import com.sgp.spendsync.Others.ItemClick;
import com.sgp.spendsync.R;

import java.util.List;

public class TranscationAdapter extends RecyclerView.Adapter<TranscationAdapter.MyViewHolder> {

    Activity activity;
    List<ModelTransaction> modelTransactions;
    ItemClick onClick;
    public  int img;
    public static int[] image = {
            R.mipmap.ic_category_salary_normal, R.mipmap.ic_category_awards_normal,
            R.mipmap.ic_category_grants_normal, R.mipmap.ic_category_sale_normal,
            R.mipmap.ic_category_rental_normal, R.mipmap.ic_category_refunds_normal,
            R.mipmap.ic_category_coupons_normal, R.mipmap.ic_category_lottery_normal,
            R.mipmap.ic_category_dividends_normal, R.mipmap.ic_category_investments_normal,
            R.mipmap.ic_category_food_normal, R.mipmap.ic_category_bills_normal,
            R.mipmap.ic_category_transportation_normal, R.mipmap.ic_category_home_normal,
            R.mipmap.ic_category_car_normal, R.mipmap.ic_category_entertainment_normal,
            R.mipmap.ic_category_shopping_normal, R.mipmap.ic_category_clothing_normal,
            R.mipmap.ic_category_insurance_normal, R.mipmap.ic_category_tax_normal,
            R.mipmap.ic_category_telephone_normal, R.mipmap.ic_category_cigarette_normal,
            R.mipmap.ic_category_health_normal, R.mipmap.ic_category_sport_normal,
            R.mipmap.ic_category_baby_normal,
            R.mipmap.ic_category_pet_normal, R.mipmap.ic_category_beauty_normal,
            R.mipmap.ic_category_electronics_normal, R.mipmap.ic_category_hamburger_normal,
            R.mipmap.ic_category_wine_normal, R.mipmap.ic_category_vegetables_normal,
            R.mipmap.ic_category_snacks_normal, R.mipmap.ic_category_gift_normal,
            R.mipmap.ic_category_social_normal, R.mipmap.ic_category_travel_normal,
            R.mipmap.ic_category_education_normal, R.mipmap.ic_category_fruits_normal,
            R.mipmap.ic_category_book_normal, R.mipmap.ic_category_office_normal,
            R.mipmap.ic_normal_education_01, R.mipmap.ic_normal_education_02,
            R.mipmap.ic_normal_education_03, R.mipmap.ic_normal_education_04,
            R.mipmap.ic_normal_education_05, R.mipmap.ic_normal_education_06,
            R.mipmap.ic_normal_education_07, R.mipmap.ic_normal_electronics_01,
            R.mipmap.ic_normal_electronics_02, R.mipmap.ic_normal_electronics_03,
            R.mipmap.ic_normal_electronics_04, R.mipmap.ic_normal_electronics_05,
            R.mipmap.ic_normal_electronics_06,
            R.mipmap.ic_normal_entertainment_01, R.mipmap.ic_normal_entertainment_02,
            R.mipmap.ic_normal_entertainment_03, R.mipmap.ic_normal_entertainment_04,
            R.mipmap.ic_normal_entertainment_05, R.mipmap.ic_normal_entertainment_06,
            R.mipmap.ic_normal_entertainment_07, R.mipmap.ic_normal_entertainment_08,
            R.mipmap.ic_normal_entertainment_09, R.mipmap.ic_normal_entertainment_10,
            R.mipmap.ic_normal_entertainment_11, R.mipmap.ic_normal_entertainment_12,
            R.mipmap.ic_normal_entertainment_13, R.mipmap.ic_normal_entertainment_14,
            R.mipmap.ic_normal_entertainment_15, R.mipmap.ic_normal_entertainment_16,
            R.mipmap.ic_normal_entertainment_17, R.mipmap.ic_normal_entertainment_18,
            R.mipmap.ic_normal_family_01, R.mipmap.ic_normal_family_02,
            R.mipmap.ic_normal_family_03, R.mipmap.ic_normal_family_04,
            R.mipmap.ic_normal_family_05, R.mipmap.ic_normal_family_06,
            R.mipmap.ic_normal_family_07,
            R.mipmap.ic_normal_fitness_01,
            R.mipmap.ic_normal_fitness_02, R.mipmap.ic_normal_fitness_03,
            R.mipmap.ic_normal_fitness_04, R.mipmap.ic_normal_fitness_05,
            R.mipmap.ic_normal_fitness_06,
            R.mipmap.ic_normal_food_01, R.mipmap.ic_normal_food_02,
            R.mipmap.ic_normal_food_03, R.mipmap.ic_normal_food_04,
            R.mipmap.ic_normal_food_05, R.mipmap.ic_normal_food_06,
            R.mipmap.ic_normal_food_07, R.mipmap.ic_normal_food_08,
            R.mipmap.ic_normal_food_09, R.mipmap.ic_normal_food_10,
            R.mipmap.ic_normal_food_11, R.mipmap.ic_normal_food_12,
            R.mipmap.ic_normal_food_13, R.mipmap.ic_normal_food_14,
            R.mipmap.ic_normal_food_15, R.mipmap.ic_normal_food_16,
            R.mipmap.ic_normal_food_17, R.mipmap.ic_normal_food_18,
            R.mipmap.ic_normal_food_19,
            R.mipmap.ic_normal_furniture_01, R.mipmap.ic_normal_furniture_02,
            R.mipmap.ic_normal_furniture_03, R.mipmap.ic_normal_furniture_04,
            R.mipmap.ic_normal_furniture_05, R.mipmap.ic_normal_furniture_06,
            R.mipmap.ic_normal_furniture_07, R.mipmap.ic_normal_furniture_08,
            R.mipmap.ic_normal_furniture_09, R.mipmap.ic_normal_furniture_10,
            R.mipmap.ic_normal_furniture_11, R.mipmap.ic_normal_furniture_12,
            R.mipmap.ic_normal_furniture_13, R.mipmap.ic_normal_furniture_14,
            R.mipmap.ic_normal_furniture_15, R.mipmap.ic_normal_furniture_16,
            R.mipmap.ic_normal_furniture_16,
            R.mipmap.ic_normal_income_01, R.mipmap.ic_normal_income_02,
            R.mipmap.ic_normal_income_03,
            R.mipmap.ic_normal_life_01, R.mipmap.ic_normal_life_02,
            R.mipmap.ic_normal_life_03, R.mipmap.ic_normal_life_04,
            R.mipmap.ic_normal_medical_01, R.mipmap.ic_normal_medical_02,
            R.mipmap.ic_normal_medical_03, R.mipmap.ic_normal_medical_04,
            R.mipmap.ic_normal_medical_05, R.mipmap.ic_normal_medical_06,
            R.mipmap.ic_normal_medical_07, R.mipmap.ic_normal_medical_08,
            R.mipmap.ic_normal_personal_01, R.mipmap.ic_normal_personal_02,
            R.mipmap.ic_normal_personal_03, R.mipmap.ic_normal_personal_04,
            R.mipmap.ic_normal_shopping_01, R.mipmap.ic_normal_shopping_02,
            R.mipmap.ic_normal_shopping_03, R.mipmap.ic_normal_shopping_04,
            R.mipmap.ic_normal_shopping_05, R.mipmap.ic_normal_shopping_06,
            R.mipmap.ic_normal_shopping_07, R.mipmap.ic_normal_shopping_08,
            R.mipmap.ic_normal_shopping_09, R.mipmap.ic_normal_shopping_10,
            R.mipmap.ic_normal_shopping_11, R.mipmap.ic_normal_shopping_12,
            R.mipmap.ic_normal_shopping_13, R.mipmap.ic_normal_shopping_14,
            R.mipmap.ic_normal_shopping_15, R.mipmap.ic_normal_shopping_16,
            R.mipmap.ic_normal_shopping_17, R.mipmap.ic_normal_shopping_18,
            R.mipmap.ic_normal_shopping_19, R.mipmap.ic_normal_shopping_20,
            R.mipmap.ic_normal_shopping_21, R.mipmap.ic_normal_shopping_22,
            R.mipmap.ic_normal_transportation_01, R.mipmap.ic_normal_transportation_02,
            R.mipmap.ic_normal_transportation_03, R.mipmap.ic_normal_transportation_04,
            R.mipmap.ic_normal_transportation_05, R.mipmap.ic_normal_transportation_06,
            R.mipmap.ic_normal_transportation_07, R.mipmap.ic_normal_transportation_08,
            R.mipmap.ic_normal_transportation_08, R.mipmap.ic_normal_transportation_09,
            R.mipmap.ic_normal_transportation_10, R.mipmap.ic_normal_transportation_11,
            R.mipmap.ic_normal_transportation_12, R.mipmap.ic_normal_transportation_13,
            R.mipmap.ic_normal_transportation_14, R.mipmap.ic_normal_transportation_15,
            R.mipmap.ic_normal_transportation_16,
            R.mipmap.ic_category_others_normal,
            R.mipmap.ic_category_add};
    public TranscationAdapter(Activity activity, List<ModelTransaction> modelArrayList, ItemClick onClick) {

        this.activity=activity;
        this.modelTransactions=modelArrayList;
        this.onClick=onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (modelTransactions.get(position).getTransaction_type().equals("EXPENSE")) {
            holder.txtAmount.setTextColor(activity.getResources().getColor(R.color.redColor));
        } else {
            holder.txtAmount.setTextColor(activity.getResources().getColor(R.color.greenColor));
        }
        holder.txtAmount.setText(modelTransactions.get(position).getAmount().toString());
        holder.txtDescription.setText(modelTransactions.get(position).getNote().toString());
        holder.txtTime.setText(modelTransactions.get(position).getDatentime().toString());
        holder.txtCategory.setText(modelTransactions.get(position).getCategory_type().toString());

        img = modelTransactions.get(position).getImage_pos();
        holder.ivLogo.setImageResource(image[img]);

        holder.relCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelTransactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategory,txtDescription,txtAmount,txtTime;
        ImageView ivLogo;
        LinearLayout relCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategory=itemView.findViewById(R.id.txtCategory);
            txtDescription=itemView.findViewById(R.id.txtDescription);
            txtAmount=itemView.findViewById(R.id.txtAmount);
            txtTime=itemView.findViewById(R.id.txtTime);
            ivLogo=itemView.findViewById(R.id.ivLogo);
            relCategory=itemView.findViewById(R.id.relCategory);
        }
    }
}
