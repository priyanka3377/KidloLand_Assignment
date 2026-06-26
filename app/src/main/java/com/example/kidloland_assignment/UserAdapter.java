package com.example.kidloland_assignment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<UserModel> userList;
    private final OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position, UserModel user);
    }

    public UserAdapter(List<UserModel> userList, OnDeleteClickListener deleteListener) {
        this.userList = userList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.tvName.setText("Name: " + user.name);
        holder.tvDob.setText("DOB: " + user.dob);
        holder.tvEmail.setText("Email: " + user.email);

        if (user.isCurrentUser) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#D8CCEC"));
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F2EDF9"));
        }

        holder.btnDelete.setOnClickListener( v -> deleteListener.onDeleteClick(holder.getBindingAdapterPosition(), user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userList.size());
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDob, tvEmail;
        CardView cardView;
        ImageView btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName   = itemView.findViewById(R.id.tvName);
            tvDob    = itemView.findViewById(R.id.tvDob);
            tvEmail  = itemView.findViewById(R.id.tvEmail);
            cardView = itemView.findViewById(R.id.cardView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
