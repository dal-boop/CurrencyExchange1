package com.example.currencyexchange.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.currencyexchange.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectCurrencyAdapter
        extends RecyclerView.Adapter<SelectCurrencyAdapter.VH> {

    public static class CurrencyOption {
        public final String code;
        public boolean isSelected;
        public CurrencyOption(String code, boolean sel) {
            this.code = code;
            this.isSelected = sel;
        }
    }

    private final List<CurrencyOption> options;
    public SelectCurrencyAdapter(List<CurrencyOption> opts) {
        options = opts;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_currency, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int pos) {
        CurrencyOption opt = options.get(pos);
        holder.tvCode.setText(opt.code);
        holder.cb.setChecked(opt.isSelected);

        String resName = "flag_" + opt.code.toLowerCase(Locale.ROOT);
        int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(resName, "drawable",
                        holder.itemView.getContext().getPackageName());
        if (resId != 0) holder.ivFlag.setImageResource(resId);
        else holder.ivFlag.setImageResource(R.drawable.flag_placeholder);

        holder.cb.setOnCheckedChangeListener((buttonView, checked) -> {
            opt.isSelected = checked;
        });
    }

    @Override public int getItemCount() { return options.size(); }

    public List<String> getSelectedCodes() {
        List<String> sel = new ArrayList<>();
        for (CurrencyOption o : options) {
            if (o.isSelected) sel.add(o.code);
        }
        return sel;
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivFlag;
        TextView tvCode;
        CheckBox cb;
        VH(@NonNull View v) {
            super(v);
            ivFlag = v.findViewById(R.id.iv_flag);
            tvCode = v.findViewById(R.id.tv_code);
            cb     = v.findViewById(R.id.cb_select);
        }
    }
}
