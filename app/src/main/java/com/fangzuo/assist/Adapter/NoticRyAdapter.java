package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangzuo.assist.Dao.NoticBean;
import com.fangzuo.assist.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class NoticRyAdapter extends RecyclerArrayAdapter<NoticBean> {
    Context context;

    public NoticRyAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarkHolder(parent);
    }
    class MarkHolder extends BaseViewHolder<NoticBean> {

        private TextView item1;
        private TextView item2;
        private TextView item3;
//        private TextView item4;
        public MarkHolder(ViewGroup parent) {
            super(parent, R.layout.items_notic);
            item1= $(R.id.tv_item1);
            item2= $(R.id.tv_item2);
            item3= $(R.id.tv_item3);
//            item4= $(R.id.tv_item4);
//            checkBox = $(R.id.view_cb);
        }

        @Override
        public void setData(NoticBean data) {
            super.setData(data);
            item1.setText("单号："+data.FBillNo);
            item2.setText("数据单据："+data.FActivityType);
            item3.setText("总行数"+data.FNumAll);
//            item4.setText(data.FBillNo);
        }
    }


//    //纯文字布局
//    class MainHolderForTxt extends BaseViewHolder<PlanBean> {
//
//        private TextView time;
//        private TextView eesay;
//        private ImageView favour;
//        private TextView num;
//        public MainHolderForTxt(ViewGroup parent) {
//            super(parent, R.layout.item_plan_for_txt);
//            time = $(R.id.tv_time);
//            eesay = $(R.id.tv_main_essay);
//            num = $(R.id.tv_favour);
//            favour = $(R.id.iv_favour);
//        }
//
//        @Override
//        public void setData(PlanBean data) {
//            super.setData(data);
//            eesay.setText(data.getEssay());
//            time.setText(data.getCreatedAt());
////            num.setText(data.getFavour().get__op());
//
//            favour.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(App.getContext(), "喜欢+1", Toast.LENGTH_SHORT).show();
//                }
//            });
//
////            Glide.with(getContext())
////                    .load(data.getPic())
////                    .placeholder(R.mipmap.ic_launcher)
////                    .centerCrop()
////                    .into(imageView);
//
//        }
//    }
}
