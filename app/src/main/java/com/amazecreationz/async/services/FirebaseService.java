package com.amazecreationz.async.services;

import android.os.Build;

import com.amazecreationz.async.constants.FirebaseConstants;
import com.amazecreationz.async.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseService implements FirebaseConstants {
    private FirebaseFirestore dB;
    private DocumentReference asyncRef;

    public static FirebaseService getInstance() {
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.dB = FirebaseFirestore.getInstance();
        firebaseService.asyncRef = firebaseService.dB.collection(APP_DATA_REF).document(ASYNC_REF);
        return firebaseService;
    }

    public CollectionReference getDevicesRef(User user) {
        return asyncRef.collection(user.getUID()).document(DATA_REF).collection(DEVICES_REF);
    }

    public void setUserData(User user) {
        dB.collection(USERS_COLLECTION).document(user.getUID()).set(user.getUser());
    }

    public void setDeviceInfo(User user) {
        Map<String, Object> device = new HashMap<>();
        device.put(DEVICE_TYPE, ANDROID);
        device.put(DEVICE_BRAND, Build.BRAND);
        device.put(DEVICE_MODEL, Build.MODEL);
        device.put(DEVICE_PRODUCT, Build.PRODUCT);
        device.put(DATE_ADDED, new Date().getTime());
        getDevicesRef(user).document(user.getDeviceID()).set(device);
    }

    public void setData(User user) {
        setUserData(user);
        setDeviceInfo(user);
    }
}