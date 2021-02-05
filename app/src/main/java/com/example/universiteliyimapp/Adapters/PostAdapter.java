package com.example.universiteliyimapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.universiteliyimapp.Activities.PostDetailActivity;
import com.example.universiteliyimapp.Models.Post;
import com.example.universiteliyimapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {


    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row= LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);



        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTittle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
        holder.postCategories.setText(mData.get(position).getCategories());





    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

TextView tvTittle;
ImageView imgPost;
ImageView imgPostProfile;
TextView postCategories;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvTittle=itemView.findViewById(R.id.row_post_title);
            imgPost=itemView.findViewById(R.id.row_post_img);
            imgPostProfile=itemView.findViewById(R.id.row_post_profile_img);
            postCategories=itemView.findViewById(R.id.row_post_categories);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Intent postDetailActivity =new Intent(mContext, PostDetailActivity.class);
                   int positon=getAdapterPosition();
                   postDetailActivity.putExtra("title",mData.get(positon).getTitle());
                   postDetailActivity.putExtra("postImage",mData.get(positon).getPicture());
                   postDetailActivity.putExtra("descripton",mData.get(positon).getDescription());
                   postDetailActivity.putExtra("postKey",mData.get(positon).getPostKey());
                   postDetailActivity.putExtra("userPhoto",mData.get(positon).getUserPhoto());
                   postDetailActivity.putExtra("userName",mData.get(positon).getDisplayName());

                    postDetailActivity.putExtra("postCategories",mData.get(positon).getCategories());
                    Long timeStamp= (Long) mData.get(positon).getTimeStamp();
                    postDetailActivity.putExtra("postDate",timeStamp);
                    mContext.startActivity(postDetailActivity);

                }
            });

        }
    }
}
