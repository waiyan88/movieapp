package wy.chris.movieapp;


import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesFragment extends Fragment {

AdView mAdView;
View view;
InterstitialAd mInterstitialAd;
    GridView gridView;
    RecyclerView recyclerView;

    public SeriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View convertview = inflater.inflate(R.layout.fragment_series, container, false);
        gridView = convertview.findViewById(R.id.allseries);




        view=convertview;

        recyclerView = convertview.findViewById(R.id.newseries);

        loadAds();
        ArrayList<SeriesModel> seriesModels = new ArrayList<SeriesModel>();
        seriesModels.add(new SeriesModel("https://image.tmdb.org/t/p/w185/r8BhphHIruCL2jM4sd1Zs6sMJov.jpg", "Tale of Nokdu","9.5" ));
        seriesModels.add(new SeriesModel("https://upload.wikimedia.org/wikipedia/en/thumb/6/67/Melting_Me_Softly.jpg/250px-Melting_Me_Softly.jpg", "Melting me Softlay", "9.3"));
        seriesModels.add(new SeriesModel("https://image.tmdb.org/t/p/w185/bBIiRnCVOifc1eaW7sCbJHSWJ68.jpg", "VIP","9.5" ));

        GridApater ad = new GridApater(seriesModels);
        gridView.setAdapter(ad);

        SeriesRcAdapter rcAdapter=new SeriesRcAdapter(seriesModels);
        recyclerView.setAdapter(rcAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return convertview;
    }

    public void loadAds()
    {
        MobileAds.initialize(getContext(),GoogleAdMob.app_id);
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(GoogleAdMob.interstatial_id);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    public  class SeriesRcAdapter extends RecyclerView.Adapter<SeriesRcAdapter.SeriesHolder>{

        @NonNull
        @Override
        public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.movieitems,parent,false);
            SeriesHolder seriesHolder=new SeriesHolder(view);
            return seriesHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SeriesHolder holder, int position) {
            final SeriesModel seriesModel=seriesModels.get(position);
            Glide.with(getContext())
                    .load(seriesModel.imagelink)
                    .into(holder.movieimage);

            holder.moviename.setText(seriesModel.seriesname);
            holder.movieimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();

                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdClicked() {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                startActivity(intent);
                            }
                        });



                    }
                    else {
                        Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return seriesModels.size();
        }

        ArrayList<SeriesModel> seriesModels;

        public SeriesRcAdapter(ArrayList<SeriesModel> movieModels) {
            this.seriesModels = movieModels;
        }
        public class SeriesHolder extends RecyclerView.ViewHolder {

            ImageView movieimage;
            TextView moviename;
            public SeriesHolder(@NonNull View itemView) {
                super(itemView);
                movieimage=itemView.findViewById(R.id.movieimage);
                moviename = itemView.findViewById(R.id.movienamegridview);
            }
        }
    }
    private class GridApater extends BaseAdapter {
        ArrayList<SeriesModel> seriesModels;

        public GridApater(ArrayList<SeriesModel> seriesModels) {
            this.seriesModels = seriesModels;
        }

        @Override
        public int getCount() {
            return seriesModels.size();
        }

        @Override
        public Object getItem(int position) {
            return seriesModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater inflater = getLayoutInflater();
            final SeriesModel seriesModel = seriesModels.get(position);
            View view = inflater.inflate(R.layout.movieitems, null);
            ImageView seriesimag = view.findViewById(R.id.movieimage);
            TextView seriesname = view.findViewById(R.id.movienamegridview);
            Glide.with(getContext())
                    .load(seriesModel.imagelink)
                    .into(seriesimag);
            seriesname.setText(seriesModel.seriesname);

            seriesimag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();

                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdClicked() {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                startActivity(intent);
                            }
                        });



                    }
                    else {
                        Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                        startActivity(intent);
                    }
                }
            });

            return view;
        }
    }
}

