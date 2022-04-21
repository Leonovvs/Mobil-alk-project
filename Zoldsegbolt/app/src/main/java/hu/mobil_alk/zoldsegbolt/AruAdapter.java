package hu.mobil_alk.zoldsegbolt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class AruAdapter extends RecyclerView.Adapter<AruAdapter.ViewHolder> implements Filterable {
    private ArrayList<Aru> mAruItemsData;
    private ArrayList<Aru> mAruItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    AruAdapter(Context context, ArrayList<Aru> aruItemsData){
        this.mAruItemsData = aruItemsData;
        this.mAruItemsDataAll = aruItemsData;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.aru, parent, false));
    }

    @Override
    public void onBindViewHolder(AruAdapter.ViewHolder holder, int position) {
        Aru currentItem = mAruItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mAruItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return aruFilter;
    }



    private Filter aruFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Aru> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0){
                results.count = mAruItemsDataAll.size();
                results.values = mAruItemsDataAll;
            }else {
                String filterPatten = charSequence.toString().toLowerCase().trim();

                for (Aru aru : mAruItemsDataAll){
                    if (aru.getName().toLowerCase().contains(filterPatten) || aru.getInfo().toLowerCase().contains(filterPatten)){
                        filteredList.add(aru);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mAruItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mAruImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mPriceText = itemView.findViewById(R.id.price);
            mAruImage = itemView.findViewById(R.id.itemImage);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Activity", "Add cart button clicked");
                    ((VasarlasActivity)mContext).updateCartIcon();
                }
            });
        }

        public void bindTo(Aru currentItem) {
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getInfo());
            mPriceText.setText(currentItem.getPrice());


            Glide.with(mContext).load(currentItem.getImageResource()).into(mAruImage);
        }
    }

}
