package com.cookandroid.bdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.cookandroid.bdchat.Adapter.ChatAdapter;
import com.cookandroid.bdchat.Models.MessageModel;
import com.cookandroid.bdchat.Models.Users;
import com.cookandroid.bdchat.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String[] sendName = new String[1];
        database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Users value = dataSnapshot.getValue(Users.class);
                sendName[0] = value.getUserName();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w( "Failed to read value.", error.toException());
            }
        });


        final String senderId= auth.getUid();
        String recieveId = getIntent().getStringExtra("userId"); // userId, userName 받아오기
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic"); // 추후 프로필 사진 사용할때 사용할 예정



        binding.userName.setText(userName);
        database.getReference().child("Users").child(recieveId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfilePic())
                                .placeholder(R.drawable.avatar)
                                .into(binding.profileImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // 보내는 버튼
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class); // 뒤로가기 버튼 눌렀을 때 화면 전환
                startActivity(intent);

            }


        });

        // 메시지와 메시지 ui연결
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,
                this,recieveId,sendName[0]);

        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // 수평,수직으로 배치시켜주는 레이아웃 매니저
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recieveId; // senderId와 recieveId 순서로 합친 String으로 Firebase database의 senderRoom과 receiverRoom을 나눈다.
        final String receiverRoom = recieveId + senderId;






        database.getReference().child("chats").child(senderRoom) // 앞서 users처럼 "chat"에 senderRoom에 저장
                .addValueEventListener(new ValueEventListener() { // 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기합니다.

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear(); // 배열을 리셋시킨다.
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){ // for문을 이용해 dataSnapshot 안의 내용을 처음부터 끝까지 꺼낼 수 있는 문장을 만들고, 그것은 snapshot이라는 변수에 들어간다
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // 메시지 보내기 클릭 버튼
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.enterMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                model.setName(sendName[0]);
                binding.enterMessage.setText("");

                // real time DB에 User 밑에 chat에 생성 및 받는이 보낸이 채팅 연동
                database.getReference().child("chats")
                        .child(senderRoom).push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom).push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });

            }
        });{

        }

    }
}