package com.example.plantext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemApdapter extends RecyclerView.Adapter<ItemApdapter.ViewHolder> {
    Context context;
    List<ItemModel>  list;

    public ItemApdapter(Context context, List<ItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemApdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemApdapter.ViewHolder holder, int position) {
        ItemModel model=list.get(position);
        holder.title.setText(model.getItemName());
//        Glide.with(context)
//                .load(model.getItemImage())
//                .into(holder.imageView);
        Glide.with(context)
                .load(model.getItemImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Chọn tùy chọn");
                builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            // Truyền dữ liệu sang EditActivity khi một mục trong RecyclerView được chọn
                            Intent intent = new Intent(context, EditActivity.class);
                            intent.putExtra("key", model.getKey()); // key là mã của mục cần chỉnh sửa
                            intent.putExtra("description", model.getItemName()); // thông tin mô tả
                            intent.putExtra("image",model.getItemImage()); // đường dẫn hình ảnh
                            context.startActivity(intent);

                        }
                        else if(i==1){
                            deleteModel(model);
                        }
                    }
                });
                builder.show();
            }
        });
    }
    private void deleteModel(ItemModel model) {
        // Get the key of the item to be deleted
        String key = model.getKey();

        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        // Attempt to remove the item from the database
        databaseReference.child(key).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Removal succeeded
                        // Remove the item from the local list and notify the adapter
                        int position = list.indexOf(model);
                        if (position != -1) {
                            list.remove(position);
                            notifyItemRemoved(position);
                        }
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Removal failed
                        Toast.makeText(context, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Handle the failure case here (e.g., displaying an error message).
                    }
                });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            title=itemView.findViewById(R.id.title);
        }
    }
}
