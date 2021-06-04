package com.archy.android.amaptestapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by pandajoy on 17-1-16.
 */

public abstract class BaseFragment extends Fragment {
    private boolean isAttach;
    protected Activity mActivity;
    protected Bundle mSavedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = (Activity) activity;
        super.onAttach(activity);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewID() != 0) {
            return inflater.inflate(getContentViewID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsAndData(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttach = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttach = false;
    }




    protected boolean isAttach() {
        return isAttach;
    }

    /**
     * bind layout resource file
     */
    protected abstract int getContentViewID();

    /**
     * init views and events here
     */
    protected abstract void initViewsAndData(View view);

}
