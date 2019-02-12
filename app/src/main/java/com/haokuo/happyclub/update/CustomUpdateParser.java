package com.haokuo.happyclub.update;

import com.alibaba.fastjson.JSON;
import com.haokuo.happyclub.bean.UpdateBean;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.proxy.IUpdateParser;

/**
 * Created by zjf on 2018/12/27.
 */
public class CustomUpdateParser implements IUpdateParser {
    @Override
    public UpdateEntity parseJson(String json) throws Exception {
        UpdateBean result = JSON.parseObject(json, UpdateBean.class);
        if (result != null) {
            UpdateBean.VersionBean version = result.getVersion();
            return new UpdateEntity()
                    .setHasUpdate(result.getUpdateFlag())
                    .setForce(version.getCompulsive())
                    .setIsIgnorable(version.getIgnorable())
                    .setVersionCode(version.getVersionCode())
                    .setVersionName(version.getVersionName())
                    .setUpdateContent(version.getContent())
                    .setDownloadUrl(version.getUrl())
                    .setSize(version.getSize());
        }
        return null;

    }
}
