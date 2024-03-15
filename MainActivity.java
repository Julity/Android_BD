// MainActivity.java
package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.db.ApiService;
import com.example.myapplication.db.MyDataModel;
import com.example.myapplication.db.MyDbManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MyDbManager myDbManager;
    private EditText idTitle, idDisc, idSearch;
    private TextView tvTest;
                         private ApiService apiService;
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDbManager = new MyDbManager(this);
        idTitle = findViewById(R.id.idTitle);
        idDisc = findViewById(R.id.idDisc);
        idSearch = findViewById(R.id.idSearch);
        tvTest = findViewById(R.id.tvTest);
        myDbManager.openDb();
        String channelId = "my_notification_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "My Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
                                // Инициализируем Retrofit и ApiService
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://your-server-url/") // Замените на URL вашего веб-сервера
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

        apiService = retrofit.create(ApiService.class);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.example.myapplication.REQUEST_NOTIFICATION_PERMISSION".equals(intent.getAction())) {
                    // Здесь запросите разрешение и выполните необходимые действия
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 123);
                }
            }
        };
        // Регистрация приемника для обновления tvTest
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.example.myapplication.UPDATE_TEXT_VIEW".equals(intent.getAction())) {
                    String text = intent.getStringExtra("text");
                    updateTextView(text);
                }
            }
        };
        registerReceiver(receiver, new IntentFilter("com.example.myapplication.UPDATE_TEXT_VIEW"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("com.example.myapplication.REQUEST_NOTIFICATION_PERMISSION"));

        updateUI();
    }

    public void onClickSave(View view) {
    //    myDbManager.insertToDb(idTitle.getText().toString(), idDisc.getText().toString());
                            // Создаем объект MyDataModel и отправляем на сервер
                            MyDataModel myData = new MyDataModel(idTitle.getText().toString(), idDisc.getText().toString());
                            sendDataToServer(myData);
        updateUI();

    }

                            private void sendDataToServer(MyDataModel data) {
                                Call<Void> call = apiService.addData(data);

                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        // Обработка успешного ответа от сервера
                                        updateUI(); // Обновляем UI после успешной отправки данных
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        // Обработка ошибки
                                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
    public void onClickImport(View view) {
        // Обработчик события нажатия кнопки "Импорт слов"
        startImportThread();
     //   new ImportDataTask().execute(); // Запускаем задачу в отдельном потоке
    }

    public void onClickDelete(View view) {
        String titleToDelete = idSearch.getText().toString();


       new AlertDialog.Builder(this)
                .setTitle("Подтверждение удаления")
                .setMessage("Вы уверены, что хотите удалить данные?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Если пользователь нажимает "Да", то удаляем данные из базы
                        myDbManager.deleteFromDb(titleToDelete);
                        updateUI();
                    }
                })
                .setNegativeButton("Отмена", null) // Если пользователь нажимает "Отмена", ничего не происходит
                .show();
    }

    public void onClickSearch(View view) {
        String keyword = idSearch.getText().toString();
        tvTest.setText("");
        for (String title : myDbManager.searchInDb(keyword)) {
            tvTest.append(title);
            tvTest.append("\n");
        }
    }
    public void onClickClearDatabase(View view) {
        // Создайте диалоговое окно для подтверждения
        new AlertDialog.Builder(this)
                .setTitle("Подтверждение очистки базы данных")
                .setMessage("Вы уверены, что хотите удалить все данные из базы данных? Это действие нельзя будет отменить.")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Если пользователь нажимает "Да", то очищаем базу данных
                        myDbManager.clearDatabase();
                        updateUI();
                    }
                })
                .setNegativeButton("Отмена", null) // Если пользователь нажимает "Отмена", ничего не происходит
                .show();
    }

    private void updateUI() {
        tvTest.setText("");
        for (String title : myDbManager.getFromDb()) {
            tvTest.append(title);
            tvTest.append("\n");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        myDbManager.closeDb();
    }
    /////////ИСПОРТ
    private void startImportThread() {
        // Показать Toast с сообщением о начале импорта
        Toast.makeText(this, "Импорт данных начался", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // В отдельном потоке выполняем импорт данных
                final List<String> importedWords = WebDataImporter.importWordsFromWebsite("https://studynow.ru/dicta/allwords");

                // Вернуться на главный поток для обновления UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Обновить пользовательский интерфейс и отобразить импортированные данные
                        StringBuilder wordListText = new StringBuilder();
                        for (String word : importedWords) {
                            // Определение индекса пробела после первых двух символов
                            int spaceIndex = word.indexOf(" ", 3);

                            // Проверка наличия пробела и наличия символов после него
                            if (spaceIndex != -1 && spaceIndex < word.length() - 1) {
                                // Получение слова для первой колонки (с 3 символа до пробела)
                                String firstColumn = word.substring(3, spaceIndex);

                                // Получение оставшейся части для второй колонки (после пробела)
                                String secondColumn = word.substring(spaceIndex + 1);

                                // Добавление данных в базу
                                myDbManager.insertToDb(firstColumn, secondColumn);

                                // Добавление данных в текстовое представление
                                wordListText.append(firstColumn).append(" | ").append(secondColumn).append("\n");
                            }
                        }
                        tvTest.setText(wordListText.toString());

                        // Показать Toast с сообщением об окончании импорта
                        Toast.makeText(MainActivity.this, "Импорт данных завершен", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
    public void onClickPlayMusic(View view) {
        Log.d("MainActivity", "onClickPlayMusic: Starting BackgroundMusicService");
        startService(new Intent(this, BackgroundMusicService.class));

    }
    public void updateTextView(String text) {
        tvTest.setText(text);
    }
    public void onClickImport2(View view) {
        Intent intent = new Intent(this, WordImportService.class);
        startService(intent);

    }

    public void onClickStopMusic(View view) {
        stopService(new Intent(this, BackgroundMusicService.class));
    }


}
