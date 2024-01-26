package com.it193.dogadoptionapp.storage;

import com.it193.dogadoptionapp.model.Account;

/**
 * The use of this class "AppStateStorage" is only for persisting some info for the current session.
 * Thus, SharedPreferences will not be used here
 */
public class AppStateStorage {

    private static AppStateStorage s_instance;
    private AppStateStorage() {}
    public static AppStateStorage getInstance() {
        if (s_instance == null)
            s_instance = new AppStateStorage();
        return s_instance;
    }

    private Account currentActiveAccount;

    public void setActiveAccount(Account activeAccount) {
        currentActiveAccount = activeAccount;
    }

    public Account getActiveAccount() {
        return currentActiveAccount;
    }

    public void clearActiveAccount() {
        currentActiveAccount = null;
    }

    public boolean isActiveAccountSet() {
        return currentActiveAccount != null;
    }

}
