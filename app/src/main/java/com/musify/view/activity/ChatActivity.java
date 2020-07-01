package com.musify.view.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.musify.R;
import com.musify.model.MusifyAPIRequestQueue;
import com.musify.model.message.Message;
import com.musify.view.message.MessageAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Clase encargada de cargar la vista del Chat, y a su vez de enviar los mensajes del usuario al servidor
 */
public class ChatActivity extends AppCompatActivity {
    private String user;
    private MusifyAPIRequestQueue requests;
    private MessageAdapter messageAdapter;
    private RecyclerView messageView;

    /**
     * En este método se cargará la vista, y se inicializará el evento click del usuario.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user = getIntent().getStringExtra("username");
        requests = MusifyAPIRequestQueue.getInstance(this);
        FloatingActionButton sendMessageButton = findViewById(R.id.send_message_button);
        EditText userMessageInput = findViewById(R.id.user_message_input);
        TextView titulo = findViewById(R.id.chatbot_title);
        messageView = findViewById(R.id.messages_view);
        messageAdapter = new MessageAdapter(this);
        messageView.setAdapter(messageAdapter);

        // Inicializamos las acciones dentro del click
        sendMessageButton.setOnClickListener((view) -> {
            // Si el usuario no ha escrito en el chat, no se hará nada
            if (!userMessageInput.getText().toString().isEmpty()){
                messageAdapter.add(new Message(userMessageInput.getText().toString(), true));
                messageView.scrollToPosition(messageAdapter.getItemCount() - 1);
                // Preparamos el JSON de la petición para pedir canciones
                JSONObject requestSongBody = new JSONObject();
                try {
                    requestSongBody.put("user_input", userMessageInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Creamos la petición para pedir recomendaciones, se crea antes que la de pedir canciones
                // debido a que la de crear canciones la necesita creada para poder crear la suya
                JsonObjectRequest requestRecommendationsRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.api_host) + "/" + user + "/request_recommendations", null, response ->
                {
                    // En el caso de que la petición haya dado OK, se procede a preparar el mensaje del chatbot
                    // con sus recomendaciones
                    try {
                        JSONArray songs = response.getJSONArray("songs");
                        String chatbotResponse = "Estas son las canciones que te recomiendo:";
                        for (int i = 0; i < songs.length(); i++) {
                            chatbotResponse += "\n -" + songs.getJSONObject(i).getString("song") + " del artista " + songs.getJSONObject(i).getString("artist");
                        }
                        messageAdapter.add(new Message(chatbotResponse, false));
                        messageView.scrollToPosition(messageAdapter.getItemCount() - 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    titulo.setText(R.string.chatbot);
                }, error ->
                {
                    // En el caso de que el servidor haya detectado fallas o que no hayan recomendaciones
                    // se despliega un Toast con el error
                    if (error.networkResponse.statusCode == 400) {
                        String errorMessage = null;
                        try {
                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast loginError = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        loginError.show();
                    }
                    titulo.setText(R.string.chatbot);
                });

                // Creamos la petición de pedir canciones
                JsonObjectRequest requestSongsRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_host) + "/" + user + "/request_songs", requestSongBody, response ->
                {
                    // En el caso de que el servidor haya generado una respuesta al usuario
                    // se procede a añadir la respuesta a la vista
                    try {
                        messageAdapter.add(new Message(response.getString("response"), false));
                        messageView.scrollToPosition(messageAdapter.getItemCount() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    titulo.setText(R.string.chatbot);
                    try {
                        // Si la acción del chatbot es la de haber recogido la búsqueda del usuario
                        // se añade la petición para pedir recomendaciones
                        if (response.getString("intent").equals("Recoger canción")
                                || response.getString("intent").equals("Recoger artista")
                                || response.getString("intent").equals("Recoger album")
                                || response.getString("intent").equals("Recoger cancion v2")
                                || response.getString("intent").equals("Recoger artista v2")
                                || response.getString("intent").equals("Recoger album v2")) {
                            titulo.setText(getString(R.string.chatbot) + " está contestando");
                            requestRecommendationsRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            requests.addToRequestQueue(requestRecommendationsRequest);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error ->
                {
                    // En el caso de que el chatbot haya tenido problemas a la hora de encontrar canciones
                    // se despliega su mensaje de error en un toast
                    if (error.networkResponse.statusCode == 400) {
                        String errorMessage = null;
                        try {
                            errorMessage = new JSONObject(new String(error.networkResponse.data)).getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast loginError = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        loginError.show();
                    }
                    titulo.setText(R.string.chatbot);
                });
                titulo.setText(getString(R.string.chatbot) + " está contestando");
                requestSongsRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requests.addToRequestQueue(requestSongsRequest);
                userMessageInput.setText("");
            }
        });
    }
}
