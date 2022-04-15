package hu.mobil_alk.zoldsegbolt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class VasarlasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vasarlas);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }
    }
}