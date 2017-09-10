package com.example.vnprk.prisontrainings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by VNPrk on 30.04.2017.
 */

public class TrainingRecyclerAdapter extends RecyclerView.Adapter<TrainingRecyclerAdapter.TrainingViewHolder>{

    Context context;
    TrainingClickListener trainingClickListener;
    DataResCompound dataRes;
    private SparseBooleanArray selectedItems;

    class TrainingViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrainName;
        TextView tvTrainLevel;
        TextView tvTrainNeed;
        TextView tvTrainMy;
        ImageView ivTrainIcon;

        TrainingViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        void initViews(View itemView) {
            tvTrainName = (TextView)itemView.findViewById(R.id.tv_tr_name);
            tvTrainLevel = (TextView)itemView.findViewById(R.id.tv_tr_level);
            tvTrainNeed = (TextView)itemView.findViewById(R.id.tv_tr_need);
            tvTrainMy = (TextView)itemView.findViewById(R.id.tv_tr_my);
            ivTrainIcon = (ImageView)itemView.findViewById(R.id.iv_icon_train);
        }
    }

    List<ClassTraining> data;

    public TrainingRecyclerAdapter(Context _context, List<ClassTraining> data, TrainingClickListener _listener) {
        this.data = data;
        context = _context;
        trainingClickListener = _listener;
        selectedItems = new SparseBooleanArray();
        dataRes = DataResCompound.getInstance();
    }

    public void setData(List<ClassTraining> trainings){
        data=trainings;
        notifyDataSetChanged();
    }

    public void toggleSelection(int pos) {
        clearSelections();
        selectedItems.put(pos, true);
        notifyItemChanged(pos);
    }

    public int getSelection() {
        return selectedItems.keyAt(0);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    @Override
    public TrainingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);
        final ImageView ivTrainIcon = (ImageView)view.findViewById(R.id.iv_icon_train);
        final TrainingViewHolder viewHolder = new TrainingViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = viewHolder.getAdapterPosition();
                trainingClickListener.onTrainingClick(ivTrainIcon, position);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                int position = viewHolder.getAdapterPosition();
                trainingClickListener.onTrainingLongClick(ivTrainIcon, position);
                return false;
            }
        });
        return viewHolder/*new NoteViewHolder(view)*/;
    }

    @Override
    public void onBindViewHolder(TrainingViewHolder holder, int i) {
        holder.tvTrainName.setText(dataRes.getTextRes(data.get(i).getIdName()) /*context.getString(
                context.getResources().getIdentifier(data.get(i).getIdName(),"string",context.getApplicationContext().getPackageName()))*/);
        holder.tvTrainLevel.setText(Integer.toString(data.get(i).getLvlTrening()));
        holder.tvTrainNeed.setText(data.get(i).getStrNeedAttempts());
        holder.tvTrainMy.setText(data.get(i).getOldStrAttempts());
        holder.ivTrainIcon.setImageDrawable(dataRes.getDrawableRes(data.get(i).getIdImage())/*ContextCompat.getDrawable(context, context.getResources().getIdentifier(data.get(i).getIdImage(),"drawable",context.getApplicationContext().getPackageName()))*/);
        holder.itemView.setActivated(selectedItems.get(i, false));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public interface TrainingClickListener {
        void onTrainingClick(View trainImage, int position);
        void onTrainingLongClick(View trainImage, int position);
    }
}

