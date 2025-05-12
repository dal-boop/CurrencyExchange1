package com.example.currencyexchange;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.currencyexchange.adapters.FavoritesAdapter;
import com.example.currencyexchange.models.CurrencyCbr;
import com.example.currencyexchange.network.CbrApi;
import com.example.currencyexchange.network.CbrApiClient;
import com.example.currencyexchange.network.CbrResponse;
import com.example.currencyexchange.utils.PrefsHelper;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    private RecyclerView rv;
    private FavoritesAdapter adapter;
    private List<CurrencyCbr> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private boolean isLoading = false;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar tb = v.findViewById(R.id.toolbar);
        tb.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SelectFavoritesFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        });

        rv = v.findViewById(R.id.rv_favorites);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoritesAdapter(list);
        rv.setAdapter(adapter);

        swipeRefresh = v.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.black);
        swipeRefresh.setOnRefreshListener(this::loadFavorites);

        return v;
    }

    @Override public void onResume() {
        super.onResume();
        swipeRefresh.setRefreshing(true);
        loadFavorites();
    }

    private void loadFavorites() {
        if (isLoading) return;
        isLoading = true;

        CbrApi api = CbrApiClient.getRetrofit().create(CbrApi.class);
        api.getDailyRates().enqueue(new Callback<CbrResponse>() {
            @Override
            public void onResponse(Call<CbrResponse> call, Response<CbrResponse> resp) {
                isLoading = false;
                swipeRefresh.setRefreshing(false);
                if (resp.isSuccessful() && resp.body() != null) {
                    updateList(resp.body().valutes);
                }
            }

            @Override
            public void onFailure(Call<CbrResponse> call, Throwable t) {
                isLoading = false;
                swipeRefresh.setRefreshing(false);
                t.printStackTrace();

            }
        });
    }

    private void updateList(List<CurrencyCbr> all) {
        Set<String> sel = PrefsHelper.getFavoritesWithDefaults(requireContext());
        list.clear();
        for (CurrencyCbr c : all) {
            if (sel.contains(c.charCode)) {
                list.add(c);
            }
        }
        adapter.notifyDataSetChanged();
    }
}

