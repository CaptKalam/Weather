package com.paszylk.marcin.weather.cities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paszylk.marcin.weather.R;
import com.paszylk.marcin.weather.addcity.AddCityActivity;
import com.paszylk.marcin.weather.citydetail.CityDetailActivity;
import com.paszylk.marcin.weather.cities.domain.model.CityDaoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

public class CitiesFragment extends Fragment implements CitiesContract.View {

    private CitiesContract.Presenter presenter;

    private CitiesAdapter citiesAdapter;

    private View noCitiesView;

    private TextView noCitiesMainView;

    private TextView noCitiesAddView;

    private LinearLayout citiesLayout;

    public CitiesFragment() {
        // Requires empty public constructor
    }

    public static CitiesFragment newInstance() {
        return new CitiesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        citiesAdapter = new CitiesAdapter(new ArrayList<CityDaoMapper>(0), itemListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull CitiesContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.cities_frag, container, false);

        ListView listView = (ListView) root.findViewById(R.id.cities_list);
        listView.setAdapter(citiesAdapter);
        citiesLayout = (LinearLayout) root.findViewById(R.id.citiesLL);

        // Set up no cities view
        noCitiesView = root.findViewById(R.id.noCities);
        noCitiesMainView = (TextView) root.findViewById(R.id.noCitiesMain);
        noCitiesAddView = (TextView) root.findViewById(R.id.noCitiesAdd);
        noCitiesAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCity();
            }
        });

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_city);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNewCity();
            }
        });

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadCities(false);
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                presenter.loadCities(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cities_fragment_menu, menu);
    }

    /**
     * Listener for clicks on cities in the ListView.
     */
    CityItemListener itemListener = new CityItemListener() {
        @Override
        public void onCityClick(CityDaoMapper clickedCity) {
            presenter.openCityDetails(clickedCity);
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showCities(List<CityDaoMapper> cities) {
        citiesAdapter.replaceData(cities);

        citiesLayout.setVisibility(View.VISIBLE);
        noCitiesView.setVisibility(View.GONE);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_city_message));
    }

    @Override
    public void showAddCity() {
        Intent intent = new Intent(getContext(), AddCityActivity.class);
        startActivityForResult(intent, AddCityActivity.REQUEST_ADD_CITY);
    }

    @Override
    public void showCityDetailsUi(long cityId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), CityDetailActivity.class);
        intent.putExtra(CityDetailActivity.EXTRA_CITY_ID, cityId);
        startActivity(intent);
    }

    @Override
    public void showLoadingCitiesError() {
        showMessage(getString(R.string.loading_cities_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private static class CitiesAdapter extends BaseAdapter {

        private List<CityDaoMapper> mCities;
        private CityItemListener mItemListener;

        public CitiesAdapter(List<CityDaoMapper> cities, CityItemListener itemListener) {
            setList(cities);
            mItemListener = itemListener;
        }

        public void replaceData(List<CityDaoMapper> cities) {
            setList(cities);
            notifyDataSetChanged();
        }

        private void setList(List<CityDaoMapper> cities) {
            mCities = checkNotNull(cities);
        }

        @Override
        public int getCount() {
            return mCities.size();
        }

        @Override
        public CityDaoMapper getItem(int i) {
            return mCities.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.city_item, viewGroup, false);
            }

            final CityDaoMapper city = getItem(i);
            TextView cityName = (TextView) rowView.findViewById(R.id.find_city);
            TextView temperature = (TextView) rowView.findViewById(R.id.temperature);

            cityName.setText(city.getName());
            temperature.setText(String.format(Locale.getDefault(), "%s \u00b0C", city.getTemperatureValue()));

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onCityClick(city);
                }
            });

            return rowView;
        }
    }

    public interface CityItemListener {

        void onCityClick(CityDaoMapper clickedCity);
    }

}
