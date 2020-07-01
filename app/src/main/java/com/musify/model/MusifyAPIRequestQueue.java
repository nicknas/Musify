package com.musify.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Clase encargada de guardar una cola con todas las peticiones REST que realiza la app contra el servidor
 */
public class MusifyAPIRequestQueue {
    private static MusifyAPIRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    /**
     * Constructor de la cola de peticiones
     * @param ctx el contexto de la actividad donde se invoque
     */
    private MusifyAPIRequestQueue(Context ctx){
        this.ctx = ctx;
        requestQueue = getRequestQueue();
    }

    /**
     * Método para obtener una instancia singleton de esta clase
     * @param context el contexto de la actividad donde se invoque
     * @return la instancia guardada, si estaba creada, se devuelve la misma instancia. De lo contrario crea la instancia
     */
    public static synchronized MusifyAPIRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new MusifyAPIRequestQueue(context);
        }
        return instance;
    }

    /**
     * Método para obtener una instancia singleton de la cola de peticiones
     * @return la instancia guardada, si estaba creada, se devuelve la misma instancia. De lo contrario crea la instancia
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Guarda en la cola de peticiones, una nueva petición. La cola ejecutará la petición cuando ésta quede la primera en la cola
     * @param req la petición a guardar
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
