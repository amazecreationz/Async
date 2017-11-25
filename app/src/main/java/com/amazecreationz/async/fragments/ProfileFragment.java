package com.amazecreationz.async.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazecreationz.async.R;
import com.amazecreationz.async.constants.FirebaseConstants;
import com.amazecreationz.async.models.User;
import com.amazecreationz.async.services.FirebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment implements FirebaseConstants {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private User user;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final CardView cardView = view.findViewById(R.id.user_card);
        cardView.post(new Runnable()
        {
            @Override
            public void run()
            {
                ViewGroup.LayoutParams params= cardView.getLayoutParams();
                if(cardView.getHeight() < cardView.getWidth()) {
                    params.height = cardView.getWidth();
                    cardView.setLayoutParams(params);
                }
            }
        });
        TextView nameView = view.findViewById(R.id.user_name);
        nameView.setText(user.getName());
        TextView emailView = view.findViewById(R.id.user_email);
        emailView.setText(user.getEmail());
        final ProgressBar progressBar = view.findViewById(R.id.device_pbar);
        final TextView noDevicesText = view.findViewById(R.id.no_devices);
        final RecyclerView devicesList = view.findViewById(R.id.devices_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        devicesList.setLayoutManager(mLayoutManager);
        devicesList.setNestedScrollingEnabled(false);
        FirebaseService firebaseService = FirebaseService.getInstance();
        firebaseService.getDevicesRef(user).orderBy(DATE_ADDED, Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    if(docs.size() == 0) {
                        noDevicesText.setVisibility(View.VISIBLE);
                    } else {
                        devicesList.setAdapter(new DevicesViewAdapter(docs));
                        devicesList.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}

class DevicesViewAdapter extends RecyclerView.Adapter<DevicesViewAdapter.ViewHolder> implements FirebaseConstants {
    private List<DocumentSnapshot> docs;

    DevicesViewAdapter(List<DocumentSnapshot> docs) {
        this.docs = docs;
    }

    @Override
    public DevicesViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_device_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View view = holder.view;
        TextView deviceModel = view.findViewById(R.id.device_model);
        String devName = docs.get(position).getString(DEVICE_BRAND)+" "+docs.get(position).getString(DEVICE_MODEL);
        deviceModel.setText(devName);
        TextView addedDate = view.findViewById(R.id.date_added);
        String dateString = "Last Login : " + new SimpleDateFormat("dd MMM yyyy 'at' h:mm a", Locale.ENGLISH).format(new Date(docs.get(position).getLong(DATE_ADDED)));
        addedDate.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }
}