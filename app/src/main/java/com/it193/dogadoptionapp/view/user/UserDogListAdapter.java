package com.it193.dogadoptionapp.view.user;

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

public class UserDogListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater inflater;
    List<Dog> dogList;

    public UserDogListAdapter(Context ctx, List<Dog> dogList) {
        this.ctx = ctx;
        this.dogList = dogList;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return dogList.size();
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
        convertView = inflater.inflate(
                R.layout.item_dog_info,
                null
        );

        ImageView dogImageView = (ImageView) convertView.findViewById(R.id.custom_item_image);
        TextView dogNameView = (TextView) convertView.findViewById(R.id.custom_item_text1);
        TextView dogBreedView = (TextView) convertView.findViewById(R.id.custom_item_text2);

        dogImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(
                        dogList.get(position).getPhotoBytes(),
                        0,
                        dogList.get(position).getPhotoBytes().length
                )
        );
        dogNameView.setText(dogList.get(position).getName());
        dogBreedView.setText(dogList.get(position).getBreed());

        return convertView;
    }
}
