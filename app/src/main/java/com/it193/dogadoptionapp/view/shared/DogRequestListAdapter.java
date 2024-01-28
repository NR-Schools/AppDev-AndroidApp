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
import com.it193.dogadoptionapp.model.Account;
import com.it193.dogadoptionapp.model.Dog;
import com.it193.dogadoptionapp.retrofit.DogApi;
import com.it193.dogadoptionapp.storage.AppStateStorage;
import com.it193.dogadoptionapp.utils.NotificationUtility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogRequestListAdapter extends BaseAdapter {

    DogApi dogApi;
    Context ctx;
    LayoutInflater inflater;
    List<Dog> dogRequestList;
    boolean isAdmin;

    public DogRequestListAdapter(Context ctx, List<Dog> dogRequestList, DogApi dogApi) {
        this.ctx = ctx;
        this.dogRequestList = dogRequestList;
        this.inflater = LayoutInflater.from(ctx);
        this.dogApi = dogApi;

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
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();
        Dog dog = new Dog();
        dog.setId(dogRequestList.get(position).getId());
        dog.setAdoptAccepted(false);
        dog.setAdoptRequested(dogRequestList.get(position).isAdoptRequested());
        dog.setAccount(dogRequestList.get(position).getAccount());

        dogApi.adminConfirmReq(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dog
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                NotificationUtility.successAlert(
                        ctx,
                        "Admin Reject Dog Request Successful"
                );
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                NotificationUtility.errorAlert(
                        ctx,
                        "Dog Request",
                        "Admin Reject Dog Request Failed"
                );
            }
        });
    }
    private void handleAdminAccept(int position) {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();
        Dog dog = new Dog();
        dog.setId(dogRequestList.get(position).getId());
        dog.setAdoptAccepted(true);
        dog.setAdoptRequested(dogRequestList.get(position).isAdoptRequested());
        dog.setAccount(dogRequestList.get(position).getAccount());

        dogApi.adminConfirmReq(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dog
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                NotificationUtility.successAlert(
                        ctx,
                        "Admin Accept Dog Request Successful"
                );
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                NotificationUtility.errorAlert(
                        ctx,
                        "Dog Request",
                        "Admin Accept Dog Request Failed"
                );
            }
        });
    }
    private void handleUserCancel(int position) {
        Account currentAccount = AppStateStorage.getInstance().getActiveAccount();
        Dog dog = new Dog();
        dog.setId(dogRequestList.get(position).getId());
        dog.setAdoptAccepted(dogRequestList.get(position).isAdoptAccepted());
        dog.setAdoptRequested(false);
        dog.setAccount(dogRequestList.get(position).getAccount());

        dogApi.userCancelDogAdoptRequest(
                currentAccount.getEmail(),
                currentAccount.getSessionAuthString(),
                dog
        ).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                NotificationUtility.successAlert(
                        ctx,
                        "User Cancel Dog Request Successful"
                );
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                NotificationUtility.errorAlert(
                        ctx,
                        "Dog Request",
                        "User Cancel Dog Request Failed"
                );
            }
        });
    }
}
