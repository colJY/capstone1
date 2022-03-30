package com.cookandroid.bdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.cookandroid.bdchat.Adapter.ChatAdapter;
import com.cookandroid.bdchat.Models.MessageModel;
import com.cookandroid.bdchat.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

//그룹 채팅 기능 추가 0329
public class GroupChatActivity extends AppCompatActivity {


    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        //뒤로가기 버튼 활성화
        binding.backArrow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid(); // sender User Id 받아오기
        binding.userName.setText("Group Chat"); // 채팅방 상단에 이름을 Group Chat이라고 설정 => 수정 필요

        final ChatAdapter adapter = new ChatAdapter(messageModels,this);
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);// 수평,수직으로 배치시켜주는 레이아웃 매니저
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        //메시지 내역 보이기
        database.getReference().child("Group Chat")// 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기합니다.
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //보내기 버튼 클릭시
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = binding.enterMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());

                binding.enterMessage.setText("");// 메시지를 보낸 다음에 채팅 입력 창 초기화(비우기)
                //DB의 Group Chat 아래 메시지 저장 => 추후 수정 필요
                database.getReference().child("Group Chat").push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GroupChatActivity.this,"Message Send",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}