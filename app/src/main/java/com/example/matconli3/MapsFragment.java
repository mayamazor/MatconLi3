package com.example.matconli3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matconli3.model.Recipe;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;

public class MapsFragment extends Fragment {

    GoogleMap map;
    MapsFragmentViewModel viewModel;
    LiveData<List<Recipe>> liveData;
    List<Recipe> data = new LinkedList<>();
    String lastClicked = "";

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            setMarkers();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.0461, 34.8516), 8));
        }
    };

    private void setMarkers() {
        map.clear();
        for (Recipe recipe : data) {
            Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(recipe.lat, recipe.lon)).title(recipe.recipeName));
            marker.setTag(recipe.id);

            map.setOnMarkerClickListener(clickedMarker -> {
                String tag = clickedMarker.getTag().toString();
                Log.d("TAG", "Clicked! title: " + tag);

                if (lastClicked.equals(tag)) {
                    lastClicked = "";
                    Log.d("TAG", "Window true");

                    Recipe recipe2 = null;
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getId().equals(tag)) {
                            recipe2 = data.get(i);
                        }
                    }

                    MapsFragmentDirections.ActionMapsFragmentToRecipePage action = MapsFragmentDirections.actionMapsFragmentToRecipePage(recipe2);
                    Navigation.findNavController(getActivity(), R.id.main_nav_host).navigate(action);
                } else {
                    lastClicked = tag;
                    Log.d("TAG", "Window false");
                }
                return false;
            });
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        viewModel = new ViewModelProvider(this).get(MapsFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                List<Recipe> reversedData = reverseData(recipes);
                data = reversedData;
                setMarkers();
            }

        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private List<Recipe> reverseData(List<Recipe> recipes) {
        List<Recipe> reversedData = new LinkedList<>();
        for (Recipe recipe : recipes) {
            reversedData.add(0, recipe);
        }
        return reversedData;
    }
}