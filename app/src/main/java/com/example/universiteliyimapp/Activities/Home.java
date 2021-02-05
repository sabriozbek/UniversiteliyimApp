package com.example.universiteliyimapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.universiteliyimapp.Fragments.BlankFragment;
import com.example.universiteliyimapp.Fragments.HomeFragment;
import com.example.universiteliyimapp.Fragments.ProfileFragment;
import com.example.universiteliyimapp.Fragments.SettingsFragment;
import com.example.universiteliyimapp.Models.Post;
import com.example.universiteliyimapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   private static final int PRegCode=2;
    private static final int REQUESCODE =2 ;

    DrawerLayout drawer;

FirebaseUser currentUser;
FirebaseAuth mAuth;

Dialog popAddPost;
ImageView popupUserImage,popupPostImage,popupAddBtn;
EditText popupTitle,popupDescription;
ProgressBar popupProgressBar;
Spinner spinner;
    String selectedItem;
    String UDisplayName;
    private Uri pickedImgUri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new BlankFragment()).commit();

       // getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();





        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
if(savedInstanceState==null){
    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new HomeFragment()).commit();
setTitle("Üniversiteliyim");

    navigationView.setCheckedItem(R.id.nav_home);
}

mAuth=FirebaseAuth.getInstance();
currentUser=mAuth.getCurrentUser();

//popup
        iniPopup();



        setupPopupImageClick();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                popAddPost.show();
               // MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner1);
                //spinner.setItems("saasad","fgsdfgsdf");
               // spinner.setText("Seçim Yap");

            }
        });
        //DİKAATTTTTTT


    }

    private void setupPopupImageClick() {

popupPostImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //gallery açılacak
checkAndRequestForPermission();



    }
});




    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(Home.this,"Lütfen izin veriniz.",Toast.LENGTH_LONG).show();
            }
            else{

                ActivityCompat.requestPermissions(Home.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PRegCode);
            }
        }
        else
            openGallery();

    }

    private void openGallery() {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedImgUri=data.getData();
popupPostImage.setImageURI(pickedImgUri);
        }
    }







    private void iniPopup() {
        popAddPost=new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity= Gravity.TOP;

//popup işlemleri
        popupUserImage=popAddPost.findViewById(R.id.popup_user_image);
        popupPostImage=popAddPost.findViewById(R.id.popup_img);
        popupAddBtn=popAddPost.findViewById(R.id.popup_add);
        popupTitle=popAddPost.findViewById(R.id.pop_title);
        popupDescription=popAddPost.findViewById(R.id.pop_desc);
popupProgressBar=popAddPost.findViewById(R.id.popup_progressbar);
        spinner=popAddPost.findViewById(R.id.spinner1);
       // Spinner popupSpinner = new Spinner(getApplicationContext(), Spinner.MODE_DIALOG);

//load resim
        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

//bu kullanıma bakkkk



        //spinner








    //final  LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


      // String[] country = { "Ankara", "İstanbul", "Tunceli", "İzmir", "Tokat",  };
       //spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
      // ArrayAdapter<String > aa=new ArrayAdapter<String>(layoutInflater.getContext(),android.R.layout.simple_spinner_item,country);
       //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(aa);
        updateNavHeader();

      // @SuppressLint("ResourceType") ArrayAdapter <CharSequence> adapter = new ArrayAdapter(getLayoutInflater().inflate(),R.array.Spinner_items,android.R.layout.simple_spinner_item);
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
              R.array.Spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       // adapter.add("item 1");
       // adapter.add("item 2");
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
showMessage("Bir üniversite seçiniz.");
            }
        });

       // spinner.setOnItemSelectedListener(Home.this);


      //ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,R.layout.popup_add_post,);
     //   dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     //  spinner.setAdapter(dataAdapter);



// myspinner.showAsDialog()
//ListView Deneme



//add post click
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupProgressBar.setVisibility(View.VISIBLE);
                popupAddBtn.setVisibility(View.INVISIBLE);
if (!popupTitle.getText().toString().isEmpty()&& !popupDescription.getText().toString().isEmpty()&&pickedImgUri!=null){


    //TODO herşey tamam kontrol ettik post oluşturalım
    StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("blog_images");
    final StorageReference imageFilePath=storageReference.child(pickedImgUri.getLastPathSegment());
    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
    @Override
    public void onSuccess(Uri uri) {
        String imageDownloadLink=uri.toString();
        Post post=new Post(popupTitle.getText().toString(),
               popupDescription.getText().toString(),
                imageDownloadLink,
                currentUser.getUid(),
                currentUser.getPhotoUrl().toString(),
                selectedItem,currentUser.getDisplayName());

        //add post to firebase
       addPost(post);




    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        //wrons
        showMessage(e.getMessage());
        popupProgressBar.setVisibility(View.INVISIBLE);
        popupAddBtn.setVisibility(View.VISIBLE);


    }
});
        }
    });






}else{
    showMessage("Lütfen belirtilen alanları eksiksiz doldurunuz.");
    popupProgressBar.setVisibility(View.INVISIBLE);
    popupAddBtn.setVisibility(View.VISIBLE);
}

            }
        });

    }

    private void addPost(Post post) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("Posts").push();

String key=myRef.getKey();
post.setPostKey(key);
//add post to firebase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Gönderi Eklendi");
                popupProgressBar.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });


    }

    private void showMessage(String s) {
        Toast.makeText(Home.this,s,Toast.LENGTH_LONG).show();
    }


    public void updateNavHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
View headerView=navigationView.getHeaderView(0);
        TextView navusername=headerView.findViewById(R.id.nav_username);
TextView navMail=headerView.findViewById(R.id.nav_userMail);
ImageView navPhoto=headerView.findViewById(R.id.nav_userPhoto);
navMail.setText(currentUser.getEmail());
navusername.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navPhoto);



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportActionBar().setTitle("Üniversiteliyim");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new HomeFragment()).commit();
                break;

            case R.id.nav_profile:
                getSupportActionBar().setTitle("Profil");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new ProfileFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportActionBar().setTitle("Ayarlar");
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new SettingsFragment()).commit();
                break;
            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                Intent loginActivity =new Intent(getApplicationContext(), LoginActivity.class);
                NavigationView navigationView = findViewById(R.id.nav_view);
                View headerView=navigationView.getHeaderView(0);
                TextView navusername=headerView.findViewById(R.id.nav_username);


                SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("stringValue",navusername.getText().toString());

                editor.commit();



                loginActivity.putExtra("userName",navusername.getText());


                startActivity(loginActivity);
                finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
