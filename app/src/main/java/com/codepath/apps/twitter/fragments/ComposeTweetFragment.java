package com.codepath.apps.twitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.TwitterClient;
import com.codepath.apps.twitter.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ComposeTweetFragment extends DialogFragment {

    @BindView(R.id.ivUserProfileImage) ImageView ivUserProfileImage;
    @BindView(R.id.tvUserScreenName) TextView tvUserScreenName;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.etTweet) EditText etTweet;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    @BindView(R.id.ivClose) ImageView ivClose;
    @BindView(R.id.tvCharCount) TextView tvCharCount;

    private static final String ARG_USER = "user";

    private User user;

    private OnSubmitTweetListener listener;

    private TwitterClient client;

    public ComposeTweetFragment() {
    }

    public static ComposeTweetFragment newInstance(User user) {
        ComposeTweetFragment fragment = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        if (getArguments() != null) {
            user = Parcels.unwrap(getArguments().getParcelable(ARG_USER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName.setText(user.getName());
        String atScreenName = getResources().getString(R.string.at_screen_name, user.getScreenName());
        tvUserScreenName.setText(atScreenName);
        // Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivUserProfileImage);

        Picasso.with(getContext())
                .load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(ivUserProfileImage);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etTweet.getText().toString();
                listener.onSubmitTweetListener(tweet);

                dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvCharCount.setText(String.valueOf(140 - etTweet.getText().length()));
        TextWatcher etTweetWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvCharCount.setText(String.valueOf(140 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etTweet.addTextChangedListener(etTweetWatcher);
        etTweet.setSelection(etTweet.getText().length());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubmitTweetListener) {
            listener = (OnSubmitTweetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSubmitTweetListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnSubmitTweetListener {
        void onSubmitTweetListener(String tweet);
    }
}
