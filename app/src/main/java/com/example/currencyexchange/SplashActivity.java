package com.example.currencyexchange;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.currencyexchange.models.TinkoffResponse;
import com.example.currencyexchange.network.CbrApi;
import com.example.currencyexchange.network.CbrApiClient;
import com.example.currencyexchange.network.CbrResponse;
import com.example.currencyexchange.network.TinkoffApi;
import com.example.currencyexchange.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private boolean cbrLoaded  = false;
    private boolean tinkoffLoaded = false;
    private AnimatorSet pulseAnim;
    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoImage = findViewById(R.id.logoImage);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logoImage, "scaleX", 1f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logoImage, "scaleY", 1f, 1.1f);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        pulseAnim = new AnimatorSet();
        pulseAnim.playTogether(scaleX, scaleY);
        pulseAnim.setInterpolator(new LinearInterpolator());
        pulseAnim.start();
        loadCbrRates();
        loadTinkoffRates();

    }

    private void loadCbrRates() {
        CbrApi api = CbrApiClient.getRetrofit().create(CbrApi.class);
        api.getDailyRates().enqueue(new Callback<CbrResponse>() {
            @Override public void onResponse(Call<CbrResponse> call, Response<CbrResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    AppData.cbrRates = resp.body().valutes;
                }
                cbrLoaded = true;
                maybeGoMain();
            }
            @Override public void onFailure(Call<CbrResponse> call, Throwable t) {
                Log.e("Splash", "CBR load failed", t);
                cbrLoaded = true;
                maybeGoMain();
            }
        });
    }

    private void loadTinkoffRates() {
        TinkoffApi api = ApiClient.getRetrofit().create(TinkoffApi.class);
        api.getRates().enqueue(new Callback<TinkoffResponse>() {
            @Override public void onResponse(Call<TinkoffResponse> call, Response<TinkoffResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null && resp.body().payload != null) {
                    AppData.tinkoffRates = resp.body().payload.rates;
                }
                tinkoffLoaded = true;
                maybeGoMain();
            }
            @Override public void onFailure(Call<TinkoffResponse> call, Throwable t) {
                Log.e("Splash", "Tinkoff load failed", t);
                tinkoffLoaded = true;
                maybeGoMain();
            }
        });
    }

    private void maybeGoMain() {
        if (cbrLoaded && tinkoffLoaded) {
            if (pulseAnim.isRunning()) pulseAnim.cancel();
            if (AppData.cbrRates == null && AppData.tinkoffRates == null) {
                Toast.makeText(this, "Не удалось загрузить курсы", Toast.LENGTH_LONG).show();
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pulseAnim != null && pulseAnim.isRunning()) {
            pulseAnim.cancel();
        }
    }
}
