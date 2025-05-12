package com.example.currencyexchange.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyexchange.R;
import com.example.currencyexchange.models.Bank;
import com.example.currencyexchange.models.CurrencyRate;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

public class ExchangerAdapter extends RecyclerView.Adapter<ExchangerAdapter.BankViewHolder> {

    private final List<Bank> banks;

    public ExchangerAdapter(List<Bank> banks) {
        this.banks = banks;
    }

    @NonNull @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        return new BankViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        holder.bind(banks.get(position));
    }

    @Override public int getItemCount() {
        return banks.size();
    }

    class BankViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView  card;
        ImageView         ivLogo;
        TextView          tvName;
        RecyclerView      rvRates;

        BankViewHolder(@NonNull View itemView) {
            super(itemView);
            card    = (MaterialCardView)itemView;
            ivLogo  = itemView.findViewById(R.id.iv_bank_logo);
            tvName  = itemView.findViewById(R.id.tv_bank_name);
            rvRates = itemView.findViewById(R.id.rv_currency_rates);

            card.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Bank b = banks.get(pos);
                b.isExpanded = !b.isExpanded;
                notifyItemChanged(pos);
            });
        }

        void bind(Bank bank) {
            ivLogo.setImageResource(bank.logoRes);
            tvName.setText(bank.name);

            rvRates.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rvRates.setAdapter(new CurrencyAdapter(bank.rates));

            rvRates.setVisibility(bank.isExpanded ? View.VISIBLE : View.GONE);
        }
    }

    private static class CurrencyAdapter
            extends RecyclerView.Adapter<CurrencyAdapter.VH> {

        private final List<CurrencyRate> rates;
        CurrencyAdapter(List<CurrencyRate> rates) { this.rates = rates; }

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_currency_rate, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int pos) {
            CurrencyRate r = rates.get(pos);

            String codeField = r.fromCurrency.code != null ? r.fromCurrency.code : "";
            String nameField = r.fromCurrency.name != null ? r.fromCurrency.name : "";

            String displayCode;
            if (codeField.matches("^[A-Za-z]{3}$")) {
                displayCode = codeField;
            } else if (nameField.matches("^[A-Za-z]{3}$")) {
                displayCode = nameField;
            } else {
                displayCode = nameField;
            }

            holder.tvName.setText(displayCode);
            holder.tvBuy.setText(String.format(Locale.getDefault(), "Покупка: %.2f", r.buy));
            holder.tvSell.setText(String.format(Locale.getDefault(), "Продажа: %.2f", r.sell));
        }


        @Override public int getItemCount() { return rates.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvBuy, tvSell;

            VH(@NonNull View v) {
                super(v);
                tvName = v.findViewById(R.id.tv_currency_name);
                tvBuy  = v.findViewById(R.id.tv_buy_rate);
                tvSell = v.findViewById(R.id.tv_sell_rate);
            }
        }
    }
}
