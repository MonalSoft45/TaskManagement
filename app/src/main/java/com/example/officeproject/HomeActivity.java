package com.example.officeproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.officeproject.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
  Toolbar toolbar;
  FloatingActionButton fabBtn;
//Firebase
 DatabaseReference mData;
 FirebaseAuth mAuth;
 //Recycler
 RecyclerView recyclerView;
 FirebaseRecyclerOptions<Data>options;
 FirebaseRecyclerAdapter<Data,MyViewHolder>adapter;
 //Update input field

    private EditText titleUp;
    private EditText noteUp;
    private Button btnDeleteUp;
    private Button btnUpdate;

    //Variable
    private String title;
    private String note;
    private String postKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar_home);
        fabBtn = findViewById(R.id.fab_btn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Task");


        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth mUser = FirebaseAuth.getInstance();
        String uid = mUser.getUid();
        mData = FirebaseDatabase.getInstance().getReference().child("Task Note").child(uid);
        mData.keepSynced(true);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);




        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myDialog= new AlertDialog.Builder(HomeActivity.this);

                LayoutInflater inflater =  LayoutInflater.from(HomeActivity.this);

                View myView = inflater.inflate(R.layout.costominput,null);

                myDialog.setView(myView);
                AlertDialog dialog = myDialog.create();
                dialog.show();

                final EditText et_title = myView.findViewById(R.id.et_title);
               final EditText et_note = myView.findViewById(R.id.et_note);
                Button btnAdd = myView.findViewById(R.id.btn_Add);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mtitle = et_title.getText().toString().trim();
                        String mnote = et_note.getText().toString().trim();

                        if (TextUtils.isEmpty(mtitle)){
                            et_title.setError("Required...");
                            return;
                        }if(TextUtils .isEmpty(mnote)){
                            et_note.setError("Required...");
                            return;
                        }
                        dialog.dismiss();
                        String id = mData.push().getKey();
                        String datee = DateFormat.getDateInstance().format(new Date());

                        Data data = new Data(mtitle,mnote,datee,id);
                        mData.child(id).setValue(data);
                        Toast.makeText(HomeActivity.this, "Data Insert", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

//

    }

    @Override
    protected void onStart() {
        super.onStart();
        options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(mData,Data.class).build();
        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
                holder.setTitle(model.getTitle());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());

                holder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postKey = getRef(position).getKey();
                        title= model.getTitle();
                        note = model .getNote();
                        update();
                    }
                });
            }
            //
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =   LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data,parent,false);
                return new MyViewHolder(view);
            }
//
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{
        View myview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
        }
        public void setTitle(String title){
            TextView mTitle = myview.findViewById(R.id.title);
            mTitle.setText(title);
        }
        public void setNote(String note){
            TextView mNote = myview.findViewById(R.id.note);
            mNote.setText(note);
        }
        public void setDate(String date){
            TextView mDate = myview.findViewById(R.id.date);
            mDate.setText(date);
        }
    }

    public  void update(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myview = inflater.inflate(R.layout.updateinputinfo,null);
        mydialog.setView(myview);
        AlertDialog dialog = mydialog.create();
        titleUp = myview.findViewById(R.id.et_title_update);
        noteUp = myview.findViewById(R.id.et_note_update);
        titleUp.setText(title);
        titleUp.setSelection(title.length());

        noteUp.setText(note);
        noteUp.setSelection(note.length());

        btnDeleteUp = myview.findViewById(R.id.btn_delete);
        btnUpdate = myview.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleUp.getText().toString().trim();
                note = noteUp.getText().toString().trim();

                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data = new Data(title,note,mDate,postKey);
                mData.child(postKey).setValue(data);
                dialog.dismiss();
            }
        });

        btnDeleteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child(postKey).removeValue();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}