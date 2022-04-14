package com.cookandroid.bdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cookandroid.bdchat.Models.Users;
import com.cookandroid.bdchat.databinding.ActivitySingUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class SingUpActivity extends AppCompatActivity {

    ActivitySingUpBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // 계정 생성시 나오는 다이얼로그
        progressDialog = new ProgressDialog(SingUpActivity.this);
        progressDialog.setTitle("계정 생성 중 ");
        progressDialog.setMessage("계정 생성 완료");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtUsername.getText().toString().isEmpty() &&!binding.txtStuNum.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty()) // 빈 공간 있는 지 확인
                {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString()) // createUserWithEmailAndPassword에 전달하여 신규 계정을 생성합니다
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {
                                Users user = new Users(binding.txtUsername.getText().toString(),binding.txtStuNum.getText().toString(),binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString()); // user 객체를 이름, 학번, 이메일, 패스워드를 받아 생성한다.
                                String id = task.getResult().getUser().getUid(); // id에 uid를 저장한다.
                                database.getReference().child("Users").child(id).setValue(user); // firebase의 database에 "Users" 항목에 uid와 user의 value를 저장한다.

                                Toast.makeText(SingUpActivity.this, "가입되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SingUpActivity.this,SignInActivity.class); // SignUpActivity에서 SignInActivity로 화면을 전환한다,
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(SingUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(SingUpActivity.this,"올바른 값을 입력하세요",Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 이미 계정이 있습니다 페이지 전환
        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingUpActivity.this,SignInActivity.class);
                startActivity(intent);

            }
        });
    }
}