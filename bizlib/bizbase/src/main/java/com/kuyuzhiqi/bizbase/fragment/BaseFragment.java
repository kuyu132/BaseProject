package com.kuyuzhiqi.bizbase.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

public class BaseFragment extends Fragment {

    protected FragmentActivity mActivity;

    /** 是否刚从其他的Tab Fragment切换过来,用于数据刷新 */
    protected boolean isSwitchFromOtherTab = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //令嵌套的fragment也能获取到onActivityResult的信息
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 当前Fragment是否嵌套有子Fragment.
     * 嵌套有子Fragment的子类需要重写本方法并返回true,避免友盟页面统计数据有误.
     */
    protected boolean isContainChildFragment() {
        return false;
    }

    /**
     * 获取页面名称
     */
    private String getFragmentPageName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 是否刚从其他的TabFragment切换过来
     */
    public void setSwitchFromOtherTab(boolean switchFromOtherTab) {
        isSwitchFromOtherTab = switchFromOtherTab;
    }

    /**
     * fragment需要处理back键时，可实现此接口
     */
    public interface OnBackPressListener {
        /**
         * 需要在Activity调用
         *
         * @return 是否处理了back键按下
         */
        boolean onBackPressed();
    }

    /**
     * fragment需要外部显式调用刷新数据,可实现此接口
     */
    public interface IRefreshData {
        /**
         * 触发刷新数据
         */
        void refreshData();
    }
}
