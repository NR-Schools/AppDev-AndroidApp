package com.it193.dogadoptionapp.view.shared;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRequestRepository;
import com.it193.dogadoptionapp.storage.AppStateStorage;

import java.util.List;

public class DogRequestListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    List<Dog> dogRequestList;
    boolean isAdmin;

    Button adminReject;
    Button adminAccept;
    Button userCancel;

    public DogRequestListAdapter(Context ctx, List<Dog> dogRequestList) {
        this.ctx = ctx;
        this.dogRequestList = dogRequestList;
        this.inflater = LayoutInflater.from(ctx);

        // Check if Admin
        isAdmin = false;
        if (AppStateStorage.getInstance().getActiveAccount().getEmail().equals("Admin"))
            isAdmin = true;

    }

    @Override
    public int getCount() {
        return dogRequestList.size();
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

        if (isAdmin) {
            convertView = inflater.inflate(
                    R.layout.item_dog_adminrequest,
                    null
            );
            TextView userNameField = convertView.findViewById(R.id.itemDogRequestUserNameField);
            adminReject = convertView.findViewById(R.id.itemDogRequestAdminRejectRequest);
            adminAccept = convertView.findViewById(R.id.itemDogRequestAdminAcceptRequest);
            userNameField.setText(dogRequestList.get(position).getAccount().getEmail());

            adminReject.setOnClickListener(v -> handleAdminReject(position));
            adminAccept.setOnClickListener(v -> handleAdminAccept(position));
        }
        else {
            convertView = inflater.inflate(
                    R.layout.item_dog_userrequest,
                    null
            );
            TextView breedField = convertView.findViewById(R.id.itemDogRequestBreedField);
            userCancel = convertView.findViewById(R.id.itemDogRequestUserCancelRequest);
            breedField.setText(dogRequestList.get(position).getAccount().getEmail());


        }


        // Fetch Views
        ImageView dogImageView = convertView.findViewById(R.id.itemDogRequestImageDisplay);
        TextView dogNameField = convertView.findViewById(R.id.itemDogRequestNameField);

        // Set Values
        dogImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(
                        dogRequestList.get(position).getPhotoBytes(),
                        0,
                        dogRequestList.get(position).getPhotoBytes().length
                )
        );
        dogNameField.setText(dogRequestList.get(position).getName());



        return convertView;
    }

    // Handle Actions
    public void handleAdminReject(int position) {
        DogRequestRepository
                .getRepository(ctx)
                .adminConfirmRequest(
                        dogRequestList.get(position),
                        false
                )
                .setCallback((a, b) -> {
                    // Update the data and refresh the adapter
                    notifyDataSetChanged();
                });
    }

    public void handleAdminAccept(int position) {
        DogRequestRepository
                .getRepository(ctx)
                .adminConfirmRequest(
                        dogRequestList.get(position),
                        true
                )
                .setCallback((a, b) -> {
                    // Update the data and refresh the adapter
                    notifyDataSetChanged();
                });
    }
    public void handleUserCancel(int position) {
        DogRequestRepository
                .getRepository(ctx)
                .userCancelDogRequest(
                        dogRequestList.get(position)
                )
                .setCallback((a, b) -> {
                    // Update the data and refresh the adapter
                    notifyDataSetChanged();
                });
    }

    public Button getAdminReject() {
        return adminReject;
    }

    public Button getAdminAccept() {
        return adminAccept;
    }

    public Button getUserCancel() {
        return userCancel;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
