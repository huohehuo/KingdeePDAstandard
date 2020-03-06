package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import com.fangzuo.assist.Beans.DownTipBean;
import com.fangzuo.assist.Dao.NoticBean;
import com.fangzuo.assist.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;

public class DownTipRyAdapter extends RecyclerArrayAdapter<DownTipBean> {
    Context context;
    private ArrayList<String> strings;

    public DownTipRyAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarkHolder(parent);
    }
    class MarkHolder extends BaseViewHolder<DownTipBean> {

        private TextView item1;
        private TextView item2;
        private TextView item3;
        private CheckBox cbCheck;
        private CardView llCard;
        public MarkHolder(ViewGroup parent) {
            super(parent, R.layout.items_down_tip);
            item1= $(R.id.tv_item1);
            item2= $(R.id.tv_item2);
            item3= $(R.id.tv_item3);
            cbCheck= $(R.id.cb_ischeck);
            llCard= $(R.id.ll_card);
//            item4= $(R.id.tv_item4);
//            checkBox = $(R.id.view_cb);
        }

        @Override
        public void setData(DownTipBean data) {
            super.setData(data);
            item1.setText(data.FName);
            item2.setText(data.FTip);
            if (null != data.FTip && !"".equals(data.FTip)){
                llCard.setCardBackgroundColor(context.getResources().getColor(R.color.cpb_blue));
            }else{
                llCard.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            }
            if (null!=strings && strings.size()>0){
                for (int j = 0; j < strings.size(); j++) {
                    if (strings.get(j).equals(data.tag) ){
                        cbCheck.setChecked(true);
                        break;
                    }
                    else{
                        cbCheck.setChecked(false);
                    }
                }
            }
        }
    }
    //标识已有单据
    public void setDownList(ArrayList<String> list){
        strings = list;
        notifyDataSetChanged();
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
