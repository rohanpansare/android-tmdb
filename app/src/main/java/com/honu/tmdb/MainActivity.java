package com.honu.tmdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.honu.tmdb.rest.Movie;

public class MainActivity extends AppCompatActivity implements  MoviePosterGridFragment.OnMovieSelectedListener {

    static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_MOVIE = "movie";

    boolean mTwoPaneMode = false;

    // remember the selected movie
    Movie mSelectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreateView");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        }

        if (savedInstanceState != null) {
            mSelectedMovie = savedInstanceState.getParcelable(KEY_MOVIE);
        }

        if (findViewById(R.id.fragment_detail) != null) {
            mTwoPaneMode = true;
            if (mSelectedMovie != null) {
                TextView titleView = (TextView) findViewById(R.id.movie_detail_title);
                titleView.setText(mSelectedMovie.getTitle());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_MOVIE, mSelectedMovie);
    }

    @Override
    public void onMovieSelected(Movie movie) {

        Log.d(TAG, "Show movie details: " + movie.getTitle());

        mSelectedMovie = movie;

        if (mTwoPaneMode) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetailFragment.KEY_MOVIE, movie);
            Fragment fragment = MovieDetailFragment.newInstance(movie);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_detail, fragment).commit();
            TextView titleView = (TextView) findViewById(R.id.movie_detail_title);
            titleView.setText(movie.getTitle());
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movie", movie);
            this.startActivity(intent);
        }
    }
}
