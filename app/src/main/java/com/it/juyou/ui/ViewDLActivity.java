package com.it.juyou.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.it.juyou.AdapterModel;
import com.it.juyou.MainActivity;
import com.it.juyou.R;
import com.it.juyou.adapter.RecyAdapter;
import com.it.juyou.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ViewDLActivity extends Activity {

    private Unbinder unbinder;
    private RecyAdapter mRecyAdapter;
    @BindView(R.id.vd_recyclerview)    RecyclerView mRecyclerView;
    @BindView(R.id.content_view_dl)    LinearLayout mViewDL;
    List<AdapterModel> mDatas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view_dl);
        unbinder = ButterKnife.bind(this);

        for (int i=0;i<30;i++){
            mDatas.add( new AdapterModel(i +"",i%2));
        }
        GridLayoutManager gridLayoutManager  =  new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyAdapter = new RecyAdapter(this,mDatas);
        mRecyAdapter.setOnItemClickListener(new RecyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, AdapterModel data) {
                Toast.makeText(ViewDLActivity.this,data.content,Toast.LENGTH_SHORT).show();
            }
        });
        mRecyAdapter.setOnItemDragListener(new RecyAdapter.OnRecyclerViewItemDragListener() {
            @Override
            public void onItemDrag(View view, AdapterModel data, DragEvent event) {
                Log.d("ViewDLActivity","ViewDLActivity:X" + event.getX() + " :Y:"+ event.getY() + "  view " +  view.getMeasuredWidth() +" Height " + view.getMeasuredHeight());
                mViewDL.layout((int)event.getX(),(int) event.getY(),(int)event.getX() +view.getMeasuredWidth(),(int) event.getY() + view.getMeasuredHeight());
            }
        });
        mRecyclerView.setAdapter(mRecyAdapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }



}
