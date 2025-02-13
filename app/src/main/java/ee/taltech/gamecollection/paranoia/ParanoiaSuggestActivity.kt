package ee.taltech.gamecollection.paranoia

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ee.taltech.gamecollection.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ParanoiaSuggestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paranoia_suggest)

        val inputQuestion = findViewById<EditText>(R.id.editTextTextQuestion)
        val submitButton = findViewById<Button>(R.id.buttonSubmitQuestion)

        copyQuestionsFileIfNeeded()

        submitButton.setOnClickListener {
            val question = inputQuestion.text.toString().trim()

            if (question.isNotEmpty()) {
                try {
                    val fileOutputStream = FileOutputStream(File(filesDir, "ParanoiaQuestions"), true)
                    fileOutputStream.write(("$question\n").toByteArray())
                    fileOutputStream.close()

                    val dbHelper = ParanoiaDbHelper(this)
                    //dbHelper.addNewQuestion(question)

                    Toast.makeText(this, "Question added locally!", Toast.LENGTH_SHORT).show()
                    inputQuestion.text.clear()
                } catch (e: Exception) {
                    Log.e("ParanoiaSuggest", "Error writing question: ${e.message}")
                    Toast.makeText(this, "Failed to add question!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter a question!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copyQuestionsFileIfNeeded() {
        val fileName = "ParanoiaQuestions"
        val file = File(filesDir, fileName)

        if (!file.exists()) {
            try {
                assets.open(fileName).use { inputStream ->
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Log.d("ParanoiaSuggest", "Questions file copied to internal storage.")
            } catch (e: IOException) {
                Log.e("ParanoiaSuggest", "Error copying file: ${e.message}")
            }
        }
    }

}