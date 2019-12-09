package com.liufeismart.animationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liufeismart.animationdemo.animation1.activity.Animation1Activity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> data = new ArrayList<>();

    private Class[] activities = {
            Animation1Activity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = this.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter mainAdapter = new MainAdapter(data);
        data.add("RecyclerViewItem点击动画效果-仿新浪微博");
        rv.setAdapter(mainAdapter);
    }


    class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

        List<String> data;

        public MainAdapter(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, null);
            return new MainViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, final int i) {
            mainViewHolder.tv_content.setText(data.get(i));
            mainViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, activities[i]));
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MainViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_content;

            public MainViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_content = itemView.findViewById(R.id.tv_content);
            }
        }
    }
}
