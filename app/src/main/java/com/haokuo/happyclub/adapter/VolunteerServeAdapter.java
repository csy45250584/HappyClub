package com.haokuo.happyclub.adapter;

import com.amap.api.services.core.LatLonPoint;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.RecourseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class VolunteerServeAdapter extends BaseQuickAdapter<RecourseBean, BaseViewHolder> {

    public VolunteerServeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RecourseBean item) {
        helper.setText(R.id.tv_serve_name, item.getServeName());
        helper.setText(R.id.tv_serve_request, item.getClaim());
        helper.setText(R.id.tv_serve_address, item.getAddress());
        //TODO 设置距离
        helper.setVisible(R.id.tv_serve_distance, item.getDistance() != 0);
        float distance = item.getDistance();
        String distanceString;
        if (distance >= 1000) {
            float temp = distance / 1000;
            distanceString = String.format("约%.1fkm", temp);
        } else {
            distanceString = String.format("约%.0fm", distance);
        }
        helper.setText(R.id.tv_serve_distance, distanceString);
        helper.setText(R.id.tv_score, String.valueOf(item.getIntegral()));
        helper.addOnClickListener(R.id.iv_accept_serve);
    }

    public List<LatLonPoint> getLatLonPointList() {
        ArrayList<LatLonPoint> latLonPoints = new ArrayList<>();
        for (RecourseBean bean : mData) {
            latLonPoints.add(new LatLonPoint(Double.valueOf(bean.getLatitude()), Double.valueOf(bean.getLongitude())));
        }
        return latLonPoints;
    }
}
