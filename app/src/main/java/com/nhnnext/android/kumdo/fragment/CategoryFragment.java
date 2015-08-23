package com.nhnnext.android.kumdo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhnnext.android.kumdo.R;

/**
 * Category Class의 목록을 가져와서 리스트로 보여줄 Fragment기 때문에 굳이 View를 따로 만들지 않고
 * ListFragment를 상속받아서 사용
 * Default로 ListView를 return 하므로 onCreateView callback method를 구현할 필요가 없다
 * (출처 : http://developer.android.com/intl/ko/guide/components/fragments.html)
 * Tab Layout을 위해 ViewPager를 사용. 따라서 v4.Fragment를 상속받는다(`15.08.10 by jyb)
 */

public class CategoryFragment extends Fragment {
    private static final String TAG = "CategoryFragment";
    private Category category;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        category = new Category();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_view, container, false);
        final ListView mListView = (ListView) view.findViewById(R.id.category_container);

        mListView.setAdapter(new ImageAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
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

    private static class Category {
        private int[] name = {
                R.string.category_romance,
                R.string.category_friend,
                R.string.category_family,
                R.string.category_adventure,

        };
        private int[] image = {
                R.drawable.romance,
                R.drawable.friend,
                R.drawable.family,
                R.drawable.adventure
        };
    }

    private class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return category.name.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                v = inflater.inflate(R.layout.category_row, null);
            }

            ViewHolder holder = (ViewHolder) v.getTag(R.id.id_holder);

            if (holder == null) {
                holder = new ViewHolder(v);
                v.setTag(R.id.id_holder, holder);
            }

            holder.image.setImageResource(category.image[position]);
            holder.name.setText(category.name[position]);
            Animation textMoveAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.text_move);
            holder.name.startAnimation(textMoveAnimation);

            return v;
        }

        private class ViewHolder {
            ImageView image;
            TextView name;

            public ViewHolder(View v) {
                image = (ImageView) v.findViewById(R.id.category_image);
                name = (TextView) v.findViewById(R.id.category_name);
                v.setTag(this);
            }
        }
    }
}
