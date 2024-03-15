package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.db.MyDbManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordImportService extends IntentService {

    public WordImportService() {
        super("WordImportService");
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onHandleIntent(Intent intent) {
        // Запуск AsyncTask для выполнения сетевого запроса в фоновом режиме
        new ImportWordsTask().execute();
    }

    private class ImportWordsTask extends AsyncTask<Void, String, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            // Логика импорта слов с сайта
            String url = "https://studynow.ru/dicta/allwords";
            List<String> importedWords = WebDataImporter.importWordsFromWebsite("https://studynow.ru/dicta/allwords");
            List<String> wordsList = new ArrayList<>();
            try {
                Document document = Jsoup.connect(url).get();
                Elements wordElements = document.select("table#wordlist tr");

                for (Element wordElement : wordElements) {
                    String word = wordElement.text();
                    wordsList.add(word);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Имитация вывода информации в течение импорта (замените на реальный код)
            for (int i = 0; i < 5; i++) {
                publishProgress("Прогресс: " + (i + 1) * 20 + "%");
                try {
                    Thread.sleep(1000); // имитация длительной операции
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return wordsList;
        }
MyDbManager MM;
        @Override
        protected void onProgressUpdate(String... values) {
            // Вызывается в главном потоке при вызове publishProgress в doInBackground
            Log.d("ImportProgress", values[0]);
            // Здесь вы можете обновить UI с информацией о прогрессе (например, через broadcast)
        }

        @Override
        protected void onPostExecute(List<String> importedWords) {
            // Вызывается после завершения сетевого запроса
      //      MM.insertToDb("g","f");
            // Отправка уведомления
            sendNotification("Import completed", "Words imported successfully!");
            // Обновление TextView
            StringBuilder wordListText = new StringBuilder();
            for (String word : importedWords) {
                // Добавление каждого слова к StringBuilder
                wordListText.append(word).append("\n");
            }

            // Получение активности и обновление TextView
            Intent updateIntent = new Intent("com.example.myapplication.UPDATE_TEXT_VIEW");
            updateIntent.putExtra("text", wordListText.toString());
            sendBroadcast(updateIntent);
        }
    }
    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_notification_channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// Проверяем разрешение на отправку уведомлений
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Разрешение предоставлено, выполняем отправку уведомления
            notificationManager.notify(0, builder.build());
        } else {
            // Разрешение не предоставлено, обработайте этот случай
            Intent permissionIntent = new Intent("com.example.myapplication.REQUEST_NOTIFICATION_PERMISSION");
            sendBroadcast(permissionIntent);
        }

    }
}
