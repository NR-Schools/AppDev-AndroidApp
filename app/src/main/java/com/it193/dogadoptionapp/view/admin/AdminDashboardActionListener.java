package com.it193.dogadoptionapp.view.admin;

import com.it193.dogadoptionapp.model.Dog;

public interface AdminDashboardActionListener {
    void onAddDogInfo();
    void onUpdateDogInfo(Dog dog);
    void onDeleteDogInfo();
}
