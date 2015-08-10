package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Category Class의 목록을 가져와서 리스트로 보여줄 Fragment기 때문에 굳이 View를 따로 만들지 않고
 * ListFragment를 상속받아서 사용
 * Default로 ListView를 return 하므로 onCreateView callback method를 구현할 필요가 없다
 * (출처 : http://developer.android.com/intl/ko/guide/components/fragments.html)
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */

public class CategoryFragment extends ListFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // list view를 위해 ArrayAdapter 생성.
        // android에서 제공하는 기본 list layout을 사용하고, inner Category class의 값을 카테고리 메뉴 리스트로 사용한다
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new Category().category));
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity(), l.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    private class Category {
        private String[] category = {
                "연애/사랑",
                "우정/친구",
                "가족",
                "여행"
        };
    }
}
