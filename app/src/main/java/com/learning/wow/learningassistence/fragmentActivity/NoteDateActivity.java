package com.learning.wow.learningassistence.fragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.learning.wow.learningassistence.SingleFragmentActivity;
import com.learning.wow.learningassistence.fragment.NoteDateFragment;

/**
 * Created by wow on 2017/10/8.
 */

public class NoteDateActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NoteDateFragment();
    }
}
