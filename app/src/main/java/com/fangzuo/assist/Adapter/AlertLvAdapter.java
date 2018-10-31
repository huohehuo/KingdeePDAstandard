package com.fangzuo.assist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzuo.assist.Dao.PDSub;
import com.fangzuo.assist.Dao.Storage;
import com.fangzuo.assist.Dao.WaveHouse;
import com.fangzuo.assist.R;
import com.fangzuo.assist.Utils.GreenDaoManager;
import com.fangzuo.greendao.gen.DaoSession;
import com.fangzuo.greendao.gen.StorageDao;
import com.fangzuo.greendao.gen.WaveHouseDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NB on 2017/8/21.
 */

public class AlertLvAdapter extends BaseAdapter {
    private final DaoSession daosession;
    Context context;
    List<PDSub> items;
    private ViewHolder viewHolder;

    public AlertLvAdapter(Context context, List<PDSub> items) {
        this.context = context;
        this.items = items;
        daosession = GreenDaoManager.getmInstance(context).getDaoSession();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.items_pd_alert_list, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        WaveHouseDao waveHouseDao = daosession.getWaveHouseDao();
        List<WaveHouse> waveHouses = waveHouseDao.queryBuilder().where(WaveHouseDao.Properties.FSPID.eq(items.get(i).FStockPlaceID)).build().list();
        if(waveHouses.size()>0){
            viewHolder.tvWavehouse.setText("仓位"+waveHouses.get(0).FName);
        }else{
            viewHolder.tvWavehouse.setText("0");
        }

        StorageDao storageDao = daosession.getStorageDao();
        List<Storage> storages = storageDao.queryBuilder().where(StorageDao.Properties.FItemID.eq(items.get(i).FStockID)).build().list();
        if(storages.size()>0){
            viewHolder.tvStorage.setText("仓库"+storages.get(0).FName);
        }else{
            viewHolder.tvStorage.setText("0");
        }

        viewHolder.tvBatchNo.setText("批次:"+items.get(i).FBatchNo);
        viewHolder.tvFid.setText("id:"+items.get(i).FID);
        viewHolder.tvFname.setText(items.get(i).FName);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_fid)
        TextView tvFid;
        @BindView(R.id.tv_fname)
        TextView tvFname;
        @BindView(R.id.tv_storage)
        TextView tvStorage;
        @BindView(R.id.tv_wavehouse)
        TextView tvWavehouse;
        @BindView(R.id.tv_batchNo)
        TextView tvBatchNo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
