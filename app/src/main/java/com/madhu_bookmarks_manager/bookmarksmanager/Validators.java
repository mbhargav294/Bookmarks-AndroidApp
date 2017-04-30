package com.madhu_bookmarks_manager.bookmarksmanager;

/**
 * Created by mbhar on 4/26/2017.
 */

class Validators {
    boolean isEmailValid(String email) {
        return email.matches("[a-z][a-z0-9._]{1,20}@[a-z]{1,10}(\\.[a-z]{2,5}){1,3}");
    }

    boolean isPasswordValid(String password) {
        return password.matches("[a-zA-Z0-9]{6,}");
    }

    boolean isPasswordMatch(String psw1, String psw2){
        return psw1.equals(psw2);
    }

    boolean isNameValid(String name) {
        return name.matches("[a-zA-Z]+");
    }
}
