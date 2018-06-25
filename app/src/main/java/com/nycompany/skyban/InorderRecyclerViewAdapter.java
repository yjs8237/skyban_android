package com.nycompany.skyban;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InorderRecyclerViewAdapter extends RecyclerView.Adapter<InorderRecyclerViewAdapter.ViewHolder> {
    private RecyclerViewClickListener mListener;
    private ArrayList<InoderDTO> inOrders;

    public InorderRecyclerViewAdapter(ArrayList<InoderDTO> inOrders, RecyclerViewClickListener listener) {
        this.inOrders = inOrders;
        this.mListener = listener;
    }

    @Override
    public InorderRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup v, int i) {
        //xml 가져옴
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.inorder_recyclerview_item, v, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(InorderRecyclerViewAdapter.ViewHolder vh, int i) {
        //xml 데아터 바인딩
        vh.tv_name.setText(inOrders.get(i).getName());
        vh.tv_version.setText(inOrders.get(i).getApi());
        vh.tv_api_level.setText(inOrders.get(i).getVer());
    }

    @Override
    public int getItemCount() {
        //아이템을 측정하는 카운터
        return inOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_name,tv_version,tv_api_level;
        private RecyclerViewClickListener mListener;
        public ViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);

            tv_name = (TextView)v.findViewById(R.id.textView13);
            tv_version = (TextView)v.findViewById(R.id.textView15);
            tv_api_level = (TextView)v.findViewById(R.id.textView16);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }
}
