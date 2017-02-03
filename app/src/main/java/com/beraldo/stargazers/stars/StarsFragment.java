package com.beraldo.stargazers.stars;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beraldo.stargazers.R;
import com.beraldo.stargazers.data.source.model.Star;
import com.beraldo.stargazers.data.source.GenericError;
import com.beraldo.stargazers.data.source.net.APIError;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class StarsFragment extends Fragment implements StarsContract.View {
    private StarsContract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private StarsAdapter adapter;
    protected ProgressDialog dialog;

    public StarsFragment() {
        // Requires empty public constructor
    }

    public static StarsFragment newInstance() {
        return new StarsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull StarsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_frag, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        emptyView = (TextView) rootView.findViewById(R.id.no_stargazers);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        return rootView;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(active) showDialog(getActivity());
        else dismissDialog();
    }

    @Override
    public void showStars(ArrayList<Star> stars) {
        if(stars.size() == 0) showEmptyView();
        else {
            // Populate adapter and set it to the recycler
            adapter = new StarsAdapter(stars, getActivity());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoadingStarsError(GenericError err) {
        if(err instanceof APIError)
            showMessageDialog("Ops, l'API ha restituito un errore.\n" + err.getMessage(),
                    null, null, null, null);
        else
            showMessageDialog("Ahi, c'è stato un errore.\n" + err.getMessage(),
                    null, null, null, null);
    }

    public void showMessageDialog(String message, String positiveButtonLabel, String negativeButtonLabel,
                                  DialogInterface.OnClickListener positiveButtonListener,
                                  DialogInterface.OnClickListener negativeButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(false);

        if (positiveButtonLabel != null)
            builder.setPositiveButton(positiveButtonLabel, positiveButtonListener);
        else builder.setPositiveButton("OK", null);
        if (negativeButtonLabel != null)
            builder.setNegativeButton(negativeButtonLabel, negativeButtonListener);

        builder.create().show();
    }

    protected void showDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage(getResources().getString(R.string.dialog_wait));
        dialog.show();
    }

    protected void dismissDialog() {
        if ((dialog != null) && (dialog.isShowing())) dialog.dismiss();
    }

    // Build a simple alert dialog to do the search
    public void showSearchDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        alert.setTitle("Cerca stargazers");
        alert.setMessage("Inserisci username e repo");
        final EditText username = new EditText(getActivity());
        username.setHint("Username");
        final EditText reponame = new EditText(getActivity());
        reponame.setHint("Repository");
        layout.addView(username);
        layout.addView(reponame);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String username_value = username.getText().toString();
                String reponame_value = reponame.getText().toString();
                if (!username_value.isEmpty() && !reponame_value.isEmpty()) {
                    mPresenter.loadStars(username_value, reponame_value);
                } else {
                    Toast.makeText(getActivity(), "Input non valido",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getActivity(), "Annullato",
                        Toast.LENGTH_LONG).show();
            }
        });

        alert.show();
    }

    public void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private static class StarsAdapter extends RecyclerView.Adapter<StarsAdapter.ViewHolder> {
        private ArrayList<Star> mDataset;
        private Context context;

        public StarsAdapter(ArrayList<Star> list, Context context) {
            this.context = context;
            mDataset = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,
                    parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Star star = mDataset.get(position);
            holder.starUsername.setText(star.getLogin());

            Glide.with(context)
                    .load(star.getAvatarUrl())
                    .crossFade()
                    .override(240, 240)
                    .into(holder.starAvatar);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        protected static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView starUsername;
            private ImageView starAvatar;

            private ViewHolder(View itemView) {
                super(itemView);
                starUsername = (TextView) itemView.findViewById(R.id.card_username);
                starAvatar = (ImageView) itemView.findViewById(R.id.card_avatar);
            }
        }
    }
}
