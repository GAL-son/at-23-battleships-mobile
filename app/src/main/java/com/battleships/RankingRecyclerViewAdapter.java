package com.battleships;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankingRecyclerViewAdapter extends RecyclerView.Adapter<RankingRecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<JSONObject> rankingItems;

    public RankingRecyclerViewAdapter(Context context, ArrayList<JSONObject> rankingItems){
        this.context = context;
        this.rankingItems = rankingItems;
    }

    @NonNull
    @Override
    public RankingRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new RankingRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.textViewPos.setText(String.valueOf(position+1));
        try {
            holder.textViewUserName.setText(String.valueOf(rankingItems.get(position).get("login")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            holder.textViewScore.setText(String.valueOf(rankingItems.get(position).get("gammerScore")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rankingItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textViewPos, textViewUserName, textViewScore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPos = itemView.findViewById(R.id.textViewPosition);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewScore = itemView.findViewById(R.id.textViewScore);
        }
    }
}
