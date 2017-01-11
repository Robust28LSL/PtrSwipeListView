package com.it.juyou.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.it.juyou.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by LSL on 2016/11/25.
 */
public class MyListview extends ListView implements AbsListView.OnScrollListener {

    private static final int DONE = 0;
    private static final int PULL_TO_REFRESH = 1;
    private static final int RELEASE_TO_REFRESH = 2;
    private static final int REFRESHING = 3;
    private static final int SCROLL_TO_LOAD = 4;
    private static final int LOADING = 5;


    private boolean isFirstable ;
    private  boolean isLastable;

    private LinearLayout headerView,footerView;
    private ProgressBar loadProgress,refreshProgress;
    private ImageView arrowIv;
    private TextView pullRefreshTv,scrollLoadTv;
    private int headerViewHeight,footerViewHeight;
    private OnRefreshListener mOnRefreshListener;
    private static final int RATIO = 3;
    private Context mContext;
    private boolean isRefresh;
    private boolean isLoad;

    public MyListview(Context context) {
        super(context);
        init(context);
    }

    public MyListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOverScrollMode(View.OVER_SCROLL_NEVER);//listview中滚动拖动到顶部或者底部时的阴影完美解决。
        setOnScrollListener(this);

        headerView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_header, null, false);
        arrowIv  =  (ImageView)  headerView.findViewById(R.id.ptr_rotate_arrow_iv);
        pullRefreshTv   =  (TextView)  headerView.findViewById(R.id.tv_pull_to_refresh);
        refreshProgress   =  (ProgressBar)  headerView.findViewById(R.id.refresh_progress);

        footerView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listview_footer, null, false);
        loadProgress = (ProgressBar) footerView.findViewById(R.id.load_progress);
        scrollLoadTv = (TextView) footerView.findViewById(R.id.tv_scroll_to_load);


        measureView(headerView);
        measureView(footerView);
        addHeaderView(headerView);
        headerViewHeight = headerView.getMeasuredHeight();
        footerViewHeight = footerView.getMeasuredHeight();

        headerView.setPadding(0, -headerViewHeight, 0, 0);

        isRefresh = true;
        isLoad = true;


    }

    public void setRefreshable(boolean isrefresh) {
        isRefresh = isrefresh;
    }
    public void setLoadable(boolean isload) {
        isLoad = isload;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(firstVisibleItem == 0){
            isFirstable = true;
        }else {
            isFirstable = false;
        }

        if(firstVisibleItem + visibleItemCount >= totalItemCount -1 ){
            isLastable = true;
        }else {
            isLastable = false;
        }
    }


    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public interface OnRefreshListener{
        /**
         * @param state  1 pull refresh  2  scroll load
         */
        void onRefreshOrLoad(int state);
    }
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        mOnRefreshListener = onRefreshListener;

    }

    /**
     *
     * @param type   1 refresh 2 load
     * @param state  0  sucess 1  fail  2 no more or no new data
     */

    public void setOnRefreshComplete(int type ,int state){
       if(type == 1){
          refreshstate = DONE;
          changeHeaderByState(refreshstate);
       }else if(type == 2){
        loadstate = DONE;
        switch (state){
            case 0 :
            this.removeFooterView(footerView);
            isAddFooter = false;
            break;
            case 1 :
                loadProgress.setVisibility(View.GONE);
                scrollLoadTv.setText("加载失败");
                isAddFooter = true;
             break;
            case 2 :
                loadProgress.setVisibility(View.GONE);
                scrollLoadTv.setText("没有更多了");
                isAddFooter = true;
           break;
        }
       }

    }

    private  boolean isAddFooter;
    private  boolean isRecord;
    private float startY;
    private float startX;
    private float offsetY;
    private float offsetX;
    private int refreshstate;
    private int loadstate;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
      switch (ev.getAction()){
          case MotionEvent.ACTION_DOWN:
              startY = ev.getY();
              startX = ev.getX();
              if(isFirstable && !isRecord && isRefresh ){
                isRecord = true;
            }else if(isLastable && !isRecord && isLoad ){
                isRecord = true;
            }
              break;
          case MotionEvent.ACTION_MOVE:
              float curY = ev.getY();
              float curX = ev.getX();
              if(Math.abs(curX - startX) > Math.abs(curY - startY)){
                  return super.onTouchEvent(ev);
              }

              if(isFirstable && !isRecord && isRefresh ){
                  startY = curY;
                  isRecord = true;
              }else if(isLastable && !isRecord && isLoad ){
                  startY = curY;
                  isRecord = true;
              }

              if(isFirstable && refreshstate != REFRESHING && isRecord && isRefresh  ){//下拉刷新
                   offsetY = curY - startY;
                  float currentHeight = (-headerViewHeight+offsetY/3);
                  float currentProgress = 1+currentHeight/headerViewHeight;
                  if(currentProgress >=1){
                      currentProgress = 1;
                  }

                 if(refreshstate == RELEASE_TO_REFRESH && isRecord){
                     setSelection(0);
                     if(-headerViewHeight+offsetY/3 < 0){
                         refreshstate = PULL_TO_REFRESH;
                         changeHeaderByState(refreshstate);
                     }else if(offsetY <= 0){
                         refreshstate = DONE;
                         changeHeaderByState(refreshstate);
                     }
                 }
                 if(refreshstate == PULL_TO_REFRESH && isRecord){
                     setSelection(0);
                     if(-headerViewHeight+offsetY/3 >= 0){
                         refreshstate = RELEASE_TO_REFRESH;
                         changeHeaderByState(refreshstate);
                     }else if(offsetY <= 0){
                         refreshstate = DONE;
                         changeHeaderByState(refreshstate);
                     }
                 }
                  if(refreshstate == DONE && isRecord){
                      if(offsetY > 0){
                          refreshstate = PULL_TO_REFRESH;
                      }
                  }
                  if(refreshstate == PULL_TO_REFRESH){
                      headerView.setPadding(0, (int) (-headerViewHeight + offsetY/RATIO),0,0);
                      pullRefreshTv.setText("pull to refresh");
                  }else  if(refreshstate == RELEASE_TO_REFRESH){
                      headerView.setPadding(0, (int) (-headerViewHeight + offsetY/RATIO),0,0);
                      pullRefreshTv.setText("release to refresh");
                  }
              }else if(isLastable && loadstate !=LOADING && isRecord && isLoad ){//滚动加载
                  loadstate = SCROLL_TO_LOAD;
                  if(!isAddFooter){
                      addFooterView(footerView);
                      isAddFooter = true;
                  }else {
                      loadProgress.setVisibility(View.VISIBLE);
                      scrollLoadTv.setText("loading...");
                  }

              }


              break;
          case MotionEvent.ACTION_UP:

              if(refreshstate == REFRESHING){
                  this.smoothScrollBy((int)(-headerViewHeight+offsetY/RATIO), 500);
                  changeHeaderByState(refreshstate);
              }else if(refreshstate == PULL_TO_REFRESH){
                  this.smoothScrollBy((int) (-headerViewHeight + offsetY / RATIO + headerViewHeight), 500);
              }else if(refreshstate == RELEASE_TO_REFRESH){
                  this.smoothScrollBy((int)(-headerViewHeight+offsetY/RATIO), 500);
                  if(null != mOnRefreshListener){
                      mOnRefreshListener.onRefreshOrLoad(1);
                      refreshstate = REFRESHING;
                  }
                  changeHeaderByState(refreshstate);
              }





              if(loadstate == SCROLL_TO_LOAD){
                  if(null != mOnRefreshListener){
                      mOnRefreshListener.onRefreshOrLoad(2);
                      loadstate = LOADING;
                  }
              }

              isRecord = false;
              break;
      }
        return super.onTouchEvent(ev);
    }

    private void changeHeaderByState(int refreshstate) {
        switch (refreshstate){
            case DONE :
                headerView.setPadding(0, -headerViewHeight, 0, 0);
                refreshProgress.setVisibility(View.GONE);
                arrowIv.setVisibility(View.VISIBLE);
                break;
            case PULL_TO_REFRESH:
                pullRefreshTv.setText("pull to refresh");
                refreshProgress.setVisibility(View.GONE);
                arrowIv.setVisibility(View.VISIBLE);
                //ViewHelper.setRotationY(arrowIv,360);
               /* AnimationSet animationSet = new AnimationSet(true);
                RotateAnimation rotateAnimation = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setDuration(500);
                rotateAnimation.setFillAfter(true);
                animationSet.addAnimation(rotateAnimation);
                arrowIv.startAnimation(animationSet);*/
                arrowIv.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.pull_anim));
                break;
            case RELEASE_TO_REFRESH :
                pullRefreshTv.setText("release to refresh");
                refreshProgress.setVisibility(View.GONE);
                arrowIv.setVisibility(View.VISIBLE);
               // ViewHelper.setRotationY(arrowIv,360);
               /* AnimationSet animationSetR = new AnimationSet(true);
                RotateAnimation rotateAnimationR = new RotateAnimation(180,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimationR.setDuration(500);
                rotateAnimationR.setFillAfter(true);
                animationSetR.addAnimation(rotateAnimationR);*/
                arrowIv.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.release_anim));
                break;
            case REFRESHING :
                arrowIv.clearAnimation();
                arrowIv.setVisibility(View.GONE);
                refreshProgress.setVisibility(View.VISIBLE);
                pullRefreshTv.setText("REFRESHING...");
                break;


        }
    }
}
