package com.example.myappnetworking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myappnetworking.model.Planetary;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanetaryAdapter extends RecyclerView.Adapter<PlanetaryAdapter.PlanetaryViewHolder> {

    private final List<Planetary> planetaryItems;
    private final Context context;

    public PlanetaryAdapter(Context context, List<Planetary> planetaryItems) {
        this.context = context;
        this.planetaryItems = planetaryItems;
    }

    @NonNull
    @Override
    public PlanetaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planetary, parent, false);
        return new PlanetaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetaryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Planetary item = planetaryItems.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.dateTextView.setText(item.getDate());

        // Load image using Picasso
        Picasso.with(holder.itemView.getContext())
                .load(item.getUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = item.get_id();
                Toast.makeText(context, itemId, Toast.LENGTH_SHORT).show();
                showEditDialog(itemId, item.getTitle(), item.getUrl(), item.getDate(), position);


            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy id của item hiện tại
                String itemId = item.get_id();
                deletePlanetaryItem(itemId, position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return planetaryItems.size();
    }

    public static class PlanetaryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView dateTextView;
        Button editButton;
        Button deleteButton;

        public PlanetaryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void deletePlanetaryItem(String itemId, int position) {

        PostApiServer.POST_API_SERVER.deletePlanetaryItem(itemId)
        .enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Delete successfully!!!", Toast.LENGTH_SHORT).show();
                    // Xóa item khỏi danh sách và cập nhật RecyclerView
                    planetaryItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, planetaryItems.size());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(String itemId, String currentTitle, String currentUrl, String currentDate, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_planetary, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong dialog
        EditText titleEditText = dialogView.findViewById(R.id.editTextTitle);
        EditText urlEditText = dialogView.findViewById(R.id.editTextUrl);
        EditText dateEditText = dialogView.findViewById(R.id.editTextDate);

        // Hiển thị thông tin hiện tại trong dialog
        titleEditText.setText(currentTitle);
        urlEditText.setText(currentUrl);
        dateEditText.setText(currentDate);

        // Xử lý khi ấn nút "Save" trong dialog
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy thông tin mới từ các trường nhập
                String newTitle = titleEditText.getText().toString();
                String newUrl = urlEditText.getText().toString();
                String newDate = dateEditText.getText().toString();

                // Gọi phương thức để thực hiện hành động sửa
                editPlanetaryItem(itemId, newTitle, newUrl, newDate, position);
            }
        });

        // Xử lý khi ấn nút "Cancel" trong dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editPlanetaryItem(String itemId, String newTitle, String newUrl, String newDate, int position) {

        PostApiServer.POST_API_SERVER.editPlanetaryItem(itemId, newTitle, newUrl, newDate)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Planetary newItem = new Planetary(
                                    itemId, newTitle, newUrl, newDate, position
                            );

                            planetaryItems.set(position, newItem);
                            notifyItemChanged(position);

                            Toast.makeText(context, "Edit successfully!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Edit to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xử lý khi có lỗi trong quá trình gửi yêu cầu
                    }
                });
    }



}
