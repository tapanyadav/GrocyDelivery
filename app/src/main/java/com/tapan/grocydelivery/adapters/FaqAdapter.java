package com.tapan.grocydelivery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.tapan.grocydelivery.R;
import com.tapan.grocydelivery.models.FaqModel;

public class FaqAdapter extends FirestoreRecyclerAdapter<FaqModel, FaqAdapter.MyViewHolder> {

    Animation animationNext, animationCollapse, animationExpanded;
    int mExpandedPosition = -1;
    private Context context;

    public FaqAdapter(@NonNull FirestoreRecyclerOptions<FaqModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FaqModel model) {

        holder.textViewFaqQuestion.setText(model.getFaqQuestion());
        holder.textViewFaqAnswer.setText(model.getFaqAnswer());
        animationNext = AnimationUtils.loadAnimation(context, R.anim.button_rotate);
        animationCollapse = AnimationUtils.loadAnimation(context, R.anim.animation_list_collapse);
        animationExpanded = AnimationUtils.loadAnimation(context, R.anim.animation_list_expand);


        final boolean isExpanded = position == mExpandedPosition;
        if (isExpanded) {
            holder.linearLayoutExpandable.setVisibility(View.VISIBLE);
            holder.cardViewExpand.setCardBackgroundColor(context.getResources().getColor(R.color.expandCardColor, context.getTheme()));
            holder.linearLayoutExpandable.setAnimation(animationExpanded);
            holder.imageViewDownFaq.animate().setDuration(300).rotation(90);
        } else {
            holder.linearLayoutExpandable.setVisibility(View.GONE);
            holder.cardViewExpand.setCardBackgroundColor(context.getResources().getColor(R.color.cardBack, context.getTheme()));
            holder.linearLayoutExpandable.setAnimation(animationCollapse);
            holder.imageViewDownFaq.animate().setDuration(300).rotation(0);

        }
        holder.linearLayoutQuestion.setOnClickListener(v -> {
            mExpandedPosition = isExpanded ? -1 : position;
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_faq, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewFaqQuestion, textViewFaqAnswer;
        LinearLayout linearLayoutExpandable, linearLayoutQuestion;
        ImageView imageViewDownFaq;
        CardView cardViewExpand;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFaqQuestion = itemView.findViewById(R.id.text_question);
            textViewFaqAnswer = itemView.findViewById(R.id.text_answer);
            linearLayoutExpandable = itemView.findViewById(R.id.expandable_linear);
            linearLayoutQuestion = itemView.findViewById(R.id.linearQuestion);
            imageViewDownFaq = itemView.findViewById(R.id.image_down_faq);
            cardViewExpand = itemView.findViewById(R.id.cardExpand);
        }
    }
}
