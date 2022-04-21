package hu.mobil_alk.zoldsegbolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class BeallitasokActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private static final int SECRET_KEY = 99;

    private User mUser;

    EditText felhasznalonevEditText;
    EditText vezeteknevEditText;
    EditText keresztnevEditText;
    EditText emailEditText;
    EditText telefonszamEditText;
    EditText szallitasiCimEditText;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beallitasok);



        felhasznalonevEditText = findViewById(R.id.b_userNameEditText);
        vezeteknevEditText = findViewById(R.id.b_userVezetekNevEditText);
        keresztnevEditText = findViewById(R.id.b_userKeresztNevEditText);
        emailEditText = findViewById(R.id.b_userEmailEditText);
        telefonszamEditText = findViewById(R.id.b_phoneEditText);
        szallitasiCimEditText = findViewById(R.id.b_addressEditText);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        uid = mAuth.getUid();



        emailEditText.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().toString());


        refresh();
    }

    private void refresh(){
        getUserData();
    }

    private void updateTextFields(User user){
        if (user != null) {
            felhasznalonevEditText.setText(user.getFelhasznalonev());
            vezeteknevEditText.setText(user.getVezeteknev());
            keresztnevEditText.setText(user.getKeresztnev());

            telefonszamEditText.setText(user.getTelefonszam());
            szallitasiCimEditText.setText(user.getSzallitasiCim());
        }
    }

    private void getUserData(){
        DocumentReference docRef = mFirestore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(LOG_TAG, "DocumentSnapshot data: " + document.getData());
                        mUser = document.toObject(User.class);

                        updateTextFields(mUser);

                    } else {
                        Log.d(LOG_TAG, "No such document");
                        new_felhasznalo_feltoltese("", "", "", mAuth.getCurrentUser().getEmail(), "", "", "", "");
                    }
                } else {
                    Log.d(LOG_TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    void new_felhasznalo_feltoltese(String felhasznalonev,
                                    String vezeteknev,
                                    String keresztnev,
                                    String email,
                                    String jelszo,
                                    String jelszoUjra,
                                    String telefonszam,
                                    String szallitasiCim){

        CollectionReference dbUsers = mFirestore.collection("Users");

        User user = new User(felhasznalonev,vezeteknev, keresztnev, email, jelszo, jelszoUjra, telefonszam, szallitasiCim);

        dbUsers.document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid().toString()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Toast.makeText(RegistrationActivity.this, "new User adatok feltöltve", Toast.LENGTH_SHORT).show();
                refresh();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(RegistrationActivity.this, "new User adatok sikertelen feltöltés", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void cancel(View view) {
        finish();
    }


    private void updateUserData(User user){
        String felhasznalonev = felhasznalonevEditText.getText().toString();
        String vezeteknev = vezeteknevEditText.getText().toString();
        String keresztnev = keresztnevEditText.getText().toString();
        String telefonszam = telefonszamEditText.getText().toString();
        String szallitasiCim = szallitasiCimEditText.getText().toString();

        if (!user.getFelhasznalonev().toString().equals(felhasznalonev)){
            mFirestore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).update("felhasznalonev", felhasznalonev)
                    .addOnFailureListener(failure -> {
//                        Toast.makeText(this, "Item " + item._getId() + " cannot be changed.", Toast.LENGTH_LONG).show();
                    });
        }
        if (!user.getVezeteknev().toString().equals(vezeteknev)){
            mFirestore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).update("vezeteknev", vezeteknev)
                    .addOnFailureListener(failure -> {
//                        Toast.makeText(this, "Item " + item._getId() + " cannot be changed.", Toast.LENGTH_LONG).show();
                    });
        }
        if (!user.getKeresztnev().toString().equals(keresztnev)){
            mFirestore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).update("keresztnev", keresztnev)
                    .addOnFailureListener(failure -> {
//                        Toast.makeText(this, "Item " + item._getId() + " cannot be changed.", Toast.LENGTH_LONG).show();
                    });
        }
        if (!user.getTelefonszam().toString().equals(telefonszam)){
            mFirestore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).update("telefonszam", telefonszam)
                    .addOnFailureListener(failure -> {
//                        Toast.makeText(this, "Item " + item._getId() + " cannot be changed.", Toast.LENGTH_LONG).show();
                    });
        }
        if (!user.getSzallitasiCim().toString().equals(szallitasiCim)){
            mFirestore.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).update("szallitasiCim", szallitasiCim)
                    .addOnFailureListener(failure -> {
//                        Toast.makeText(this, "Item " + item._getId() + " cannot be changed.", Toast.LENGTH_LONG).show();
                    });
        }


    }

    public void save(View view) {
        updateUserData(mUser);
        finish();
    }

    private void gotoMain(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }



    public void delete(View view) {
        DocumentReference documentReference = mFirestore.collection("Users").document(uid);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            gotoMain();
                            Toast.makeText(BeallitasokActivity.this, "Törölve", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }


}