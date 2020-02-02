package com.example.easymechproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Cursor cursor;
    private DatabaseReference easyMechDBRef;
    private DatabaseReference easyMechDriverRef;
    private FirebaseAuth easyMechAuth;
    private FirebaseUser easyMechCurrentUser;



    private TextView user_name, user_email, user_phone, profile_names, profile_emails;
    private Button user_update, user_logout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        openHelper = new DatabaseHelper(getContext());
        db=openHelper.getReadableDatabase();
        user_name = (TextView) rootView.findViewById(R.id.user_account_name);
        user_email = (TextView) rootView.findViewById(R.id.user_email);
        user_phone = (TextView) rootView.findViewById(R.id.user_phone);
        profile_emails = (TextView) rootView.findViewById(R.id.profile_email);
        profile_names = (TextView) rootView.findViewById(R.id.profile_name);

        easyMechAuth = FirebaseAuth.getInstance();
        easyMechCurrentUser = easyMechAuth.getCurrentUser();
        easyMechDriverRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(easyMechCurrentUser.getUid());

        easyMechDBRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(easyMechCurrentUser.getUid());
        easyMechDBRef.keepSynced(true);

        easyMechDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                //final String image = dataSnapshot.child("image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                //String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                String phone = dataSnapshot.child("mobile").getValue().toString();
                String pass = dataSnapshot.child("password").getValue().toString();

                user_name.setText(name);
                user_email.setText(email);
                user_phone.setText(phone);
                profile_names.setText(name);
                profile_emails.setText(email);

/*                if(!image.equals("default")){
                    // Picasso.get().load(image).placeholder(R.drawable.default_profile).into(profilePic);

                    Picasso.get().
                            load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_profile).into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Offline works", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.default_profile).into(profilePic);
                        }
                    });

                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error Loading User Details",Toast.LENGTH_LONG).show();
            }
        });


        // user_logout = (Button)rootView.findViewById(R.id.login_btn);
      /*  cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME+ " WHERE " +DatabaseHelper.KEY_NAME+ "=? AND" +
                " "+DatabaseHelper.KEY_PASS+"=?", new String[]{"Nova","12345678"}); */
   /*     cursor = db.rawQuery("SELECT * FROM "+DatabaseHelper.TABLE_NAME, null);

        if(cursor.getCount()==0){
            Toast.makeText(getContext(),"Nothing found",Toast.LENGTH_LONG).show();
        }
        while (cursor.moveToNext()){
            user_name.setText(cursor.getString(1));
            user_email.setText(cursor.getString(3));
            user_phone.setText(cursor.getString(2));
            profile_names.setText(cursor.getString(1));
            profile_emails.setText(cursor.getString(3));
        }*/

        return rootView;
    }
}
