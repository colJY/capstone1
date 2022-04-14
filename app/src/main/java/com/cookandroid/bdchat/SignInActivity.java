package com.cookandroid.bdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cookandroid.bdchat.Models.Users;
import com.cookandroid.bdchat.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

//        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance(); // Firebase의 인증 Instance를 받아온다.
        firebaseDatabase = FirebaseDatabase.getInstance(); // firebse의 Database Instance를 가져온다

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("로그인 중");
        progressDialog.setMessage("잠시만 기다려주세요\n 실행중입니다."); // 로그인 프로그레스 메시지 설정


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {  //btnSignIn 버튼을 클릭했을 때
            @Override
            public void onClick(View view) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty()) // edit text 부분이 비어있지 않다면 아래를 실행
                {
                    progressDialog.show(); // 위에서 설정한 progressDialog를 보여주고,
                    mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString()) //비밀번호 기반의 계정을 로그인 하기 위해 signInWithEmailAndPassword를 사용하고,
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() { // 성공 유무를 확인한 후
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) { // progressDialog를 닫고, task가 성공했다면, if 아래를 실행시킨다.
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignInActivity.this,MainActivity.class); // SignInActivity에서 MainActivity로 화면을 전환한다,
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show(); // task 실패하면 Toast메시지를 출력한다.
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignInActivity.this, "이메일 / 패스워드를 입력하세요", Toast.LENGTH_SHORT).show();// edit text 부분이 비어있다면 Toast 메시지 출력
                }
            }
        });

        // 로그인 이후 계속 로그인 유지
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);

        }
        // 회원가입 화면으로 이동
        binding.txtClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,SingUpActivity.class);
                startActivity(intent);
            }
        });



    }


}