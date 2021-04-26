package metri.amit.testapplicationopen.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import metri.amit.testapplicationopen.databinding.FragmentRouteBinding;
import metri.amit.testapplicationopen.model.ErrorData;
import metri.amit.testapplicationopen.model.RouteEntity;
import metri.amit.testapplicationopen.viewmodel.RoutesViewModel;

/*
 * Fragment to display routes
 * */
public class RoutesFragment extends Fragment {

    private String TAG = "RoutesFragment";
    private RoutesViewModel routesViewModel;
    private FragmentRouteBinding mBinding;
    private RoutesAdapter routesAdapter;
    private List<RouteEntity> routeEntities = new ArrayList<>();
    private Context context;
    Handler handler = new Handler();
    /*
     * Runnable to update the list
     * */
    Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            //Initiate network call and insert the data into Room DB
            routesViewModel.getRoutesFromNetwork();
            // Handler to update the data for every minute.
            handler.postDelayed(this, 1000 * 60);
        }
    };

    public RoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        /* ViewModel initialization with fragment lifecycle scope */
        routesViewModel = new ViewModelProvider(RoutesFragment.this).get(RoutesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
         * View binding is enabled
         * Inflate the view here
         * */
        mBinding = FragmentRouteBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();

        /*
         * Subscribe for Room DB changes
         * */
        mBinding.progressCircular.setVisibility(View.VISIBLE);
        routesViewModel.getRoutesFromDB().observe(getViewLifecycleOwner(), new Observer<List<RouteEntity>>() {
            @Override
            public void onChanged(List<RouteEntity> routeEntities) {
                mBinding.progressCircular.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Fetched data from DB");
                RoutesFragment.this.routeEntities = routeEntities;
                routesAdapter.updateList(routeEntities);
            }
        });

        /*
         * Subscribe for error data.
         * Error data can be emitted in case of
         * network failures, timeouts, data validation failures or
         * any other exceptions.
         * */
        routesViewModel.subscribeForError().observe(getViewLifecycleOwner(), new Observer<ErrorData>() {
            @Override
            public void onChanged(ErrorData errorData) {
                showAlert(errorData.getErrorTitle(), errorData.getErrorMessage());
                mBinding.progressCircular.setVisibility(View.INVISIBLE);
            }
        });

        handler.post(runnableCode);

        mBinding.textViewRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnableCode);
                handler.post(runnableCode);
                mBinding.textViewRetry.setVisibility(View.INVISIBLE);
                mBinding.progressCircular.setVisibility(View.VISIBLE);
            }
        });
    }


    private AlertDialog alertDialog;

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBinding.textViewRetry.setVisibility(View.VISIBLE);
                dialog.cancel();
            }
        });

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Removes pending code execution
        handler.removeCallbacks(runnableCode);
    }

    /*
     * Set recyclerView
     * */
    private void setRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        routesAdapter = new RoutesAdapter(routeEntities);
        mBinding.recyclerView.setAdapter(routesAdapter);
    }
}