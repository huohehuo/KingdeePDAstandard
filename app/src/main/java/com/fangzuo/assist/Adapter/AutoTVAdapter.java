package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzuo.assist.Dao.Product;
import com.fangzuo.assist.R;

import java.util.List;

/**
 * Created by NB on 2017/8/1.
 */

public class AutoTVAdapter extends ArrayAdapter<Product> implements View.OnClickListener{
    Context context;
    private int resource;
    private List<Product> objects;
    private ArrayFilter mFilter;
    private InnerClickListener listener;

    public AutoTVAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;

    }

    @Override
    public int getCount() {
        return objects.size();
    }


    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Product item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
            viewHolder = new ViewHolder();
            viewHolder.fid = convertView.findViewById(R.id.fID);
            viewHolder.fname = convertView.findViewById(R.id.fname);
            viewHolder.ll = convertView.findViewById(R.id.ll);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fid.setText(objects.get(position).FNumber);
        viewHolder.fname.setText(objects.get(position).FName);
        viewHolder.ll.setOnClickListener(this);
        viewHolder.ll.setTag(position);
        return convertView;
    }


    @Override
    public CharSequence[] getAutofillOptions() {
        return super.getAutofillOptions();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    @Override
    public void onClick(View view) {
            listener.tvClick(view);
    }
    interface InnerClickListener{
        void tvClick(View v);
    }

    public void setTvClickListener(InnerClickListener listener){
            this.listener = listener;
    }

    class ViewHolder{
        TextView fid;
        LinearLayout ll;
        TextView fname;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            results.values=objects;
                results.count=objects.size();
//            if (prefix == null || prefix.length() == 0) {
//                synchronized (AutoTVAdapter.this) {
//                    Log.i("tag", "mOriginalValues.size="+objects.size());
//                    ArrayList<Product> list = new ArrayList<>(objects);
//                    results.values = list;
//                    results.count = list.size();
//                    Log.e("筛选1","null");
//                    return results;
//                }
//            } else {
//                String prefixString = prefix.toString();
//                Log.e("筛选2",prefixString);
//                final int count = objects.size();
//                final ArrayList<Product> newValues = new ArrayList<>(count);
//                for (int i = 0; i < count; i++) {
//                    final Product value = objects.get(i);
//                    Log.e("筛选3",value.FName);
//                    if(value.FName.contains(prefixString)||value.FNumber.contains(prefixString)){
//                        newValues.add(value);
//                        Log.e("筛选结果name",value.toString());
//                        Log.e("筛选结果id",value.toString());
//                    }
//                }
//
//                results.values = newValues;
//                results.count = newValues.size();
//            }
//
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            List<Product> mObjects = (List<Product>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
