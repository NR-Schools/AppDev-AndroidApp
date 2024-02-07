package com.it193.dogadoptionapp.view.admin;

import android.content.Context;
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
            dogAddView.setOnClickListener(v -> goToAddDog());
        }
        else {
            convertView = inflater.inflate(
                R.layout.item_dog_info_withremove,
                null
            );
            CardView dogToUpdate = convertView.findViewById(R.id.dog_updateinfo);
            CardView dogToRemove = convertView.findViewById(R.id.dog_removebtn);
            ImageView dogImageView = convertView.findViewById(R.id.custom_item_image);
            ImageView dogRemoveViewImage = convertView.findViewById(R.id.dog_removebtnimage);
            TextView dogNameView = convertView.findViewById(R.id.custom_item_text1);
            TextView dogBreedView = convertView.findViewById(R.id.custom_item_text2);
            TextView dogAgeView = convertView.findViewById(R.id.custom_item_age);

            // Set Default Image
            dogImageView.setImageResource(R.drawable.no_dog_icon);
            dogRemoveViewImage.setImageResource(R.drawable.removebtn);

            int actualPosition = position - 1; // Adjusted position for dogList
            dogToUpdate.setOnClickListener(v -> goToUpdateDog(actualPosition));
            dogToRemove.setOnClickListener(v -> handleDogDelete(actualPosition));

            if (dogList.get(actualPosition).getPhotoBytes().length != 0) {
                // Set Actual Dog Image
                dogImageView.setImageBitmap(BitmapFactory.decodeByteArray(
                        dogList.get(actualPosition).getPhotoBytes(),
                        0,
                        dogList.get(actualPosition).getPhotoBytes().length
                ));
            }

            dogNameView.setText(dogList.get(actualPosition).getName());
            dogBreedView.setText(dogList.get(actualPosition).getBreed());
            dogAgeView.setText(dogList.get(actualPosition).getAge() + " year/s old");
        }
        return convertView;
    }

    public void goToAddDog(){
        actionListener.onAddDogInfo();
    }

    public void goToUpdateDog(int position){
        actionListener.onUpdateDogInfo(dogList.get(position));
    }

    public void handleDogDelete(int position){
        DogRepository
                .getRepository(ctx)
                .deleteDogRecord(
                        dogList.get(position).getId()
                )
                .setCallback((responseObject, errorMessage) -> {
                    actionListener.onDeleteDogInfo();
                });
    }

}
