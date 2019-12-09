package com.liufeismart.animationdemo.animation;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liufeismart.animationdemo.util.UIUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AnimationOnMoveItem {

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutLinster;
    private WeakReference<ImageView> imageViewRef;
    private WeakReference<ViewGroup> rootViewRef;

    private int ANIMATION_FOR_MOVE_ITEM_DURATION = 2000;

    boolean isItemMoving;

    int locationParent[] = new int[2];

    /**
     *
     * @param moveInRV 移入Item的RecyclerView
     * @param moveOutRV 移出Item的RecyclerView
     * @param moveInDataList 移入Item的RecyclerView的data
     * @param moveOutDataList 移出Item的RecyclerView的data
     * @param moveOutIndex 移出Item的Index
     * @param onMoveItemAnimationCallback
     */
    public void moveItemAnimation(final RecyclerView moveInRV, final RecyclerView moveOutRV,
                                  final List moveInDataList, final List moveOutDataList, final int moveOutIndex, final OnMoveItemAnimationCallback onMoveItemAnimationCallback) {
        final int moveOutDataListSize = moveOutDataList.size();
        moveInDataList.add(moveOutDataList.get(moveOutIndex));
        final int moveInDataListSize = moveInDataList.size();
        moveInRV.getAdapter().notifyItemRangeInserted(moveInDataList.size()-1, moveInDataList.size());
        //两种不同的方式:目的是调用Adapter.notifyXXX,RecyclerView重新布局之后,能够获取到相应的控件的位置信息
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//通过监听方式
            if(mOnGlobalLayoutLinster!= null) {
                moveInRV.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutLinster);
                moveOutRV.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutLinster);

            }
            mOnGlobalLayoutLinster = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //onGlobalLayout在moveOutRV.notifyXXX之前，
                    if(moveOutDataListSize == moveOutDataList.size()
                            && moveInDataListSize == moveInDataList.size()
                            && !isItemMoving) {
                        isItemMoving = true;
                        startAnimationForMoveItem(moveInRV, moveOutRV,
                                moveInDataList, moveOutDataList,
                                moveOutIndex, onMoveItemAnimationCallback);

                    }

                }
            };
            moveInRV.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutLinster);
        }
        else {//通过延时的方式
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnimationForMoveItem(moveInRV, moveOutRV,
                            moveInDataList, moveOutDataList,
                            moveOutIndex, onMoveItemAnimationCallback);
                }
            }, 100);
        }

    }


    private void startAnimationForMoveItem(final RecyclerView moveInRV, final RecyclerView moveOutRV,
                                           final List moveInDataList, final List moveOutDataList,
                                           final int moveOutIndex, final OnMoveItemAnimationCallback callback) {

        //newViewItem, moveOutViewItem;
        final View newViewItem, moveOutViewItem;
        final String contentVisible;
        int newItemLocation[] = new int[2];
        int moveOutItemLocation[] = new int[2];
        //获取两个ItemView的信息：位置，Bitmap
        newViewItem = moveInRV.getChildAt(moveInDataList.size()-1);
        moveOutViewItem = moveOutRV.getChildAt(moveOutIndex);
        newViewItem.getLocationInWindow(newItemLocation);
        moveOutViewItem.getLocationInWindow(moveOutItemLocation);
        final Bitmap bitmap = UIUtil.loadBitmapFromView(moveOutViewItem);
        ImageView imageView = null;
        //设置imageView的起始位置moveOutViewItem与一致
        if(imageViewRef == null || imageViewRef.get() == null) {
//            ViewGroup parentView = (ViewGroup)moveOutRV.getParent();
//            parentView.getLocationInWindow(locationParent);
            ViewGroup rootView =  ((ViewGroup)moveOutRV.getRootView());
            rootViewRef = new WeakReference<>(rootView);
            imageView = new ImageView(moveOutRV.getContext());
            imageViewRef = new WeakReference<>(imageView);
//            imageView.setAlpha(0.3f);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(moveOutViewItem.getWidth(), moveOutViewItem.getHeight());
            params.topMargin = moveOutItemLocation[1];
            params.leftMargin = moveOutItemLocation[0];
            rootView.addView(imageView, params);
        }
        else {
            imageView = imageViewRef.get();
//            ViewGroup parentView = (ViewGroup)moveOutRV.getParent();
//            parentView.getLocationInWindow(locationParent);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(moveOutViewItem.getWidth(), moveOutViewItem.getHeight());
            params.topMargin = moveOutItemLocation[1];
            params.leftMargin = moveOutItemLocation[0];
            imageView.setLayoutParams(params);
        }
        imageView.setVisibility(View.VISIBLE);

        //动画开始前
        callback.onAnimationBefore(moveOutViewItem, newViewItem);

        imageView.setImageBitmap(bitmap);
        //设置动画并启动
        TranslateAnimation translateAnimation = new TranslateAnimation(0 , newItemLocation[0]-moveOutItemLocation[0],0, newItemLocation[1]-moveOutItemLocation[1]);
        translateAnimation.setDuration(ANIMATION_FOR_MOVE_ITEM_DURATION);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

//                imageView.setVisibility(View.GONE);
                if(moveOutIndex< moveOutDataList.size()) {
                    moveOutDataList.remove(moveOutIndex);
                    moveOutRV.getAdapter().notifyDataSetChanged();//notifyItemRemoved(moveOutIndex);
                }
                isItemMoving = false;

                callback.onAnimationEnd(moveOutViewItem, newViewItem);
//                imageViewRef.get().setVisibility(View.GONE);
                imageViewRef.get().setImageBitmap(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        translateAnimation.setFillAfter(true);
        imageView.startAnimation(translateAnimation);
    }

    public interface OnMoveItemAnimationCallback {
        void onAnimationBefore(View moveOutItem, View moveInItem);
        void onAnimationEnd(View moveOutItem, View moveInItem);
    }


}
