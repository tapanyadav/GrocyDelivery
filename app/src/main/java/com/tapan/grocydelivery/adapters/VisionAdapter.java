package com.tapan.grocydelivery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.models.VisionModel;

import java.util.List;

public class VisionAdapter extends RecyclerView.Adapter<VisionAdapter.VisionViewHolder> {

    Context context;
    private List<VisionModel> visionModelList;
    private ViewPager2 viewPager2;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            visionModelList.addAll(visionModelList);
            notifyDataSetChanged();
        }
    };

    public VisionAdapter(Context context, List<VisionModel> visionModelList, ViewPager2 viewPager2) {
        this.context = context;
        this.visionModelList = visionModelList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public VisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_item_vision, parent, false);
        return new VisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisionViewHolder holder, int position) {

        holder.setItems(visionModelList.get(position));
        if (position == visionModelList.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return visionModelList.size();
    }

    public class VisionViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewVision;
        TextView textViewVisionHead, textViewVisionData;

        public VisionViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewVision = itemView.findViewById(R.id.aboutTeamVisionImage);
            textViewVisionData = itemView.findViewById(R.id.aboutTeamVisionV);
            textViewVisionHead = itemView.findViewById(R.id.aboutTeamVisionH);
        }

        void setItems(VisionModel visionModel) {

            Glide.with(context).load(visionModel.getImageVision()).placeholder(R.drawable.loading_photo).into(imageViewVision);
            textViewVisionHead.setText(visionModel.getTextVisionHead());
            textViewVisionData.setText(visionModel.getTextVisionData());
        }
    }
}
