package com.example.universiteliyimapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.universiteliyimapp.Adapters.CommentAdapter;
import com.example.universiteliyimapp.Models.Comment;
import com.example.universiteliyimapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost,imgUserPost,imgCurrentUser;
    TextView txtPostDsc,txtPostDateName,txtPostTitle,txtPostCategories;
    EditText editTextComment;
Button btnAddComment;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
String PostKey;
ImageView backView;
FirebaseDatabase firebaseDatabase;
TextView postDetailUserName;
RecyclerView RvComment;
CommentAdapter commentAdapter;
List<Comment> listComment;
static String COMMENT_KEY="Comment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Window w=getWindow();
       w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//getSupportActionBar().hide();




imgPost=findViewById(R.id.post_detail_img);
imgUserPost=findViewById(R.id.post_detail_user_img);
imgCurrentUser=findViewById(R.id.post_detail_currentuser_img);
txtPostTitle=findViewById(R.id.post_detail_title);
txtPostDsc=findViewById(R.id.post_detail_desc);
txtPostDateName=findViewById(R.id.post_detail_date_name);
txtPostCategories=findViewById(R.id.post_detail_categories);
editTextComment=findViewById(R.id.post_detail_comment);
btnAddComment=findViewById(R.id.post_detail_comment_add_btn);
postDetailUserName=findViewById(R.id.post_detail_username);
firebaseAuth=FirebaseAuth.getInstance();
firebaseUser=firebaseAuth.getCurrentUser();
firebaseDatabase=FirebaseDatabase.getInstance();

RvComment=findViewById(R.id.rv_comment);
btnAddComment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        btnAddComment.setVisibility(View.INVISIBLE);
        DatabaseReference commentReference=firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
        String comment_content=editTextComment.getText().toString();
        String uid=firebaseUser.getUid();
        String uname=firebaseUser.getDisplayName();
        String uimg=firebaseUser.getPhotoUrl().toString();
        Comment comment=new Comment(comment_content,uid,uimg,uname);
        commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Yorumunu ekledim.");
                editTextComment.setText("");
btnAddComment.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Yorumunu şuan ekleyemiyorum."+e.getMessage());
            }
        });

    }
});



backView=findViewById(R.id.backView);

backView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();

    }
});


String postImage=getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);
        String postTitle=getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);
        String postCategories=getIntent().getExtras().getString("postCategories");
        txtPostCategories.setText(postCategories);

        String userP0ostImage=getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userP0ostImage).into(imgUserPost);

        String postDescripton=getIntent().getExtras().getString("descripton");
        txtPostDsc.setText(postDescripton);

Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);

PostKey=getIntent().getExtras().getString("postKey");
String date=timeStampToString(getIntent().getExtras().getLong("postDate"));
txtPostDateName.setText(date);
String displayName=getIntent().getExtras().getString("userName");
postDetailUserName.setText(displayName);

iniRvcomment();


    }

    private void iniRvcomment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));
DatabaseReference commentRef=firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
commentRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        listComment=new ArrayList<>();
        for (DataSnapshot snap:dataSnapshot.getChildren()){

            Comment comment=snap.getValue(Comment.class);
            listComment.add(comment);
        }
        commentAdapter=new CommentAdapter(getApplicationContext(),listComment);
        RvComment.setAdapter(commentAdapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }

    private void showMessage(String s) {

        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    private String timeStampToString(Long time){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH) ;
        calendar.setTimeInMillis(time);
        String date= DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;

    }
}