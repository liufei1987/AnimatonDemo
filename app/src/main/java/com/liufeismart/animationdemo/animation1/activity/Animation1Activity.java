package com.liufeismart.animationdemo.animation1.activity;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liufeismart.animationdemo.R;
import com.liufeismart.animationdemo.animation.AnimationOnMoveItem;
import com.liufeismart.animationdemo.animation1.adapter.Animation1Adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Animation1Activity extends Activity implements  Animation1Adapter.OnItemClickCallback {

    private RecyclerView rv_1, rv_2;
    private Animation1Adapter adapter1, adapter2;
    List<String> data1 = new ArrayList<>(), data2 = new ArrayList<>();

    private AnimationOnMoveItem animationOnMoveItem;
    private String contentVisible;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation1);
        rv_1 = this.findViewById(R.id.rv_1);
        rv_2 = this.findViewById(R.id.rv_2);
        //rv_1
        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(this, 4);
        rv_1.setLayoutManager(layoutManager1);
        data1.add("推荐");
        data1.add("明星");
        data1.add("法律");
        data1.add("旅游");
//        Animation1Adapter.OnItemClickCallback  onItemClickCallback = new Animation1Adapter.OnItemClickCallback() {
//            @Override
//            public void onClick(RecyclerView.Adapter adapter, int index) {
//                if(adapter == adapter1) {
//                    moveItem(rv_2, rv_1, data2, data1, index);
//                }
//                else if(adapter == adapter2) {
//                    moveItem(rv_1, rv_2, data1, data2, index);
//                }
//
//            }
//        };
        adapter1 = new Animation1Adapter(data1, this);
        rv_1.setAdapter(adapter1);
        //rv_2
        RecyclerView.LayoutManager layoutManager2 = new GridLayoutManager(this, 4);

        rv_2.setLayoutManager(layoutManager2);
        data2.add("搞笑");
        data2.add("综艺");
        data2.add("美食");
        data2.add("园艺");
        adapter2 = new Animation1Adapter(data2, this);
        rv_2.setAdapter(adapter2);
    }

    public void moveItem(final RecyclerView moveInRV, final RecyclerView moveOutRV,
                         final List moveInDataList, final List moveOutDataList, final int moveOutIndex) {
        final AnimationOnMoveItem.OnMoveItemAnimationCallback onMoveItemAnimationCallback = new AnimationOnMoveItem.OnMoveItemAnimationCallback() {
            @Override
            public void onAnimationBefore(View moveOutItem, View moveInItem) {
                //设置newViewItem的TextView不显示文本数据
                final TextView tvInMoveInItem = ((TextView)moveInItem.findViewById(R.id.tv_content));
                contentVisible = (String) tvInMoveInItem.getText();
                tvInMoveInItem.setText("");
                ((TextView)moveOutItem.findViewById(R.id.tv_content)).setText("");
            }

            @Override
            public void onAnimationEnd(View moveOutItem, View moveInItem) {
                if(moveInItem!=null) {
                    ((TextView)moveInItem.findViewById(R.id.tv_content)).setText(contentVisible);
                }
            }
        };
        //
        if(animationOnMoveItem == null) {
            animationOnMoveItem = new AnimationOnMoveItem();
        }
        animationOnMoveItem.moveItemAnimation(moveInRV, moveOutRV,
                moveInDataList, moveOutDataList,
                moveOutIndex, onMoveItemAnimationCallback);
    }

    @Override
    public void onClick(RecyclerView.Adapter adapter, int index) {
        if(adapter == adapter1) {
            moveItem(rv_2, rv_1, data2, data1, index);
        }
        else if(adapter == adapter2) {
            moveItem(rv_1, rv_2, data1, data2, index);
        }
    }
}
