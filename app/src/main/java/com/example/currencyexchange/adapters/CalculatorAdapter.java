package com.example.currencyexchange.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.currencyexchange.R;
import com.example.currencyexchange.models.CurrencyCbr;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalculatorAdapter
        extends RecyclerView.Adapter<CalculatorAdapter.VH> {

    private final List<CurrencyCbr> items = new ArrayList<>();
    private double amount = 0.0;
    public void setItems(List<CurrencyCbr> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
    public void setAmount(double amount) {
        this.amount = amount;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calculator_currency, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CurrencyCbr c = items.get(position);
        holder.tvCode.setText(c.charCode);
        int resId = holder.ivFlag.getContext().getResources()
                .getIdentifier(
                        "flag_" + c.charCode.toLowerCase(Locale.ROOT),
                        "drawable",
                        holder.ivFlag.getContext().getPackageName()
                );
        holder.ivFlag.setImageResource(resId != 0 ? resId : R.drawable.flag_placeholder);
        double raw = Double.parseDouble(c.value.replace(',', '.'));
        double ratePerUnit = raw / c.nominal;                  // <--- делим на nominal
        double result      = amount * ratePerUnit;
        holder.tvResult.setText(
                String.format(Locale.getDefault(), "%.2f ₽", result)
        );
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivFlag;
        TextView tvCode, tvResult;
        VH(@NonNull View itemView) {
            super(itemView);
            ivFlag   = itemView.findViewById(R.id.iv_flag);
            tvCode   = itemView.findViewById(R.id.tv_code);
            tvResult = itemView.findViewById(R.id.tv_result);
        }
    }
}
