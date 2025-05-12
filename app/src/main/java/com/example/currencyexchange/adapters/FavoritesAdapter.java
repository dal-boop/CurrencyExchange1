package com.example.currencyexchange.adapters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.currencyexchange.R;
import com.example.currencyexchange.models.CurrencyCbr;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.VH> {

    private final List<CurrencyCbr> items;
    private final Set<Integer> expandedPositions = new HashSet<>();
    private final Map<String, List<Entry>> historyCache  = new HashMap<>();
    private final Map<Integer,  List<String>> labelsCache   = new HashMap<>();

    public FavoritesAdapter(List<CurrencyCbr> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_currency, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CurrencyCbr c = items.get(position);
        holder.tvCode.setText(c.charCode);
        double raw = Double.parseDouble(c.value.replace(',', '.'));
        double ratePerUnit = raw / c.nominal;
        holder.tvValue.setText(
                String.format(Locale.getDefault(), "%.2f ₽", ratePerUnit)
        );
        int flagRes = holder.itemView.getContext().getResources()
                .getIdentifier(
                        "flag_" + c.charCode.toLowerCase(Locale.ROOT),
                        "drawable",
                        holder.itemView.getContext().getPackageName()
                );
        holder.ivFlag.setImageResource(flagRes != 0 ? flagRes : R.drawable.flag_placeholder);


        boolean isExpanded = expandedPositions.contains(position);
        holder.chartContainer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        if (isExpanded) {
            String key = c.id;
            if (historyCache.containsKey(key)) {
                drawChart(holder.chart,
                        historyCache.get(key),
                        labelsCache.get(position));
            } else {
                loadHistory(key, holder.chart, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivFlag;
        TextView  tvCode, tvValue;
        FrameLayout chartContainer;
        LineChart   chart;

        VH(@NonNull View itemView) {
            super(itemView);
            ivFlag         = itemView.findViewById(R.id.iv_flag);
            tvCode         = itemView.findViewById(R.id.tv_code);
            tvValue        = itemView.findViewById(R.id.tv_value);
            chartContainer = itemView.findViewById(R.id.chart_container);
            chart          = itemView.findViewById(R.id.chart);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (expandedPositions.contains(pos)) {
                    expandedPositions.remove(pos);
                } else {
                    expandedPositions.add(pos);
                }
                notifyItemChanged(pos);
            });
        }
    }

    private void loadHistory(String valNmRq, LineChart chart, int position) {
        Calendar end   = Calendar.getInstance();
        Calendar start = (Calendar) end.clone();
        start.add(Calendar.DAY_OF_MONTH, -6);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String d1 = df.format(start.getTime());
        String d2 = df.format(end.getTime());

        getDynamicApi().getHistory(d1, d2, valNmRq)
                .enqueue(new Callback<CbrDynamicResponse>() {
                    @Override
                    public void onResponse(Call<CbrDynamicResponse> call,
                                           Response<CbrDynamicResponse> res) {
                        if (!res.isSuccessful() || res.body() == null) return;
                        List<Entry> entries = new ArrayList<>();
                        List<String> labels = new ArrayList<>();
                        int idx = 0;
                        for (CbrDynamicResponse.Record r : res.body().records) {
                            float val = Float.parseFloat(r.value.replace(',', '.'));
                            entries.add(new Entry(idx, val));
                            labels.add(r.date);
                            idx++;
                        }
                        historyCache.put(valNmRq, entries);
                        labelsCache.put(position, labels);
                        drawChart(chart, entries, labels);
                    }
                    @Override public void onFailure(Call<CbrDynamicResponse> call, Throwable t) { }
                });
    }

    private void drawChart(LineChart chart, List<Entry> data, List<String> labels) {
        LineDataSet set = new LineDataSet(data, "");
        set.setDrawCircles(true);
        set.setCircleRadius(4f);
        set.setLineWidth(2f);
        int color = ContextCompat.getColor(chart.getContext(), R.color.blue);
        set.setColor(color);
        set.setCircleColor(color);

        chart.setData(new LineData(set));
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        // Умеренные внешние отступы: left, top, right, bottom
        chart.setExtraOffsets(8f, 4f, 8f, 24f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setLabelRotationAngle(-45f);

        // Сдвигаем границы, чтобы точки не прилипали к краю
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(data.size() - 1 + 0.5f);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis left = chart.getAxisLeft();
        left.setDrawGridLines(false);
        left.setTextSize(10f);

        chart.invalidate();
    }







    private CbrDynamicApi getDynamicApi() {
        return new Retrofit.Builder()
                .baseUrl("https://www.cbr.ru/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(CbrDynamicApi.class);
    }

    interface CbrDynamicApi {
        @GET("scripts/XML_dynamic.asp")
        Call<CbrDynamicResponse> getHistory(
                @Query("date_req1") String start,
                @Query("date_req2") String end,
                @Query("VAL_NM_RQ") String code
        );
    }

    @Root(name = "ValCurs", strict = false)
    public static class CbrDynamicResponse {
        @ElementList(inline = true, entry = "Record")
        public List<Record> records;

        @Root(name = "Record", strict = false)
        public static class Record {
            @Attribute(name = "Date") public String date;
            @Element(name = "Value")   public String value;
        }
    }
}
