package com.tech.sonet.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by rahul on 10/18/2022.
 */

public class BackStackManager {

    private HashMap<String, Stack<Fragment>> backStack;
    private static BackStackManager _instance;
    private FragmentManager manager;
    @Nullable
    private String currentTab;

    @Nullable
    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(@Nullable String currentTab) {
        this.currentTab = currentTab;
    }

    private BackStackManager() {
        backStack = new HashMap<>();
    }

    public static BackStackManager getInstance(FragmentActivity context) {
        if (_instance == null) {
            _instance = new BackStackManager();

        }
        _instance.manager = context.getSupportFragmentManager();
        return _instance;
    }

    public HashMap<String, Stack<Fragment>> getBackStack() {
        return backStack;
    }

    public void clear() {
        currentTab = null;
        _instance = null;
    }

    public void pushFragments(int containerId, String tag, Fragment fragment, boolean shouldAnimate) {
        currentTab = tag;
        FragmentTransaction ft = manager.beginTransaction();
        if (backStack.containsKey(tag) && !tag.equalsIgnoreCase("ChatFragment")) {
            Stack<Fragment> s = backStack.get(tag);
            if (s != null) {
                ft.replace(containerId, s.lastElement());
                ft.commit();
                if (fragmentChangeListener != null) {
                    fragmentChangeListener.onFragmentReplace(tag, s.size() > 1);
                }
            }
        } else {
            Stack<Fragment> s = new Stack<>();
            s.push(fragment);
            backStack.put(tag, s);
            ft.replace(containerId, fragment);
            ft.commit();
            if (fragmentChangeListener != null) {
                fragmentChangeListener.onFragmentReplace(tag, false);
            }
        }
    }

    public void pushSubFragments(int containerId, String tag, Fragment fragment, boolean shouldAnimate) {
        currentTab = tag;
        FragmentTransaction ft = manager.beginTransaction();
        if (backStack.containsKey(tag)) {
            Stack<Fragment> s = backStack.get(tag);
            s.push(fragment);
            ft.replace(containerId, s.lastElement());
        } else {
            Stack<Fragment> s = new Stack<>();
            s.push(fragment);
            backStack.put(tag, s);
            ft.replace(containerId, fragment);
        }
        ft.commit();
        if (fragmentChangeListener != null) {
            fragmentChangeListener.onFragmentReplace(tag, true);
        }
    }


    public void pushSubFragmentsWithoutCache(int containerId, String tag, Fragment fragment, boolean shouldAnimate) {
        currentTab = tag;
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(containerId, fragment);
        ft.commit();
    }


    public void setCurrentFragments(int containerId) {
        if (currentTab != null) {
            if (backStack.containsKey(currentTab)) {
                FragmentTransaction ft = manager.beginTransaction();
                Stack<Fragment> s = backStack.get(currentTab);
                ft.replace(containerId, s.lastElement());
                ft.commit();
            }
        }
    }

    public boolean removeFragment(int containerId, boolean animate) {
        if (currentTab == null)
            return true;
        if (backStack.containsKey(currentTab)) {
            Stack<Fragment> fragments = backStack.get(currentTab);
            if (fragments != null && fragments.size() > 1) {
                fragments.pop();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(containerId, fragments.lastElement());
                ft.commit();
                if (fragmentChangeListener != null) {
                    fragmentChangeListener.onFragmentReplace(currentTab, fragments.size() > 1);
                }
                Log.d(currentTab, "sub fragment removed");
                return false;
            } else return true;
        }
        return true;

    }

    @Nullable
    private FragmentChangeListener fragmentChangeListener;

    @Nullable
    public FragmentChangeListener getFragmentChangeListener() {
        return fragmentChangeListener;
    }

    public void setFragmentChangeListener(@Nullable FragmentChangeListener fragmentChangeListener) {
        this.fragmentChangeListener = fragmentChangeListener;
    }

    public interface FragmentChangeListener {
        void onFragmentReplace(@NonNull String tag, boolean isSubFragment);
    }

}
