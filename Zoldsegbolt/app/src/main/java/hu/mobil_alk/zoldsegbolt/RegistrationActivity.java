package hu.mobil_alk.zoldsegbolt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText felhasznalonevEditText;
    EditText vezeteknevEditText;
    EditText keresztnevEditText;
    EditText emailEditText;
    EditText jelszoEditText;
    EditText jelszoUjraEditText;
    EditText telefonszamEditText;
    EditText szallitasiCimEditText;


    private SharedPreferences preferences;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        felhasznalonevEditText = findViewById(R.id.regist_userNameEditText);
        vezeteknevEditText = findViewById(R.id.regist_userVezetekNevEditText);
        keresztnevEditText = findViewById(R.id.regist_userKeresztNevEditText);
        emailEditText = findViewById(R.id.regist_userEmailEditText);
        jelszoEditText = findViewById(R.id.regist_passwordEditText);
        jelszoUjraEditText = findViewById(R.id.regist_passwordAgainEditText);
        telefonszamEditText = findViewById(R.id.regist_phoneEditText);
        szallitasiCimEditText = findViewById(R.id.regist_addressEditText);


        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");


        emailEditText.setText(email);
        jelszoEditText.setText(password);
        jelszoUjraEditText.setText(password);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void registration(View view) {
        if (!isNetwork(getApplicationContext())){
            Toast.makeText(this, "Internet kapcsolat szükséges!", Toast.LENGTH_SHORT).show();
            return;
        }

        String felhasznalonev = felhasznalonevEditText.getText().toString();
        String vezeteknev = vezeteknevEditText.getText().toString();
        String keresztnev = keresztnevEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String jelszo = jelszoEditText.getText().toString();
        String jelszoUjra = jelszoUjraEditText.getText().toString();
        String telefonszam = telefonszamEditText.getText().toString();
        String szallitasiCim = szallitasiCimEditText.getText().toString();

        if (felhasznalonev.equals("") || vezeteknev.equals("") || keresztnev.equals("") ||
                email.equals("") || jelszo.equals("") || jelszoUjra.equals("") ||
                telefonszam.equals("") || szallitasiCim.equals("")){

            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!jelszo.equals(jelszoUjra)) {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítése.");
            Toast.makeText(this, "A jelszó és megerősítése nem egyezik meg!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, jelszo).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(LOG_TAG, "User created successfully");
                new_felhasznalo_feltoltese(felhasznalonev, vezeteknev, keresztnev, email, jelszo, jelszoUjra, telefonszam, szallitasiCim);
                goto_vasarlas();
            } else {
                Log.d(LOG_TAG, "User wasn't created successfully");
                Toast.makeText(RegistrationActivity.this, "Error: "
                        + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(RegistrationActivity.this, "new User adatok feltöltve", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this, "new User adatok sikertelen feltöltés", Toast.LENGTH_SHORT).show();
            }
        });
//        dbUsers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(RegistrationActivity.this, "new User adatok feltöltve", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(RegistrationActivity.this, "new User adatok sikertelen feltöltés", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void cancel(View view) {
        finish();
    }

    private void goto_vasarlas(){
        //Toast.makeText(this, "Sikeres regisztracio", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, VasarlasActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public boolean isNetwork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}