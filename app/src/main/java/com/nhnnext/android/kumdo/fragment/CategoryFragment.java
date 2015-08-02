package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/*
 * Category Class의 목록을 가져와서 리스트로 보여줄 Fragment기 때문에 굳이 View를 따로 만들지 않고
 * ListFragment를 상속받아서 사용
 * Default로 ListView를 return 하므로 onCreateView callback method를 구현할 필요가 없다
 */

public class CategoryFragment extends android.support.v4.app.ListFragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    private class Category {
        private String[] category = {
                "연애/사랑",
                "우정/친구",
                "가족",
                "여행"
        };
    }
}
