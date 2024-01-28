package com.it193.dogadoptionapp.view.shared;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
        convertView = inflater.inflate(
                R.layout.item_dog_request_info,
                null
        );

        // Fetch Views
        ImageView dogImageView = convertView.findViewById(R.id.itemDogRequestImageDisplay);
        TextView dogNameField = convertView.findViewById(R.id.itemDogRequestNameField);
        TextView userNameField = convertView.findViewById(R.id.itemDogRequestUserNameField);
        LinearLayout adminContainer = convertView.findViewById(R.id.itemDogRequestAdminActionContainer);
        LinearLayout userContainer = convertView.findViewById(R.id.itemDogRequestUserActionContainer);
        Button adminReject = convertView.findViewById(R.id.itemDogRequestAdminRejectRequest);
        Button adminAccept = convertView.findViewById(R.id.itemDogRequestAdminAcceptRequest);
        Button userCancel = convertView.findViewById(R.id.itemDogRequestUserCancelRequest);

        // Set Values
        dogImageView.setImageBitmap(
                BitmapFactory.decodeByteArray(
                        dogRequestList.get(position).getPhotoBytes(),
                        0,
                        dogRequestList.get(position).getPhotoBytes().length
                )
        );
        dogNameField.setText(dogRequestList.get(position).getName());
        userNameField.setText(dogRequestList.get(position).getAccount().getEmail());

        if (isAdmin) {
            userContainer.setVisibility(View.GONE);
            adminReject.setOnClickListener(v -> handleAdminReject(position));
            adminAccept.setOnClickListener(v -> handleAdminAccept(position));
        }
        else {
            adminContainer.setVisibility(View.GONE);
            userCancel.setOnClickListener(v -> handleUserCancel(position));
        }

        return convertView;
    }

    // Handle Actions
    private void handleAdminReject(int position) {
        DogRequestRepository
                .getRepository(ctx)
                .adminConfirmRequest(
                        dogRequestList.get(position),
                        false
                )
                .setCallback((a, b) -> {});
    }

    private void handleAdminAccept(int position) {
        DogRequestRepository
                .getRepository(ctx)
                .adminConfirmRequest(
                        dogRequestList.get(position),
                        true
                )
                .setCallback((a, b) -> {});
    }
    private void handleUserCancel(int position) {
        DogRequestRepository
                .getRepository(ctx)
                .userCancelDogRequest(
                        dogRequestList.get(position)
                )
                .setCallback((a, b) -> {});
    }
}
