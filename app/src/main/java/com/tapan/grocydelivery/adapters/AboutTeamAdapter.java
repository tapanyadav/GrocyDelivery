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
import com.tapan.grocydelivery.models.AboutTeamModel;

import java.util.List;

public class AboutTeamAdapter extends RecyclerView.Adapter<AboutTeamAdapter.TeamViewHolder> {

    Context context;
    private List<AboutTeamModel> aboutTeamModelList;
    private ViewPager2 viewPager2;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            aboutTeamModelList.addAll(aboutTeamModelList);
            notifyDataSetChanged();
        }
    };

    public AboutTeamAdapter(Context context, List<AboutTeamModel> aboutTeamModelList, ViewPager2 viewPager2) {
        this.context = context;
        this.aboutTeamModelList = aboutTeamModelList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.setItems(aboutTeamModelList.get(position));
        if (position == aboutTeamModelList.size() - 3) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return aboutTeamModelList.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewTeam, imageViewBack;
        TextView textViewName, textViewDesignation;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewBack = itemView.findViewById(R.id.back_about_color);
            imageViewTeam = itemView.findViewById(R.id.aboutTeamImage);
            textViewName = itemView.findViewById(R.id.aboutTeamName);
            textViewDesignation = itemView.findViewById(R.id.aboutTeamDesignation);
        }

        void setItems(AboutTeamModel aboutTeamModel) {
            Glide.with(context).load(aboutTeamModel.getImageBack()).placeholder(R.drawable.loading_new).into(imageViewBack);
            Glide.with(context).load(aboutTeamModel.getImageTeam()).placeholder(R.drawable.loading_new).into(imageViewTeam);
            textViewName.setText(aboutTeamModel.getNameTeam());
            textViewDesignation.setText(aboutTeamModel.getTeamDesignation());
        }
    }
}
