package com.example.currencyexchange;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyexchange.adapters.SelectCurrencyAdapter;
import com.example.currencyexchange.utils.PrefsHelper;

import java.util.*;

public class SelectFavoritesFragment extends Fragment {

    private static final String[] ALL_CODES = {
            "USD","EUR","GBP","CNY","JPY","CHF"
    };

    private SelectCurrencyAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_select_favorites, container, false);

        RecyclerView rv = v.findViewById(R.id.rv_currencies);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        Set<String> saved = PrefsHelper.getFavoritesWithDefaults(requireContext());
        List<SelectCurrencyAdapter.CurrencyOption> opts = new ArrayList<>();
        for (String code : ALL_CODES) {
            opts.add(new SelectCurrencyAdapter.CurrencyOption(
                    code, saved.contains(code)
            ));
        }

        adapter = new SelectCurrencyAdapter(opts);
        rv.setAdapter(adapter);

        Button btn = v.findViewById(R.id.btn_save);
        btn.setOnClickListener(view -> {
            List<String> sel = adapter.getSelectedCodes();
            PrefsHelper.saveFavorites(requireContext(), new HashSet<>(sel));
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return v;
    }
}
