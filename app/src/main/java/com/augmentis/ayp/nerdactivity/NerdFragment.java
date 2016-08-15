package com.augmentis.ayp.nerdactivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Chayanit on 8/15/2016.
 */
public class NerdFragment extends Fragment {

    public static NerdFragment newInstance() {

        Bundle args = new Bundle();

        NerdFragment fragment = new NerdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView mRecyclerView;
    List<ResolveInfo> mActivities;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        mRecyclerView.setAdapter(new NerdAdapter(mActivities));
        return v;
    }

    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();

                String labelA = a.loadLabel(pm).toString();
                String labelB = b.loadLabel(pm).toString();

                return String.CASE_INSENSITIVE_ORDER.compare(labelA, labelB);
            }
        });

        mActivities = activities;
    }

    class NerdViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;

        public NerdViewHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView;
        }

        protected void bindActivity(ResolveInfo resolveInfo) {
            PackageManager pm = getActivity().getPackageManager();
            CharSequence activitiesName = resolveInfo.loadLabel(pm);

            mNameTextView.setText(activitiesName);
        }
    }

    class NerdAdapter extends RecyclerView.Adapter<NerdViewHolder> {

        List<ResolveInfo> mActivities;

        NerdAdapter(List<ResolveInfo> activities){
            mActivities = activities;
        }

        @Override
        public NerdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, parent, false);

            return new NerdViewHolder(v);
        }

        @Override
        public void onBindViewHolder(NerdViewHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);

            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }
}
