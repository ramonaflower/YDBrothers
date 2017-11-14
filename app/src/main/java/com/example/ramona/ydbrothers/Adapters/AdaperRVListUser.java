package com.example.ramona.ydbrothers.Adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ramona.ydbrothers.Entity.UserEntities;
import com.example.ramona.ydbrothers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aideo on 3/21/2017.
 */

public class AdaperRVListUser extends RecyclerView.Adapter<AdaperRVListUser.MyViewHolder> implements Filterable {
    private List<UserEntities> listUser = null;
    private List<UserEntities> listTemp = null;
    private Context context;
    clickListener clickListener;
    //MyDatabaseHelper db;
    ItemFilter itemFilter;

    public AdaperRVListUser(List<UserEntities> listUser, Context context) {
        //db= new MyDatabaseHelper(context);
        this.listUser = listUser;
        this.listTemp = listUser;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lt_item_list_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UserEntities entities = listUser.get(position);
        holder.tvUserName.setText(entities.getsUserName().toString());
        Glide.with(context).load(entities.getsUserImage().toString()).into(holder.ivUserImg);
        holder.ivPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.ivPopupMenu, entities.getsUserID(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    @Override
    public Filter getFilter() {
        if (itemFilter == null) {
            itemFilter = new ItemFilter();
        }
        return itemFilter;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivUserImg, ivPopupMenu;
        public TextView tvUserName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivUserImg = (ImageView) itemView.findViewById(R.id.IMUserImg);
            tvUserName = (TextView) itemView.findViewById(R.id.TVUserName);
            ivPopupMenu = (ImageView) itemView.findViewById(R.id.popupMenu);

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


    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<UserEntities> filterList = new ArrayList<UserEntities>();
                for (int i = 0; i < listTemp.size(); i++) {
                    UserEntities entities = listTemp.get(i);
                    if (entities.getsUserName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(entities);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = listTemp.size();
                results.values = listTemp;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listUser = (List<UserEntities>) results.values;
            notifyDataSetChanged();
        }
    }

    private void showPopupMenu(View view, String s, int p) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_menu_listuser, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new OnMenuClickListener(s, p));
        popupMenu.show();
    }


    private class OnMenuClickListener implements PopupMenu.OnMenuItemClickListener {
        String s;
        int p;

        public OnMenuClickListener(String s, int p) {
            this.s = s;
            this.p = p;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.detailUser:
                    clickListener.onDetailClick(s);
                    return true;
                case R.id.deleteUser:
                    clickListener.onDeleteClick(s, p);
                    return true;
                default:
            }
            return false;
        }
    }

    public interface clickListener {
        void OnItemClickListener(int position);

        void OnLongItemClickListener(int position);

        void onDetailClick(String s);

        void onDeleteClick(String s, int position);
    }
}
