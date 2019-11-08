package com.example.admin.appbarbottom;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;


public class DetailProfileFragment extends Fragment {

    MainActivity mainActivity;
    private static final String EXTRA_PROFILE_ITEM = "profile_item";
    public static final String EXTRA_TRANSITION_NAME = "transition_name";
    LinearLayout linearLayout;

    public DetailProfileFragment() {
        // Required empty public constructor
    }


//    public static DetailProfileFragment newInstance(Profile profile, String transitionName) {
//        DetailProfileFragment detailProfileFragment = new DetailProfileFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(EXTRA_PROFILE_ITEM, profile);
//        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
//        detailProfileFragment.setArguments(bundle);
//        return detailProfileFragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
            setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_profile, container, false);
        mainActivity = (MainActivity) getActivity();

//        linearLayout = view.findViewById(R.id.llDescription);

//
//        TextView detailTextView = (TextView) view.findViewById(R.id.text_name);
//        detailTextView.setText(profile.name);
//
//        CircleImageView circleImageView = view.findViewById(R.id.circle_image);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            circleImageView.setTransitionName(transitionName);
//        }
//
//        Picasso.with(getContext())
//                .load(profile.imageUrl)
//                .noFade()
//                .into(circleImageView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        startPostponedEnterTransition();
//                    }
//
//                    @Override
//                    public void onError() {
//                        startPostponedEnterTransition();
//                    }
//                });
//        linearLayout.setVisibility(View.VISIBLE);

        return view;
    }

}
