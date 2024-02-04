package com.it193.dogadoptionapp.view.admin;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;

import java.util.List;

public class AdminDogListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    List<Dog> dogList;

    public AdminDogListAdapter(Context ctx, List<Dog> dogList) {
        this.ctx = ctx;
        this.dogList = dogList;
        this.inflater = LayoutInflater.from(ctx);
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
            ImageView dogAddView = (ImageView) convertView.findViewById(R.id.adminDashboardViewToAddDogView);
            dogAddView.setImageResource(R.drawable.adddog);
            return convertView;
        } else {
        convertView = inflater.inflate(
                R.layout.item_dog_info_withremove,
                null
            );
        }

        ImageView dogImageView = (ImageView) convertView.findViewById(R.id.custom_item_image);
        ImageView dogRemoveView = (ImageView) convertView.findViewById(R.id.dog_removebtn);
        TextView dogNameView = (TextView) convertView.findViewById(R.id.custom_item_text1);
        TextView dogBreedView = (TextView) convertView.findViewById(R.id.custom_item_text2);

        // Set Default Image
        dogImageView.setImageResource(R.drawable.no_dog_icon);
        dogRemoveView.setImageResource(R.drawable.removebtn);
        position = position - 1;
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
}
