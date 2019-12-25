package wy.chris.movieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class SeriesEpActivity extends AppCompatActivity {

    public static  SeriesModel seriesModel;
    public static String seriesName="";
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_ep);
        MobileAds.initialize(getApplicationContext(),GoogleAdMob.app_id);
        mAdView =findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //Create InterstitialAd

        ImageView imageView=findViewById(R.id.seriesIamge);
        Glide.with(getApplicationContext())
                .load(seriesModel.seriesImageLink)
                .into(imageView);
        TextView textView=findViewById(R.id.seriesName);
        textView.setText(seriesModel.seriesName);
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(GoogleAdMob.interstatial_id);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        listView=findViewById(R.id.eplist);

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference movies=db.collection("movies");
        movies.whereEqualTo("SeriesName",seriesModel.seriesName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MovieModel> movieModels=new ArrayList<>();
                for(DocumentSnapshot s: queryDocumentSnapshots)
                {
                    MovieModel movieModel=s.toObject(MovieModel.class);
                    movieModels.add(movieModel);


                }
                GridAdapter adapter=new GridAdapter(movieModels);
                listView.setAdapter(adapter);
            }
        });

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
            View view1=inflater.inflate(R.layout.seriesep,null);

            TextView epsr=view1.findViewById(R.id.epNo);
            epsr.setText(String.valueOf(i+1));
            TextView moviename=view1.findViewById(R.id.epName);


            moviename.setText(movieModel.movieName);

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
                                Intent intent=new Intent(getApplicationContext(),ViewMovieActivity.class);
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
                                Intent intent=new Intent(getApplicationContext(),ViewMovieActivity.class);
                                ViewMovieActivity.videolink=movieModel.movieVideoLink;
                                startActivity(intent);
                            }
                        });
                    }
                    else
                    {
                        Intent intent=new Intent(getApplicationContext(),ViewMovieActivity.class);
                        ViewMovieActivity.videolink=movieModel.movieVideoLink;
                        startActivity(intent);
                    }
                }
            });
            return view1;
        }
    }
}
