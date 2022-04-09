package com.cookandroid.bdchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cookandroid.bdchat.Models.Users;
import com.cookandroid.bdchat.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.hash.Hashing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

//0329 세팅 기능 추가
public class SettingsActivity extends AppCompatActivity {
    private final int GALLERY_CODE = 25;
    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;//firebase storage

    private Toolbar mToolbar;
    // 처음 생성될 때,
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //getSupportActionBar().hide();//툴바 제거

        //Firebase 인스턴스들
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //뒤로 가기 버튼 활성화
        binding.backArrow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //저장하기 버튼 설정
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.etStatus.getText().toString().equals("")&&
                        !binding.txtUsername.getText().toString().equals("")){
                    String status = binding.etStatus.getText().toString();
                    String username = binding.txtUsername.getText().toString();

                    HashMap<String,Object> obj = new HashMap<>();
                    obj.put("userName",username);
                    obj.put("status",status);
                    //파이어베이스 DB상의 정보 수정
                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);
                }
                else{
                    Toast.makeText(SettingsActivity.this,"username과 status를 입력해주세요"
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        });


        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfilePic())
                                .placeholder(R.drawable.avatar)
                                .into(binding.profileImage);

                        binding.etStatus.setText(users.getStatus());
                        binding.txtUsername.setText(users.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //이미지 plus버튼 활성화
        binding.plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {//휴대폰 갤러리 실행
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

    }
    //firebase storage에 데이터 저장하거나 가져오는 기능 담당 코드
    //여기서는 갤러리에서 사용자가 선택한 사진이 파이어베이터 starage에 올라가도록 함
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null && requestCode==GALLERY_CODE){
            Uri sFile = data.getData(); //프로필 사진 uri 받기
            binding.profileImage.setImageURI(sFile);
            //사용자의 이미지가 저장될 firebase storage 저장공간 위치 레퍼런스
            final StorageReference reference= storage.getReference().child("profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());

            //이미지 업로드
            reference.putFile(sFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // firebase database에 사용자 부분에 사용자별로 프로필 사진 uri 저장
                            database.getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                            .child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            });
        }

    }
}