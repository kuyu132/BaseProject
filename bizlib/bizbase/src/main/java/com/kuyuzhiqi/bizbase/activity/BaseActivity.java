package com.kuyuzhiqi.bizbase.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
    }

    /**
     * 当前页面是否包含fragment页面,由子类实现,满足友盟页面统计需要.
     */
    protected abstract boolean isActivityPageContainFragment();

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 页面统计名称,默认为当前类名
     */
    protected String getActivityPageName() {
        return this.getClass().getSimpleName();
    }
}
