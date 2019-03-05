package com.example.dell.campuslostandfoundplatform;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>{
    private List<ArticleRecycler> list;
    private OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View articleView;
        TextView articleType;

        public ViewHolder(View view){
            super(view);
            articleView=view;
            articleType=(TextView)view.findViewById(R.id.articleType);
        }
    }
    public ArticleRecyclerAdapter(List<ArticleRecycler> list){
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_recycler_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position){
        ArticleRecycler articleRecycler=list.get(position);
        holder.articleType.setText(articleRecycler.getArticleTypeName());
        holder.articleType.setBackgroundResource(articleRecycler.getBackground());
        if( listener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position);
                }
            });
        }
    }
    @Override
    public int getItemCount(){
        return list.size();
    }

    public interface OnItemClickListener{
        void onClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.listener=onItemClickListener;
    }
}
