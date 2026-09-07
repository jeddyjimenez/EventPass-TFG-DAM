package com.example.eventos_mobile;

import com.example.eventos_mobile.ServicioApi;

import java.util.*;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteRetrofit {
    // Url del proyecto en la nube
    private static final String URL_BASE = "https://proyectoeventpass-production.up.railway.app/";

    private static Retrofit retrofit;

    public static ServicioApi obtenerServicio() {
        if (retrofit == null) {
            android.util.Log.d("API_BASE", URL_BASE);
            // Guardamos las cookies de inicio de sesión
            CookieJar gestorCookies = new CookieJar() {
                private final Map<String, List<Cookie>> almacen = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    almacen.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = almacen.get(url.host());
                    return cookies != null ? cookies : new ArrayList<>();
                }
            };

            OkHttpClient clienteHttp = new OkHttpClient.Builder()
                    .cookieJar(gestorCookies)
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .client(clienteHttp)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        // Devuelve un objeto ServicioApi para hacer login, eventos, entradas, validar...
        return retrofit.create(ServicioApi.class);
    }
    public static String obtenerBaseUrl() {
        return URL_BASE;
    }

}
