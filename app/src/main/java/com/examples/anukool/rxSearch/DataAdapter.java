package com.examples.anukool.rxSearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<GitJsonResponse.Item> mArrayList =  new ArrayList<>();

    DataAdapter() {
    }

    void updateData(List<GitJsonResponse.Item> arrayList) {
        mArrayList.clear();
        mArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText("Login Name : " + mArrayList.get(i).getLogin());
        viewHolder.tv_score.setText("Score : " + mArrayList.get(i).getScore());
        viewHolder.tv_userId.setText("User Id : " + mArrayList.get(i).getId().toString());

    }

    @Override
    public int getItemCount() {
        return mArrayList != null ? mArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_score, tv_userId;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_login_name);
            tv_score = (TextView) view.findViewById(R.id.tv_score);
            tv_userId = (TextView) view.findViewById(R.id.tv_user_id);

        }
    }

}