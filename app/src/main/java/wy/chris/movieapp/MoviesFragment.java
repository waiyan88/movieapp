package wy.chris.movieapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_movies, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final RecyclerView rc_new_movies=view.findViewById(R.id.newmovies);
        final GridView g_new_movies=view.findViewById(R.id.allmovies);

        MobileAds.initialize(getContext(),GoogleAdMob.app_id);
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //Create InterstitialAd

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(GoogleAdMob.interstatial_id);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //Create InterstitialAd


        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference movieRef=db.collection("movies");
        movieRef.whereEqualTo("movieCategory","AllMovies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                ArrayList<MovieModel> movieModels=new ArrayList<MovieModel>();
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    MovieModel movieModel= snapshot.toObject(MovieModel.class);
                    movieModels.add(movieModel);
                }
                GridAdapter adapter=new GridAdapter(movieModels);
                g_new_movies.setAdapter(adapter);
            }
        });

        movieRef.whereEqualTo("movieCategory","NewMovies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                ArrayList<MovieModel> movieModels=new ArrayList<MovieModel>();
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    MovieModel movieModel= snapshot.toObject(MovieModel.class);
                    movieModels.add(movieModel);
                }
                movieAdapter md=new movieAdapter(movieModels);
               rc_new_movies.setAdapter(md);
            }
        });





        LinearLayoutManager lm=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rc_new_movies.setLayoutManager(lm);


        return  view;
    }

    public class movieAdapter extends RecyclerView.Adapter<movieAdapter.movieholder>{
        ArrayList <MovieModel> moviemodels;

        @NonNull
        @Override
        public movieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.movieitems,parent,false);
           movieholder mh=new movieholder(view);


            return mh;
        }

        @Override
        public void onBindViewHolder(@NonNull movieholder holder, int position) {
         final MovieModel movieModel=moviemodels.get(position);
            try {
                movieModel.movieVideoLink=MediaFireAPI.getMedaiFireFile(movieModel.movieVideoLink);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Glide.with(getContext())
                 .load(movieModel.movieImageLink)
                 .into(holder.imageView);
         holder.textView.setText(movieModel.movieName);
         holder.imageView.setOnClickListener(new View.OnClickListener() {
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
                             ViewMovieActivity.videolink=movieModel.movieVideoLink;
                             startActivity(intent);
                         }

                         @Override
                         public void onAdOpened() {
                             // Code to be executed when the ad is displayed.
                         }

                         @Override
                         public void onAdClicked() {
                             Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                             ViewMovieActivity.videolink=movieModel.movieVideoLink;
                             startActivity(intent);
                         }

                         @Override
                         public void onAdLeftApplication() {
                             // Code to be executed when the user has left the app.
                         }

                         @Override
                         public void onAdClosed() {
                             Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                             ViewMovieActivity.videolink=movieModel.movieVideoLink;
                             startActivity(intent);
                         }
                     });



                 }
                 else {
                     Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                     ViewMovieActivity.videolink=movieModel.movieVideoLink;
                     startActivity(intent);
                 }
             }
         });



        }

        @Override
        public int getItemCount() {
            return moviemodels.size();
        }

        public movieAdapter(ArrayList<MovieModel> moviemodels) {
            this.moviemodels = moviemodels;
        }

        public class movieholder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;

            public movieholder(@NonNull View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.movieimage);
                textView=itemView.findViewById(R.id.movienamegridview);

            }
        }
    }


    private  class  GridAdapter extends BaseAdapter
    {
        ArrayList<MovieModel> movieModels;

        public GridAdapter(ArrayList<MovieModel> movieModels) {
            this.movieModels = movieModels;
        }

        @Override
        public int getCount() {
            return movieModels.size();
        }

        @Override
        public Object getItem(int i) {
            return movieModels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater=getLayoutInflater();
            final  MovieModel movieModel=movieModels.get(i);
            try {
                movieModel.movieVideoLink=MediaFireAPI.getMedaiFireFile(movieModel.movieVideoLink);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            View view1=inflater.inflate(R.layout.movieitems,null);
            ImageView imageView=view1.findViewById(R.id.movieimage);
            TextView moviename=view1.findViewById(R.id.movienamegridview);
            Glide.with(getContext())
                    .load(movieModel.movieImageLink)
                    .into(imageView);

            moviename.setText(movieModel.movieName);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                ViewMovieActivity.videolink=movieModel.movieVideoLink;
                                startActivity(intent);
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                ViewMovieActivity.videolink=movieModel.movieVideoLink;
                                startActivity(intent);
                            }
                        });
                    }
                    else
                    {
                        Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                        ViewMovieActivity.videolink=movieModel.movieVideoLink;
                        startActivity(intent);
                    }

                }
            });
            moviename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                            }

                            @Override
                            public void onAdFailedToLoad(int errorCode) {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                ViewMovieActivity.videolink=movieModel.movieVideoLink;
                                startActivity(intent);
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when the ad is displayed.
                            }

                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdLeftApplication() {
                                // Code to be executed when the user has left the app.
                            }

                            @Override
                            public void onAdClosed() {
                                Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                                ViewMovieActivity.videolink=movieModel.movieVideoLink;
                                startActivity(intent);
                            }
                        });
                    }
                    else
                    {
                        Intent intent=new Intent(getContext(),ViewMovieActivity.class);
                        ViewMovieActivity.videolink=movieModel.movieVideoLink;
                        startActivity(intent);
                    }
                }
            });
            return view1;
        }
    }





}
