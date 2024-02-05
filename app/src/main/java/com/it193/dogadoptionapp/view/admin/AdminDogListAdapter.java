package com.it193.dogadoptionapp.view.admin;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRepository;
import com.it193.dogadoptionapp.view.shared.DogRequestListAdapter;

import java.util.List;

public class AdminDogListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    List<Dog> dogList;

    private AdminDashboardActionListener actionListener;

    public AdminDogListAdapter(Context ctx, List<Dog> dogList, AdminDashboardActionListener actionListener) {
        this.ctx = ctx;
        this.dogList = dogList;
        this.inflater = LayoutInflater.from(ctx);
        this.actionListener = actionListener;
    }

    @Override
    public int getCount() {
        return dogList.size() + 1;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == 0) {
            convertView = inflater.inflate(R.layout.item_adddog, null);
            CardView dogAddView = convertView.findViewById(R.id.adminDashboardViewToAddDogView);
            ImageView dogAddViewPhoto = convertView.findViewById(R.id.addDogItemPhoto);

            dogAddViewPhoto.setImageResource(R.drawable.adddog);
            dogAddView.setOnClickListener(v -> goToUpdateDog(0));

            return convertView;
        } else {
        convertView = inflater.inflate(
                R.layout.item_dog_info_withremove,
                null
            );
        }


        CardView dogToUpdate = convertView.findViewById((R.id.dog_updateinfo));
        CardView dogToRemove = convertView.findViewById((R.id.dog_removebtn));
        ImageView dogImageView = (ImageView) convertView.findViewById(R.id.custom_item_image);
        ImageView dogRemoveViewImage = (ImageView) convertView.findViewById(R.id.dog_removebtnimage);
        TextView dogNameView = (TextView) convertView.findViewById(R.id.custom_item_text1);
        TextView dogBreedView = (TextView) convertView.findViewById(R.id.custom_item_text2);



        // Set Default Image
        dogImageView.setImageResource(R.drawable.no_dog_icon);
        dogRemoveViewImage.setImageResource(R.drawable.removebtn);
        position = position - 1;

        int finalPosition = position;
        dogToUpdate.setOnClickListener(v -> goToUpdateDog(finalPosition));
        dogToRemove.setOnClickListener(v -> handleDogDelete(finalPosition));

        if (dogList.get(position).getPhotoBytes().length != 0) {
            // Set Actual Dog Image
            dogImageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                            dogList.get(position).getPhotoBytes(),
                            0,
                            dogList.get(position).getPhotoBytes().length
                    )
            );
        }

        dogNameView.setText(dogList.get(position).getName());
        dogBreedView.setText(dogList.get(position).getBreed());

        return convertView;
    }


    public void goToUpdateDog(int position){
        if (actionListener != null) {
            actionListener.updateDogInfo(position);
        }
    }

    public void handleDogDelete(int position){

        DogRepository
                .getRepository(ctx)
                .deleteDogRecord(
                        dogList.get(position).getId()
                )
                .setCallback((a, b) -> {});

        if (actionListener != null) {
            actionListener.deleteDogInfo();
        }
    }


    public interface AdminDashboardActionListener {
        void updateDogInfo(int position);
        void deleteDogInfo();
    }
}
