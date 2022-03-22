package com.cookandroid.bdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cookandroid.bdchat.Adapter.FragmentsAdapter;
import com.cookandroid.bdchat.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }
    // 설정, 실험기능, 로그아웃 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // 메뉴 전개자인 MenuInflater 객체를 구하고 이 객체의 inflate 메서드를 호출하여 리소스의 메뉴를 실제 메뉴로 전개한다.
        inflater.inflate(R.menu.menu,menu); // 미리 menu.xml에 만들어둔 것을 사용
        return super.onCreateOptionsMenu(menu);
    }

    // 메뉴가 선택되었을때
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                Toast.makeText(this, "설정이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.groupChat:
                Toast.makeText(this, "실험이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                mAuth.signOut(); //firebase내에서 로그아웃
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}