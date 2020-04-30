package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzuo.assist.Beans.SettingList;
import com.fangzuo.assist.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class Page1Adapter extends RecyclerArrayAdapter<SettingList> {
    Context context;
    public Page1Adapter(Context context) {
        super(context);
        this.context = context;
    }

    //
//    public MarkAdapter(Context context, MarkBean[] objects) {
//        super(context, objects);
//    }
//
//    public MarkAdapter(Context context, List<MarkBean> objects) {
//        super(context, objects);
//    }
//    @Override
//    public int getViewType(int position) {
//        if (null==(getAllData().get(position).getFYmLenght())){//当原木长为空时，为一期项目布局
//            return 1;
//        }else{
//            return 2;
//        }
//    }

//    //求宽度的平均值
//    public String getWideAve(){
//        if (getAllData().size()>0){
//            Double res = 0.0;
//            for (int i = 0; i < getAllData().size(); i++) {
//                res=res+ MathUtil.toD(getAllData().get(i).FStandby1);
//            }
//            Lg.e("总宽度"+res);
//            Lg.e("总宽度size"+getAllData().size());
//            String num = (res/getAllData().size())+"";
//            Lg.e("平均宽度"+num);
//            return num.substring(0,num.lastIndexOf("."));
//        }else{
//            return "0";
//        }
//    }
//    //拼接测量宽的数值列表
//    public String getWideString(){
//        StringBuffer buffer = new StringBuffer();
//        for (int i = 0; i < getAllData().size(); i++) {
//            buffer.append(getAllData().get(i).FStandby1).append("/");
//        }
//        return buffer.toString().substring(0,buffer.toString().length()-1);
//    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new MarkHolder(parent);
//        if (viewType==2){
//            Lg.e("二期布局");
//            return new MarkHolder4P2(parent);
//        }else{
//            Lg.e("一期布局");
//            return new MarkHolder(parent);
//        }

    }


    class MarkHolder extends BaseViewHolder<SettingList> {

        private ImageView tv_model1;
        private TextView tv_model2;
        private LinearLayout ll_bg;

        public MarkHolder(ViewGroup parent) {
            super(parent, R.layout.item_p1one);
            tv_model1 =   $(R.id.iv);
            tv_model2 =   $(R.id.tv);
            ll_bg =   $(R.id.ll_bg);
        }

        @Override
        public void setData(SettingList data) {
            super.setData(data);
            tv_model1.setImageResource(data.ImageResourse);
            tv_model2.setText(data.tv);
//            if (!"1".equals(data.HasPermit)){
//                ll_bg.setBackgroundColor(context.getResources().getColor(R.color.DJpermit));
//            }else{
//                ll_bg.setBackgroundColor(context.getResources().getColor(R.color.white));
//            }
        }
    }

//    class MarkHolder4P2 extends BaseViewHolder<PrintHistory> {
//
//        private TextView huoquan;
//        private TextView batch;
//        private TextView name;
//        private TextView model;
//        private TextView num;
//        private TextView num2;
//        private TextView note;
//        private TextView wavehouse;
//        private TextView date;
//
//        public MarkHolder4P2(ViewGroup parent) {
//            super(parent, R.layout.item_print_history_p2);
//            huoquan =   $(R.id.tv_huoquan);
//            batch =     $(R.id.tv_batch);
//            name =      $(R.id.tv_name);
//            model =     $(R.id.tv_model);
//            num =       $(R.id.tv_num);
//            num2 =      $(R.id.tv_num2);
//            note =      $(R.id.tv_note);
//            wavehouse = $(R.id.tv_wavehouse);
//            date =      $(R.id.tv_date);
//
//        }
//
//        @Override
//        public void setData(PrintHistory data) {
//            super.setData(data);
//            huoquan.setText(LocDataUtil.getRemark(data.getFHuoquan(),"number").FNote);
//            batch.setText(data.getFBatch());
//            name.setText(data.getFName());
//            model.setText(data.getFModel());
////            num.setText(data.getFNum()+"  "+data.getFUnit());//库存数量
//            num.setText(data.getFYmLenght()+"  "+data.getFUnit());//库存数量
//            if (null==data.getFNum2()){
//                num2.setVisibility(View.INVISIBLE);
//            }else{
//                num2.setVisibility(View.VISIBLE);
////                num2.setText(data.getFNum2()+"  "+data.getFUnitAux());//基本数量
//                num2.setText(data.getFYmDiameter()+"  厘米(cm)");//基本数量
//            }
//            note.setText(data.getFVolume());
//            wavehouse.setText(data.getFWaveHouse());
//            date.setText(data.getFDate());
////            name.setTextColor(App.getInstance().getColor(R.color.black));
////        //3、为集合添加值
////            isClicks = new ArrayList<>();
////            for(int i = 0;i<PrintHistory.size();i++){
////                isClicks.add(false);
////            }
////            name.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    name.setTextColor(App.getInstance().getColor(R.color.cpb_blue));
////                }
////            });
//
////            num.setText(data.getFavour().get__op());
//
////            favour.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Toast.makeText(App.getContext(), "喜欢+1", Toast.LENGTH_SHORT).show();
////                }
////            });
//
////            Glide.with(getContext())
////                    .load(data.getBg_pic())
////                    .placeholder(R.drawable.dog)
////                    .centerCrop()
////                    .into(img_bg);
//
//        }
//    }


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
