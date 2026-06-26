package com.example.kidloland_assignment;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserDetails extends AppCompatActivity {

    private List<UserModel> userList;
    private UserAdapter adapter;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        String currentName = getIntent().getStringExtra("name");
        String currentDob = getIntent().getStringExtra("dob");
        String currentEmail = getIntent().getStringExtra("email");

        db = openOrCreateDatabase("UserDB", MODE_PRIVATE, null);
        userList = loadUsersFromDb(currentName, currentDob, currentEmail);


        adapter = new UserAdapter(userList, this::showDeleteDialog);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            private final ColorDrawable background = new ColorDrawable(Color.parseColor("#E53935"));
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                UserModel user = userList.get(position);

                adapter.notifyItemChanged(position);
                showDeleteDialog(position, user);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (dX < 0) {
                    background.setBounds(
                            itemView.getRight() + (int) dX,
                            itemView.getTop(),
                            itemView.getRight(),
                            itemView.getBottom()
                    );
                    background.draw(c);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showDeleteDialog(int position, UserModel user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.name + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.execSQL("DELETE FROM users WHERE name=? AND dob=? AND email=?",
                            new String[]{user.name, user.dob, user.email});
                    adapter.removeItem(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private List<UserModel> loadUsersFromDb(String currentName, String currentDob, String currentEmail) {
        List<UserModel> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name, dob, email FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String dob = cursor.getString(1);
                String email = cursor.getString(2);

                boolean isCurrent = name.equals(currentName)
                        && dob.equals(currentDob)
                        && email.equals(currentEmail);
                list.add(new UserModel(name, dob, email, isCurrent));
            } while (cursor.moveToNext());
        }
        cursor.close();
        list.sort((a, b) -> Boolean.compare(b.isCurrentUser, a.isCurrentUser));
        return list;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}