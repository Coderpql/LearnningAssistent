package com.learning.wow.learningassistence.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.learning.wow.learningassistence.R;
import com.learning.wow.learningassistence.Tools.JishiActivity;
import com.learning.wow.learningassistence.Tools.LaoHuFuActivity;
import com.learning.wow.learningassistence.Tools.MYyActivity;

/**
 * Created by wow on 2017/9/28.
 */

public class ToolFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tool_fragment,container,false);
        ImageButton jiandu = (ImageButton)view.findViewById(R.id.tool_button);
        jiandu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MYyActivity.class);
                startActivity(intent);
            }
        });
        ImageButton jishi = (ImageButton)view.findViewById(R.id.time_button);
        jishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JishiActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
