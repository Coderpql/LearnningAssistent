package com.learning.wow.learningassistence.fragmentActivity;

import android.support.v4.app.Fragment;

import com.learning.wow.learningassistence.SingleFragmentActivity;
import com.learning.wow.learningassistence.fragment.ToolFragment;

/**
 * Created by wow on 2017/9/28.
 */

public class ToolActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ToolFragment();
    }

}
