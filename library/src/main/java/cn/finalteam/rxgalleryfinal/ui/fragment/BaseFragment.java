package cn.finalteam.rxgalleryfinal.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Stack;

import cn.finalteam.rxgalleryfinal.BuildConfig;
import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.utils.Logger;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 上午10:46
 */
public abstract class BaseFragment extends Fragment {

    public static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;
    public static final String EXTRA_CONFIGURATION = EXTRA_PREFIX + ".Configuration";
    private static Stack<BaseFragment> fragmentStack = new Stack<>();
    private final String CLASS_NAME = getClass().getSimpleName();
    protected Bundle mSaveDataBundle;
    protected String BUNDLE_KEY = "KEY_" + CLASS_NAME;
    protected Configuration mConfiguration;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        printFragmentLife("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printFragmentLife("onCreate");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        printFragmentLife("onViewCreated");

        Bundle argsBundle = getArguments();

        if (savedInstanceState != null) {
            mConfiguration = savedInstanceState.getParcelable(EXTRA_CONFIGURATION);
        }
        if (mConfiguration == null && argsBundle != null) {
            mConfiguration = argsBundle.getParcelable(EXTRA_CONFIGURATION);
        }

        if (mConfiguration != null) {
            if (argsBundle == null) {
                argsBundle = savedInstanceState;
            }
            onViewCreatedOk(view, argsBundle);
            setTheme();
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public abstract void onViewCreatedOk(View view, @Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        printFragmentLife("onCreateView");
        return inflater.inflate(getContentView(), container, false);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Logger.i("startActivityForResult");
        Fragment parentFragment = getParentFragment();
        if (null != parentFragment) {
            fragmentStack.push(this);
            parentFragment.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.i("onActivityResult");
        BaseFragment fragment = fragmentStack.isEmpty() ? null : fragmentStack.pop();
        if (null != fragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        printFragmentLife("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        printFragmentLife("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        printFragmentLife("onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        printFragmentLife("onDestroyView");
        saveStateToArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        printFragmentLife("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        printFragmentLife("onDetach");
    }

    public abstract int getContentView();

    public void setTheme() {
    }

    private void printFragmentLife(String method) {
        Logger.i(String.format("Fragment:%s Method:%s", CLASS_NAME, method));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        printFragmentLife("onActivityCreated");
        if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
        }
    }

    protected abstract void onFirstTimeLaunched();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        printFragmentLife("onSaveInstanceState");
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null) {
            mSaveDataBundle = saveState();
        }

        if (mSaveDataBundle != null) {
            Bundle b = getArguments();
            if (b != null) {
                b.putBundle(BUNDLE_KEY, mSaveDataBundle);
            }
        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null) {
            mSaveDataBundle = b.getBundle(BUNDLE_KEY);
            if (mSaveDataBundle != null) {
                restoreState();
                return true;
            }
        }
        return false;
    }

    /**
     * Restore Instance State Here
     */
    private void restoreState() {
        if (mSaveDataBundle != null) {
            mConfiguration = mSaveDataBundle.getParcelable(EXTRA_CONFIGURATION);
            onRestoreState(mSaveDataBundle);
        }
    }

    /**
     * 恢复数据
     */
    protected abstract void onRestoreState(Bundle savedInstanceState);

    /**
     * Save Instance State Here
     */
    private Bundle saveState() {
        Bundle state = new Bundle();
        state.putParcelable(EXTRA_CONFIGURATION, mConfiguration);
        onSaveState(state);
        return state;
    }

    protected abstract void onSaveState(Bundle outState);
}
