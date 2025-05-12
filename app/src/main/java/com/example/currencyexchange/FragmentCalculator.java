package com.example.currencyexchange;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import androidx.annotation.*;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.currencyexchange.adapters.CalculatorAdapter;
import com.example.currencyexchange.models.CurrencyCbr;
import com.example.currencyexchange.utils.PrefsHelper;
import java.util.ArrayList;
import java.util.List;

public class FragmentCalculator extends Fragment {

    private CalculatorAdapter adapter;
    private List<CurrencyCbr> allRates;
    private RecyclerView rv;
    private Toolbar toolbar;
    private com.google.android.material.textfield.TextInputEditText etAmount;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);

        toolbar = v.findViewById(R.id.toolbar_calculator);
        toolbar.setNavigationOnClickListener(x -> requireActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SelectFavoritesFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            return false;
        });

        rv = v.findViewById(R.id.rv_calculator);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CalculatorAdapter();
        rv.setAdapter(adapter);


        etAmount = v.findViewById(R.id.et_amount);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int st,int c,int a){}
            @Override public void onTextChanged(CharSequence s,int st,int b,int c){}
            @Override public void afterTextChanged(Editable s) {
                double amt = 0;
                try { amt = Double.parseDouble(s.toString()); }
                catch (NumberFormatException ignored){}
                adapter.setAmount(amt);
            }
        });

        allRates = AppData.cbrRates != null
                ? AppData.cbrRates
                : new ArrayList<>();

        loadFavoritesIntoCalculator();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavoritesIntoCalculator();
    }

    private void loadFavoritesIntoCalculator() {
        List<CurrencyCbr> favs = new ArrayList<>();
        for (CurrencyCbr c : allRates) {
            if (PrefsHelper.getFavoritesWithDefaults(requireContext())
                    .contains(c.charCode)) {
                favs.add(c);
            }
        }
        adapter.setItems(favs);
        String cur = etAmount.getText().toString();
        double amt = 0;
        try { amt = Double.parseDouble(cur); } catch (Exception ignored) {}
        adapter.setAmount(amt);
    }
}
