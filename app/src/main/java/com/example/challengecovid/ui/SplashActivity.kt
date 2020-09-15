package com.example.challengecovid.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View.ALPHA
import android.view.View.TRANSLATION_Y
import androidx.appcompat.app.AppCompatActivity
import com.example.challengecovid.Data
import com.example.challengecovid.R
import com.example.challengecovid.RepositoryController
import com.example.challengecovid.Utils
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import timber.log.Timber


class SplashActivity : AppCompatActivity() {

    private lateinit var job: Job
    private var firstRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timber.tag("FIREBASE").d("in onCreate in splash activity")

        // get a random message to show the user in the splash screen
        val text: String = splashMessages.random()
        splash_text.text = text

        animateSplashScreen()

        firstRun = Utils.checkFirstRun(this@SplashActivity)

        // if this is the first start prepopulate the firestore db
        if (firstRun) initDatabase()

        //handleIncomingCloudMessages()

        showSplashScreen()
    }

    private fun animateSplashScreen() {
        // set some nice animations
        val objectAnimator1: ObjectAnimator = ObjectAnimator.ofFloat(splash_title, TRANSLATION_Y, 100f)
        val objectAnimator2: ObjectAnimator = ObjectAnimator.ofFloat(splash_logo, ALPHA, 0F, 1F)
        val objectAnimator3: ObjectAnimator = ObjectAnimator.ofFloat(splash_text, TRANSLATION_Y, -100f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3)
        animatorSet.duration = 600
        animatorSet.start()
    }

    private fun initDatabase() {
        val categoryRepo = RepositoryController.getCategoryRepository()
        val challengeRepo = RepositoryController.getChallengeRepository()

        categoryRepo.saveMultipleCategories(Data.getChallengeCategories())
        challengeRepo.saveMultipleChallenges(Data.getDailyChallenges())
    }

    /*
    private fun handleIncomingCloudMessages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }

        // Handle possible data accompanying notification message.
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Timber.tag("FIREBASE_CLOUD_MESSAGE").d("Key: $key Value: $value")
            }
        }
    }
    */

    private fun showSplashScreen() {
        job = CoroutineScope(Dispatchers.Default).launch {
            // show the splash screen for 1 1/2 seconds
            delay(1500)

            if (firstRun) {
                startCharacterCreation()
            } else {
                startMain()
            }
        }
    }

    private fun startCharacterCreation() {
        val intent = Intent(this@SplashActivity, CharacterCreationActivity::class.java)
        startActivity(intent)

        // close this activity so the user can't navigate back to it!
        finish()
    }

    // navigate direct to Main Activity
    private fun startMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()    // cleanup coroutineScope
    }

    companion object {
        const val PREFS_VERSION_CODE_KEY = "version_code"

        // motivating messages that are randomly shown in the splash screen
        val splashMessages = arrayOf(
            "Manche Leute wollen, dass es passiert, manche wünschen, es würde passieren, andere sorgen dafür, dass es passiert. ~ Michael Jordan",
            "Wer einen Misserfolg nur als kleinen Umweg betrachtet, verliert nie sein Ziel aus den Augen. ~ Martin Luther",
            "Entweder werden wir einen Weg finden oder wir machen einen! ~ Hannibal",
            "Das Geheimnis zum Erfolg ist anzufangen ~ Mark Twain",
            "Erfolg hat drei Buchstaben: TUN ~ Johann Wolfang von Goethe",
            "Unser Schicksal hängt nicht von den Sternen ab, sondern von unserem Handeln. ~ William Shakespeare",
            "Alle Träume können wahr werden, wenn wir den Mut haben, ihnen zu folgen. ~ Walt Disney",
            "Wege entstehen dadurch, dass man sie geht. ~ Franz Kafka",
            "Wir sind das, was wir wiederholt tun. Vorzüglichkeit ist daher keine Handlung, sondern eine Gewohnheit. ~ Aristoteles",
            "Hindernisse können mich nicht aufhalten; Entschlossenheit bringt jedes Hindernis zu Fall. ~ Leonardo da Vinci",
            "Das gute Gelingen ist zwar nichts Kleines, fängt aber mit Kleinigkeiten an. ~ Sokrates",
            "Ist unsere Motivation stark und heilsam, können wir alles vollbringen. ~ Dalai Lama",
            "Ein Mensch, der keine Fehler gemacht hat, hat nie etwas Neues ausprobiert. ~ Albert Einstein",
            "Es macht nichts, wenn es langsam vorangeht. Hauptsache du bleibst nicht stehen. ~ Konfuzius",
            "Erfolg ist die Fähigkeit, von einem Misserfolg zum anderen zu gehen, ohne seine Begeisterung zu verlieren. ~ Winston Churchill",
            "Was vorstellbar ist, ist auch machbar. ~ Albert Einstein",
            "Es gibt mehr Menschen, die zu früh aufgeben als solche, die scheitern. ~ Henry Ford",
            "Wer sich um das Morgen am wenigsten kümmert, geht ihm mit der größten Lust entgegen. ~ Epikur",
            "Der höchste Genuss besteht in der Zufriedenheit mit sich selbst. ~ Jean-Jacques Rousseau",
            "Ein Misserfolg ist lediglich die Möglichkeit, schlauer von Neuem zu beginnen. ~ Henry Ford",
            "Ich kenne keinen sicheren Weg zum Erfolg, aber einen sicheren Weg zum Misserfolg: Es allen Recht machen zu wollen. ~ Platon",
            "Es ist nicht genug zu wissen – man muss auch anwenden. Es ist nicht genug zu wollen – man muss auch tun. ~ Johann Wolfang von Goethe",
            "Die Kunst ist, einmal mehr aufzustehen, als man umgeworfen wird. ~ Winston Churchill",
            "Wer einen Fehler gemacht hat und ihn nicht korrigiert, begeht einen zweiten. ~ Konfuzius",
            "Hindernisse und Schwierigkeiten sind Stufen, auf denen wir in die Höhe steigen. ~ Friedrich Nietzsche",
            "Ob Du denkst, Du kannst es, oder Du kannst es nicht – in beiden Fällen hast Du Recht. ~ Henry Ford",
            "Erfolg ist die Belohnung für schwere Arbeit. ~ Sophokles",
            "Glaube daran, DASS du es schaffst und du hast schon den halben Weg gemeistert. ~ Theodore Roosevelt",
            "Fange nie an, aufzuhören. Höre nie auf, anzufangen. ~ Cicero"
            )
    }
}