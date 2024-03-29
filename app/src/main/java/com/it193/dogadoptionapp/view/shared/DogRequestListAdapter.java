package com.it193.dogadoptionapp.view.shared;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.it193.dogadoptionapp.R;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.repository.DogRequestRepository;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.InputUtility;

import java.util.List;

public class DogRequestListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    List<Dog> dogRequestList;
    boolean isAdmin;
    private DogRequestActionListener actionListener;


    public DogRequestListAdapter(Context ctx, List<Dog> dogRequestList, DogRequestActionListener actionListener) {
        this.ctx = ctx;
        this.dogRequestList = dogRequestList;
        this.inflater = LayoutInflater.from(ctx);
        this.actionListener = actionListener;

        // Check if Admin
        isAdmin = false;
        if (AppStateStorage.getInstance().getActiveAccount().getEmail().equals("Admin")){
            isAdmin = true;
        }
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
            TextView userEmailField = convertView.findViewById(R.id.itemDogRequestUserEmailField);
            TextView userNameField = convertView.findViewById(R.id.itemDogRequestUserNameField);
            Button adminReject = convertView.findViewById(R.id.itemDogRequestAdminRejectRequest);
            Button adminAccept = convertView.findViewById(R.id.itemDogRequestAdminAcceptRequest);

            userEmailField.setText(
                    InputUtility.truncateStringWithEllipsis(
                            dogRequestList.get(position).getAccount().getEmail(),
                            7
                    )
            );
            userNameField.setText(
                    InputUtility.truncateStringWithEllipsis(
                            dogRequestList.get(position).getAccount().getUsername(),
                            7
                    )
            );
            adminAccept.setOnClickListener(v -> handleAdminAccept(position));
            adminReject.setOnClickListener(v -> handleAdminReject(position));

        }
        else {
            convertView = inflater.inflate(
                    R.layout.item_dog_userrequest,
                    null
            );
            TextView breedField = convertView.findViewById(R.id.itemDogRequestBreedField);
            TextView ageField = convertView.findViewById(R.id.itemDogRequestAgeField);
            Button userCancel = convertView.findViewById(R.id.itemDogRequestUserCancelRequest);

            breedField.setText(dogRequestList.get(position).getBreed());
            ageField.setText(dogRequestList.get(position).getAge() + " year/s old");
            userCancel.setOnClickListener(v -> handleUserCancel(position));
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
                    actionListener.onRefreshData();
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
                    actionListener.onRefreshData();
                });
    }
    public void handleUserCancel(int position) {
        DogRequestRepository
                .getRepository(ctx)
                .userCancelDogRequest(
                        dogRequestList.get(position)
                )
                .setCallback((a, b) -> {
                    actionListener.onRefreshData();
                });
    }
}
