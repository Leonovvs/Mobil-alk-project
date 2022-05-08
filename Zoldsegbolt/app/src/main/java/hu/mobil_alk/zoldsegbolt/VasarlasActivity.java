package hu.mobil_alk.zoldsegbolt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class VasarlasActivity extends AppCompatActivity {
    private static final String LOG_TAG = VasarlasActivity.class.getName();
    private static final int SECRET_KEY = 99;
    private static final int LAUNCH_SECOND_ACTIVITY = 1;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<Aru> mAruList;
    private AruAdapter mAdapter;

    private FrameLayout redCircle;
    private TextView contentTextView;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private int gridNumber = 1;
    private int cartNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vasarlas);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key == 1) {
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (Objects.requireNonNull(manager).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
//            Toast.makeText(this, "Detected... You're using a Tablet", Toast.LENGTH_SHORT).show();
            gridNumber = 3;
        } else {
//            Toast.makeText(this, "Detected... You're using a Mobile Phone", Toast.LENGTH_SHORT).show();
            gridNumber = 1;
        }





        if (secret_key != 99 || user == null) {
            finish();
        }


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mAruList = new ArrayList<>();

        mAdapter = new AruAdapter(this, mAruList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Aruk");

        initializeData();

        if (user == null){
            finish();
        }

    }

    private void initializeData() {
        String[] arukList = getResources().getStringArray(R.array.aru_nevek);
        String[] arukInfo = getResources().getStringArray(R.array.aru_info);
        String[] arukPrice = getResources().getStringArray(R.array.aru_arak);
        TypedArray arukImageResource = getResources().obtainTypedArray(R.array.aru_kepek);

        mAruList.clear();

        for (int i = 0; i < arukList.length; i++){
            mAruList.add(new Aru(arukList[i], arukPrice[i], arukInfo[i], arukImageResource.getResourceId(i, 0)));
        }

        arukImageResource.recycle();

        mAdapter.notifyDataSetChanged();

    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            layoutManager.setSpanCount(4);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            layoutManager.setSpanCount(gridNumber);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.vasarlas_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }



    private void gotoBeallitasok(){
        Intent i = new Intent(this, BeallitasokActivity.class);
        startActivity(i);
    }

    private void gotoHibajelentes(){
        Intent intent = new Intent(this, HibajelentesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out_button:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.settings_button:
                gotoBeallitasok();
                return true;
            case R.id.hibas_termek_button:
                gotoHibajelentes();
                return true;
            case R.id.cart:
                // TODO new activyti kosár nézet, vásárlás befejezése
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(@Nullable View view, @NonNull Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        contentTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsPanel(view, menu);
    }

    public void updateCartIcon(){
        cartNumber = (cartNumber + 1);
        if (cartNumber > 0){
            contentTextView.setText(String.valueOf(cartNumber));
        }else {
            contentTextView.setText(String.valueOf(""));
        }

        redCircle.setVisibility((cartNumber > 0) ? View.VISIBLE : View.GONE);
    }



}