package com.furkanbarissonmezisik.memorizewords.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.furkanbarissonmezisik.memorizewords.MainActivity
import com.furkanbarissonmezisik.memorizewords.R
import com.furkanbarissonmezisik.memorizewords.ui.theme.AppLanguage
import java.util.*

class NotificationManager(private val context: Context) {
    
    companion object {
        const val CHANNEL_ID = "daily_reminder_channel"
        const val NOTIFICATION_ID = 1001
        const val REQUEST_CODE = 2001
        
        // Fun emojis for notifications
        private val notificationEmojis = listOf(
            "📚", "🎯", "💪", "🔥", "📖", "📝", "🌟", "🧠", "🚀", "⭐",
            "🎓", "💡", "🎉", "🏆", "🎊", "💎", "🌈", "🎨", "🎵", "🎪",
            "🎭", "🎪", "🎨", "🎬", "🎮", "🎲", "🎯", "🎪", "🎨", "🎵",
            "🍀", "🌺", "🌻", "🌷", "🌸", "🌼", "🌹", "🌿", "🍃", "🌱",
            "⚡", "✨", "💫", "🌟", "⭐", "🌙", "☀️", "🌞", "🌝", "🌛"
        )
        
        // Random notification icons
        private val notificationIcons = listOf(
            R.drawable.ic_notification_book,
            R.drawable.ic_notification_brain,
            R.drawable.ic_notification_star,
            R.drawable.ic_notification_rocket,
            R.drawable.ic_notification_lightbulb
        )
        
        // Notification messages in different languages (without emojis)
        private val notificationMessages = mapOf(
            AppLanguage.ENGLISH to listOf(
                "Don't forget to memorize your words today!",
                "Time for your daily vocabulary practice!",
                "Your words are waiting for you!",
                "Keep your learning streak alive!",
                "Ready to expand your vocabulary?",
                "Today's words are calling your name!",
                "Let's make today a learning day!",
                "Your brain will thank you for this!",
                "Small steps, big progress!",
                "Consistency is the key to success!"
            ),
            AppLanguage.TURKISH to listOf(
                "Bugünkü kelimelerini ezberlemeyi unutma!",
                "Günlük kelime pratiği zamanı!",
                "Kelimelerin seni bekliyor!",
                "Öğrenme serini canlı tut!",
                "Kelime dağarcığını genişletmeye hazır mısın?",
                "Bugünkü kelimeler seni çağırıyor!",
                "Bugünü öğrenme günü yapalım!",
                "Beynin bunun için sana teşekkür edecek!",
                "Küçük adımlar, büyük ilerleme!",
                "Tutarlılık başarının anahtarı!"
            ),
            AppLanguage.INDONESIAN to listOf(
                "Jangan lupa menghafal kata-kata hari ini!",
                "Waktunya latihan kosakata harian!",
                "Kata-katamu menunggumu!",
                "Jaga streak belajarmu tetap hidup!",
                "Siap memperluas kosakatamu?",
                "Kata-kata hari ini memanggil namamu!",
                "Mari jadikan hari ini hari belajar!",
                "Otakmu akan berterima kasih untuk ini!",
                "Langkah kecil, kemajuan besar!",
                "Konsistensi adalah kunci kesuksesan!"
            ),
            AppLanguage.CHINESE to listOf(
                "别忘了今天背单词！",
                "每日词汇练习时间到了！",
                "你的单词在等你！",
                "保持学习连胜！",
                "准备好扩展词汇量了吗？",
                "今天的单词在呼唤你！",
                "让我们把今天变成学习日！",
                "你的大脑会为此感谢你！",
                "小步骤，大进步！",
                "坚持是成功的关键！"
            ),
            AppLanguage.SPANISH to listOf(
                "¡No olvides memorizar tus palabras de hoy!",
                "¡Hora de tu práctica diaria de vocabulario!",
                "¡Tus palabras te están esperando!",
                "¡Mantén viva tu racha de aprendizaje!",
                "¿Listo para expandir tu vocabulario?",
                "¡Las palabras de hoy te están llamando!",
                "¡Hagamos de hoy un día de aprendizaje!",
                "¡Tu cerebro te lo agradecerá!",
                "¡Pasos pequeños, gran progreso!",
                "¡La consistencia es la clave del éxito!"
            ),
            AppLanguage.ARABIC to listOf(
                "لا تنس حفظ كلماتك اليوم!",
                "حان وقت ممارسة المفردات اليومية!",
                "كلماتك في انتظارك!",
                "حافظ على سلسلة التعلم حية!",
                "مستعد لتوسيع مفرداتك؟",
                "كلمات اليوم تناديك!",
                "لنجعل اليوم يوم تعلم!",
                "دماغك سيشكرك على هذا!",
                "خطوات صغيرة، تقدم كبير!",
                "الثبات هو مفتاح النجاح!"
            ),
            AppLanguage.HINDI to listOf(
                "आज अपने शब्दों को याद करना मत भूलें!",
                "दैनिक शब्दावली अभ्यास का समय!",
                "आपके शब्द आपका इंतजार कर रहे हैं!",
                "अपनी सीखने की लकीर को जीवित रखें!",
                "अपनी शब्दावली बढ़ाने के लिए तैयार हैं?",
                "आज के शब्द आपको बुला रहे हैं!",
                "आज को सीखने का दिन बनाते हैं!",
                "आपका दिमाग इसके लिए आपको धन्यवाद देगा!",
                "छोटे कदम, बड़ी प्रगति!",
                "निरंतरता सफलता की कुंजी है!"
            ),
            AppLanguage.PORTUGUESE to listOf(
                "Não esqueça de memorizar suas palavras hoje!",
                "Hora da sua prática diária de vocabulário!",
                "Suas palavras estão esperando por você!",
                "Mantenha sua sequência de aprendizado viva!",
                "Pronto para expandir seu vocabulário?",
                "As palavras de hoje estão chamando seu nome!",
                "Vamos fazer de hoje um dia de aprendizado!",
                "Seu cérebro vai te agradecer por isso!",
                "Pequenos passos, grande progresso!",
                "Consistência é a chave do sucesso!"
            ),
            AppLanguage.FRENCH to listOf(
                "N'oubliez pas de mémoriser vos mots aujourd'hui!",
                "C'est l'heure de votre pratique quotidienne de vocabulaire!",
                "Vos mots vous attendent!",
                "Gardez votre série d'apprentissage vivante!",
                "Prêt à élargir votre vocabulaire?",
                "Les mots d'aujourd'hui vous appellent!",
                "Faisons d'aujourd'hui un jour d'apprentissage!",
                "Votre cerveau vous remerciera pour cela!",
                "Petits pas, grand progrès!",
                "La constance est la clé du succès!"
            ),
            AppLanguage.RUSSIAN to listOf(
                "Не забудьте выучить сегодняшние слова!",
                "Время для ежедневной практики словаря!",
                "Ваши слова ждут вас!",
                "Поддерживайте свою серию обучения!",
                "Готовы расширить свой словарный запас?",
                "Сегодняшние слова зовут вас!",
                "Давайте сделаем сегодня днем обучения!",
                "Ваш мозг поблагодарит вас за это!",
                "Маленькие шаги, большой прогресс!",
                "Постоянство - ключ к успеху!"
            ),
            AppLanguage.BENGALI to listOf(
                "আজ আপনার শব্দগুলি মুখস্থ করতে ভুলবেন না!",
                "দৈনিক শব্দভান্ডার অনুশীলনের সময়!",
                "আপনার শব্দগুলি আপনার জন্য অপেক্ষা করছে!",
                "আপনার শেখার স্ট্রিক জীবিত রাখুন!",
                "আপনার শব্দভান্ডার প্রসারিত করতে প্রস্তুত?",
                "আজকের শব্দগুলি আপনাকে ডাকছে!",
                "আজকে শেখার দিন করি!",
                "আপনার মস্তিষ্ক এটির জন্য আপনাকে ধন্যবাদ দেবে!",
                "ছোট পদক্ষেপ, বড় অগ্রগতি!",
                "ধারাবাহিকতা সাফল্যের চাবিকাঠি!"
            )
        )
    }
    
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily reminders to practice vocabulary"
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun scheduleDailyNotification(hour: Int = 9, minute: Int = 0) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // If the time has already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        
        // Cancel any existing alarm first
        alarmManager.cancel(pendingIntent)
        
        // Schedule the new alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }
    
    fun cancelDailyNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
    
    fun showNotification(language: AppLanguage) {
        val messages = notificationMessages[language] ?: notificationMessages[AppLanguage.ENGLISH]!!
        val randomMessage = messages.random()
        val randomEmoji = notificationEmojis.random()
        val randomIcon = notificationIcons.random()
        val messageWithEmoji = "$randomEmoji $randomMessage"
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(randomIcon)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(messageWithEmoji)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }
    
    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                // Get the current language from SharedPreferences
                val prefs = context.getSharedPreferences("language_preferences", Context.MODE_PRIVATE)
                val storedLanguageCode = prefs.getString("app_language", AppLanguage.ENGLISH.code)
                val language = AppLanguage.values().find { it.code == storedLanguageCode } ?: AppLanguage.ENGLISH
                
                val notificationManager = NotificationManager(context)
                notificationManager.createNotificationChannel()
                notificationManager.showNotification(language)
                
                // Schedule the next notification
                val notificationSettings = NotificationSettingsManager(context)
                if (notificationSettings.isEnabled) {
                    notificationManager.scheduleDailyNotification(
                        notificationSettings.reminderHour,
                        notificationSettings.reminderMinute
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

