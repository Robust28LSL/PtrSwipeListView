package com.it.juyou.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.juyou.AdapterModel;
import com.it.juyou.R;

import java.util.List;

/**
 * Created by lishuliang on 2016/12/7.
 */
public class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.ItemViewHolder> implements View.OnClickListener ,View.OnLongClickListener,View.OnDragListener{
    private Context context;
    private List<AdapterModel> mList;
     private LayoutInflater mInflater;
    public RecyAdapter(Context context, List<AdapterModel> stringList){
        this.context = context;
        this.mList = stringList;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType ==0){
          view  = mInflater.inflate(R.layout.recy_item,parent,false);
        }else {
          view  = mInflater.inflate(R.layout.recy_item2,parent,false);
        }


        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        view.setOnDragListener(this);
        return new ItemViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.mItemView.setTag(mList.get(position));
        if(mList.get(position).type == 0){
            holder.mTextView.setText(mList.get(position).content);
        }else {
            holder.mImageView.setBackgroundResource(R.drawable.ptr_rotate_arrow);
        }


    }



    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }




    class ItemViewHolder extends  RecyclerView.ViewHolder{

        private TextView mTextView;
        private ImageView mImageView;
        private View mItemView;
        public ItemViewHolder(View itemView,int type) {
            super(itemView);
            mItemView = itemView;
            if(type ==0){
                mTextView = (TextView) itemView.findViewById(R.id.tv);
            }else{
                mImageView  = (ImageView) itemView.findViewById(R.id.iv);
            }


        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , AdapterModel data);
    }
    public static interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view , AdapterModel data);
    }
    public static interface OnRecyclerViewItemDragListener {
        void onItemDrag(View view , AdapterModel data,DragEvent event);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener ;
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener ;
    private OnRecyclerViewItemDragListener mOnItemDragkListener ;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener  = listener;
    }
    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener){
        this.mOnItemLongClickListener  = listener;
    }
   public void setOnItemDragListener(OnRecyclerViewItemDragListener listener){
        this.mOnItemDragkListener  = listener;
    }

    @Override
    public boolean onLongClick(View view) {
        if(mOnItemLongClickListener!=null){
            mOnItemLongClickListener.onItemLongClick(view, (AdapterModel) view.getTag());
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(view, (AdapterModel) view.getTag());
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        Log.d("ViewDLActivity","view" + event.getX() +"  " + event.getY());
        if(mOnItemDragkListener!=null){
            mOnItemDragkListener.onItemDrag(view, (AdapterModel) view.getTag(),event);
        }


        return true;
    }


    public void addItem(String data,int position){
        mList.add(position,new AdapterModel(data,position%2));
        notifyItemInserted(position);
    }
    public void addItem(String data){
        int position = mList.size()-1;
        mList.add(position,new AdapterModel(data,position%2));
        notifyItemInserted(position);
    }
   /* public void removeItem(String data){
        int position = mList.indexOf(data);
        mList.remove(position);
        notifyItemRemoved(position);
    }*/
    public void removeItem(int position){
        if(position < mList.size()){
            mList.remove(position);
        notifyItemRemoved(position);
        }
    }

}
