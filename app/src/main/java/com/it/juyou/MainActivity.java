package com.it.juyou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.juyou.adapter.MyAdapter;
import com.it.juyou.adapter.RecyAdapter;
import com.it.juyou.ui.ViewDLActivity;
import com.it.juyou.view.DividerItemDecoration;
import com.it.juyou.view.MyListview;
import com.it.juyou.widget.DragLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainActivity extends Activity implements MyListview.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.listview)  MyListview listview;
    @BindView(R.id.add_bt)   Button add;
    @BindView(R.id.vh_bt)   Button vh_bt;
    @BindView(R.id.delete_bt)   Button delete;
    @BindView(R.id.dl)     DragLayout dl;
    TextView textView;
    LinearLayout linearLayout;

    MyAdapter myAdapter;
    RecyAdapter mRecyAdapter;
    List<AdapterModel> mDatas = new ArrayList<>();
    List<String> mLv = new ArrayList<>();
    private final static int REFRESH_COMPLETE = 0;
    private final static int LOAD_COMPLETE = 1;
    @BindView(R.id.recyclerview)    RecyclerView mRecyclerView;

    Handler mInterHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_COMPLETE :
                    listview.setOnRefreshComplete(1,0);
                    myAdapter.notifyDataSetChanged();
                    listview.setSelection(0);
                break;
                case LOAD_COMPLETE :
                 if(msg.arg1 == 1){//加载失败
                     listview.setOnRefreshComplete(2,1);

                 }else if(msg.arg1 == 2){//没有更多了
                     listview.setOnRefreshComplete(2,2);
                 }else {//加载成功
                     listview.setOnRefreshComplete(2,0);
                     myAdapter.notifyDataSetChanged();
                 }

                    break;

            }
            super.handleMessage(msg);

        }
    };


    private Unbinder unbinder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        // 2          b1851c36cc63e102d022017ebe08b0f5
        // 3          a363d8042659a026de85d2e6da7965dd
       // PMan.get(this,"a363d8042659a026de85d2e6da7965dd", "official",false);
		//TMan.getInstance(this, "a363d8042659a026de85d2e6da7965dd", "official",1);
        for (int i=0;i<100;i++){
            mDatas.add( new AdapterModel(i + "：DATA",i%2));
        }
        for (int i=0;i<100;i++){
            mLv.add( i + "：DATA");
        }

        myAdapter = new MyAdapter(mLv,this);
        listview.setAdapter(myAdapter );
        listview.setOnRefreshListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager gridLayoutManager  =  new GridLayoutManager(this,3);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 设置item动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
       // mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyAdapter = new RecyAdapter(this,mDatas);
        mRecyAdapter.setOnItemClickListener(new RecyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, AdapterModel data) {
                Toast.makeText(MainActivity.this,data.content,Toast.LENGTH_SHORT).show();
            }
        });
        mRecyAdapter.setOnItemDragListener(new RecyAdapter.OnRecyclerViewItemDragListener() {
            @Override
            public void onItemDrag(View view, AdapterModel data, DragEvent event) {
                Toast.makeText(MainActivity.this,data.content + "脱宅",Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mRecyAdapter);
        initEvent();

	}

    private void initEvent() {
        add.setOnClickListener(this);
        vh_bt.setOnClickListener(this);
        delete.setOnClickListener(this);
        dl.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


   int number ;
    @Override
    public void onRefreshOrLoad(final int state) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if(state == 1){//下拉刷新
                        mLv.add(0, "new data REFRESH");
                        mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
                    }else if(state == 2){//滚动加载
                        number++;

                        if(number%5 == 0){
                           Message message = new Message();
                           message.what = LOAD_COMPLETE;
                           message.arg1 = 1;
                           mInterHandler.sendMessage(message);

                        }else {
                            mLv.add( "new data LOAD");
                            mInterHandler.sendEmptyMessage(LOAD_COMPLETE);
                        }

                    }


                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_bt:
                mRecyAdapter.addItem("add Data",10);
                break;
            case R.id.delete_bt:
                mRecyAdapter.removeItem(20);
                break;
            case R.id.vh_bt:
                Intent intent = new Intent(this, ViewDLActivity.class);
                startActivity(intent);
                break;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
