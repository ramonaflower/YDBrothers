package com.example.ramona.ydbrothers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ramona.ydbrothers.Entity.DebtEntities;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.Models.Models;
import com.example.ramona.ydbrothers.R;
import com.example.ramona.ydbrothers.SQLite.MyDatabaseHelper;

import java.util.List;

/**
 * Created by aideo on 3/21/2017.
 */

public class AdapterRVListDebtor extends RecyclerView.Adapter<AdapterRVListDebtor.MyViewHolder> {
    private List<DebtEntities> listDebt;
    private Context context;
    private MyDatabaseHelper db;
    private UserEntities entities = new UserEntities();
    clickListener clickListener;

    //Toast.makeText(context, "This person hasn't lended anyone money yet!", Toast.LENGTH_SHORT).show();
    public AdapterRVListDebtor(List<DebtEntities> listDebt, Context context, MyDatabaseHelper db) {
        this.listDebt = listDebt;
        this.context = context;
        this.db = db;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lt_item_deb, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (db.getUserById(listDebt.get(position).getsDebtorID()) != null) {
            entities = db.getUserById(listDebt.get(position).getsDebtorID());
            holder.TVDebt.setText(Models.formatAmount(listDebt.get(position).getiDebt()) + " VND");
            holder.TVDebtUserName.setText(entities.getsUserName().toString());
            Glide.with(context).load(entities.getsUserImage().toString()).into(holder.IVDebtUser);
        }

    }

    @Override
    public int getItemCount() {
        return listDebt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TVDebt, TVDebtUserName;
        public ImageView IVDebtUser;

        public MyViewHolder(View itemView) {
            super(itemView);
            TVDebt = (TextView) itemView.findViewById(R.id.TVDebt);
            TVDebtUserName = (TextView) itemView.findViewById(R.id.TVDebUserName);
            IVDebtUser = (ImageView) itemView.findViewById(R.id.IVDebUser);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.OnItemClickListener(getLayoutPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.OnLongItemClickListener(getLayoutPosition());
                    return false;
                }
            });
        }

    }

    public void setOnItemClickListener(clickListener listener) {
        clickListener = listener;
    }

    public interface clickListener {
        void OnItemClickListener(int position);

        void OnLongItemClickListener(int position);
    }

}
