package com.example.currencyexchange;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyexchange.adapters.ExchangerAdapter;
import com.example.currencyexchange.models.Bank;
import com.example.currencyexchange.models.Currency;
import com.example.currencyexchange.models.CurrencyRate;
import com.example.currencyexchange.network.ApiClient;
import com.example.currencyexchange.network.TinkoffApi;
import com.example.currencyexchange.network.TvoiBankApi;
import com.example.currencyexchange.network.TvoiBankResponse;
import com.example.currencyexchange.network.raiffeisen.RaiffeisenApi;
import com.example.currencyexchange.network.raiffeisen.RaiffeisenResponse;
import com.example.currencyexchange.network.VtbApi;
import com.example.currencyexchange.network.VtbResponse;
import com.example.currencyexchange.network.AlfaApi;
import com.example.currencyexchange.network.AlfaResponse;
import com.example.currencyexchange.network.SovcomApi;
import com.example.currencyexchange.network.SovcomResponse;
import com.google.android.material.snackbar.Snackbar;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentExchangers extends Fragment {

    private final List<Bank> banks = new ArrayList<>();
    private ExchangerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exchangers, container, false);

        Toolbar tb = v.findViewById(R.id.toolbar_exchangers);



        RecyclerView rv = v.findViewById(R.id.rv_exchangers);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExchangerAdapter(banks);
        rv.setAdapter(adapter);

        loadTinkoff();
        loadRaiffeisen();
        loadTvoiBank();
        loadVtb();
        loadAlfa();
        loadSovcom();
        return v;
    }

    private void loadTinkoff() {
        TinkoffApi api = ApiClient.getRetrofit().create(TinkoffApi.class);
        api.getRates().enqueue(new Callback<com.example.currencyexchange.models.TinkoffResponse>() {
            @Override public void onResponse(Call<com.example.currencyexchange.models.TinkoffResponse> call,
                                             Response<com.example.currencyexchange.models.TinkoffResponse> resp) {
                if (!isAdded()) return;
                if (resp.isSuccessful() && resp.body() != null && resp.body().payload != null) {
                    List<CurrencyRate> filt = new ArrayList<>();
                    for (CurrencyRate rate : resp.body().payload.rates) {
                        if (rate != null
                                && "DebitCardsTransfers".equals(rate.category)
                                && "643".equals(rate.toCurrency.strCode)) {
                            filt.add(rate);
                        }
                    }
                    banks.add(new Bank("Т-Банк", R.drawable.ic_tinkoff, filt));
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(requireView(), "Не удалось загрузить Т-Банк", Snackbar.LENGTH_LONG).show();
                }
            }
            @Override public void onFailure(Call<com.example.currencyexchange.models.TinkoffResponse> call, Throwable t) {
                Log.e("Exchangers", "Tinkoff error", t);
                Snackbar.make(requireView(), "Ошибка сети Т-Банк", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void loadRaiffeisen() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://www.raiffeisen.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RaiffeisenApi api = rf.create(RaiffeisenApi.class);
        api.getRates("RCONNECT", "AUD,GBP,DKK,USD,EUR,KZT,CAD,CNY,NOK,SEK,CHF")
                .enqueue(new Callback<RaiffeisenResponse>() {
                    @Override public void onResponse(Call<RaiffeisenResponse> call, Response<RaiffeisenResponse> resp) {
                        if (!isAdded()) return;
                        RaiffeisenResponse body = resp.body();
                        if (resp.isSuccessful() && body != null && body.success && body.data != null) {
                            var rubBlock = body.data.rates.get(0);
                            List<CurrencyRate> list = new ArrayList<>();
                            for (var ex : rubBlock.exchange) {
                                Currency from = new Currency();
                                from.strCode = ex.code;
                                from.code = ex.code;

                                Currency to = new Currency();
                                to.strCode = rubBlock.code;
                                to.code = rubBlock.code;
                                to.name = rubBlock.title.russian;

                                CurrencyRate cr = new CurrencyRate();
                                cr.fromCurrency = from;
                                cr.toCurrency = to;
                                cr.buy = ex.rates.buy.value;
                                cr.sell = ex.rates.sell.value;
                                list.add(cr);
                            }
                            banks.add(new Bank("Райффайзен Банк", R.drawable.ic_raiffeisen, list));
                            adapter.notifyDataSetChanged();
                        } else {
                            Snackbar.make(requireView(), "Не удалось загрузить Райффайзен", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override public void onFailure(Call<RaiffeisenResponse> call, Throwable t) {
                        Log.e("Exchangers", "Raiffeisen error", t);
                        Snackbar.make(requireView(), "Ошибка сети Райффайзен", Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    private void loadTvoiBank() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("http://188.127.224.60/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TvoiBankApi api = rf.create(TvoiBankApi.class);
        api.getRates().enqueue(new Callback<TvoiBankResponse>() {
            @Override public void onResponse(Call<TvoiBankResponse> call, Response<TvoiBankResponse> resp) {
                if (!isAdded() || !resp.isSuccessful() || resp.body() == null) return;
                var items = resp.body().reresult;
                if (items == null) return;
                List<CurrencyRate> list = new ArrayList<>();
                for (var it : items) {
                    Currency from = new Currency();
                    from.code    = it.strCode;
                    from.strCode = it.strCode;
                    from.name    = it.nameCurrency;
                    Currency to = new Currency();
                    to.code    = "RUB";
                    to.strCode = "RUB";
                    to.name    = "Российский рубль";
                    CurrencyRate cr = new CurrencyRate();
                    cr.fromCurrency = from;
                    cr.toCurrency   = to;
                    cr.buy  = it.value;
                    cr.sell = it.value;
                    list.add(cr);
                }
                banks.add(new Bank("ТвойБанк", R.drawable.ic_tvoibank, list));
                adapter.notifyDataSetChanged();
            }
            @Override public void onFailure(Call<TvoiBankResponse> call, Throwable t) {
                Log.e("Exchangers", "TvoiBank error", t);
                Snackbar.make(requireView(), "Ошибка сети ТвойБанк", Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void loadVtb() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://www.vtb.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VtbApi api = rf.create(VtbApi.class);
        api.getRates(1, 1).enqueue(new Callback<VtbResponse>() {
            @Override
            public void onResponse(Call<VtbResponse> call, Response<VtbResponse> resp) {
                if (!isAdded() || !resp.isSuccessful() || resp.body() == null) return;
                List<CurrencyRate> list = new ArrayList<>();
                for (VtbResponse.Rate r : resp.body().rates) {
                    Currency from = new Currency();
                    from.code    = r.currency1.code;
                    from.strCode = r.currency1.code;
                    from.name    = r.currency1.rusName;
                    Currency to = new Currency();
                    to.code    = r.currency2.code;
                    to.strCode = r.currency2.code;
                    to.name    = r.currency2.rusName;
                    CurrencyRate cr = new CurrencyRate();
                    cr.fromCurrency = from;
                    cr.toCurrency   = to;
                    cr.buy  = r.bid   / r.scale;
                    cr.sell = r.offer / r.scale;
                    list.add(cr);
                }
                banks.add(new Bank("ВТБ Банк", R.drawable.ic_vtb, list));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<VtbResponse> call, Throwable t) {
                Log.e("Exchangers", "VTB error", t);
                Snackbar.make(requireView(), "Ошибка сети ВТБ", Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void loadAlfa() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://alfabank.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AlfaApi api = rf.create(AlfaApi.class);
        String dateLte = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateLte = OffsetDateTime.now()
                    .toString();
        }
        api.getRates(
                "standardCC",
                "USD,EUR,CNY,GBP,CHF,CZK,TRY",
                dateLte,
                true,
                "makeCash,rateCass"
        ).enqueue(new Callback<AlfaResponse>() {
            @Override
            public void onResponse(Call<AlfaResponse> call, Response<AlfaResponse> resp) {
                if (!isAdded() || !resp.isSuccessful() || resp.body() == null) return;
                List<CurrencyRate> list = new ArrayList<>();
                for (AlfaResponse.CurrencyBlock block : resp.body().data) {
                    if (block.rateByClientType.isEmpty()) continue;
                    AlfaResponse.RateByClientType byClient = block.rateByClientType.get(0);
                    AlfaResponse.RatesByType pick = null;
                    for (AlfaResponse.RatesByType rbt : byClient.ratesByType) {
                        if ("makeCash".equals(rbt.rateType)) {
                            pick = rbt;
                            break;
                        }
                    }
                    if (pick == null) pick = byClient.ratesByType.get(0);
                    double buy  = pick.lastActualRate.buy.originalValue;
                    double sell = pick.lastActualRate.sell.originalValue;

                    Currency from = new Currency();
                    from.code    = block.currencyCode;
                    from.strCode = block.currencyCode;
                    from.name    = block.currencyCode;

                    Currency to = new Currency();
                    to.code    = "RUB";
                    to.strCode = "RUB";
                    to.name    = "Российский рубль";

                    CurrencyRate cr = new CurrencyRate();
                    cr.fromCurrency = from;
                    cr.toCurrency   = to;
                    cr.buy  = buy;
                    cr.sell = sell;
                    list.add(cr);
                }

                banks.add(new Bank("Альфа-Банк", R.drawable.ic_alfa, list));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<AlfaResponse> call, Throwable t) {
                Log.e("Exchangers", "Alfa error", t);
                Snackbar.make(requireView(),
                                "Ошибка сети Альфа-Банк",
                                Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }
    private void loadSovcom() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://api-app.sovcombank.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SovcomApi api = rf.create(SovcomApi.class);
        api.getRates("7700000000000")
                .enqueue(new Callback<SovcomResponse>() {
                    @Override
                    public void onResponse(Call<SovcomResponse> call, Response<SovcomResponse> resp) {
                        if (!isAdded() || !resp.isSuccessful() || resp.body() == null) return;
                        SovcomResponse r = resp.body();
                        List<CurrencyRate> list = new ArrayList<>();
                        String[] codes = {"USD","EUR","CNY","GBP","CHF","AED","THB"};
                        for (String code : codes) {
                            SovcomResponse.CurrencyPair p;
                            switch (code) {
                                case "USD": p = r.usd; break;
                                case "EUR": p = r.eur; break;
                                case "CNY": p = r.cny; break;
                                case "GBP": p = r.gbp; break;
                                case "CHF": p = r.chf; break;
                                case "AED": p = r.aed; break;
                                case "THB": p = r.thb; break;
                                default: continue;
                            }
                            Currency from = new Currency();
                            from.code    = code;
                            from.strCode = code;
                            from.name    = code;

                            Currency to = new Currency();
                            to.code    = "RUB";
                            to.strCode = "RUB";
                            to.name    = "Российский рубль";

                            CurrencyRate cr = new CurrencyRate();
                            cr.fromCurrency = from;
                            cr.toCurrency   = to;
                            cr.buy  = p.buy;
                            cr.sell = p.sell;
                            list.add(cr);
                        }

                        banks.add(new Bank("Совкомбанк", R.drawable.ic_sovcombank, list));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<SovcomResponse> call, Throwable t) {
                        Log.e("Exchangers","Sovcom error",t);
                        Snackbar.make(requireView(),
                                        "Ошибка сети Совкомбанк",
                                        Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
    }
}
