package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nhnnext.android.kumdo.DetailActivity;
import com.nhnnext.android.kumdo.R;

/**
 * Created by jangyoungbin on 15. 7. 23..
 */
public class MylistFragment extends Fragment {
    // fragment는 현재 Lifecycle 공부가 안된 상태이기 때문에 Lifecycle을 넣지 않았습니다.
    // 이 fragment는 서버에 업로드 된 자신의 Image, Text 또는 로컬 갤러리에 저장된 (Image+Text)
    // 리스트를 보여줄 예정입니다.

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylist_view, container, false);
        GridView gridview = (GridView) view.findViewById(R.id.grid_view);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
