package ee.taltech.gamecollection.paranoia

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.IOException

class ParanoiaDbHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "paranoia.db"
        private const val DATABASE_VERSION = 6

        private const val TABLE_PARANOIA_QUESTIONS = "paranoia_questions"
        private const val COLUMN_PARANOIA_ID = "paranoia_id"
        private const val COLUMN_PARANOIA_QUESTION = "paranoia_question"
        private const val COLUMN_PARANOIA_ASKED = "paranoia_asked"

        private var CREATE_TABLE_PARANOIA_QUESTIONS = """
            CREATE TABLE $TABLE_PARANOIA_QUESTIONS (
                $COLUMN_PARANOIA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PARANOIA_QUESTION TEXT,
                $COLUMN_PARANOIA_ASKED INTEGER DEFAULT 0
            )
        """.trimIndent()
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        Log.d("DbHelper", "Database opened")
        return super.getWritableDatabase()
    }


    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DbHelper", "onCreate called!")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARANOIA_QUESTIONS")
        db.execSQL(CREATE_TABLE_PARANOIA_QUESTIONS)
        preloadParanoiaQuestions(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARANOIA_QUESTIONS")
        onCreate(db)
    }

    private fun preloadParanoiaQuestions(db: SQLiteDatabase) {
        val questions = mutableListOf<String>()
        try {
            context.assets.open("ParanoiaQuestions").bufferedReader().useLines { lines ->
                questions.addAll(lines)
            }
        } catch (e: IOException) {
            Log.d("DbHelper", "ruh ruh")
            e.printStackTrace()
        }

        for (question in questions) {
            Log.d("info", question)
            val values = ContentValues().apply {
                put(COLUMN_PARANOIA_QUESTION, question)
                put(COLUMN_PARANOIA_ASKED, 0)
            }
            db.insert(TABLE_PARANOIA_QUESTIONS, null, values)
        }
    }

    private fun markParanoiaQuestionAsAsked(id: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PARANOIA_ASKED, 1)
        }
        db.update(TABLE_PARANOIA_QUESTIONS, values, "$COLUMN_PARANOIA_ID = ?", arrayOf(id.toString()))
    }

    fun resetAskedQuestions() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PARANOIA_ASKED, 0)
        }
        db.update(TABLE_PARANOIA_QUESTIONS, values, null, null)
    }

    fun getRandomUnaskedParanoiaQuestion(): String? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PARANOIA_QUESTIONS,
            arrayOf(COLUMN_PARANOIA_ID, COLUMN_PARANOIA_QUESTION),
            "$COLUMN_PARANOIA_ASKED = 0",
            null, null, null, "RANDOM()", "1"
        )

        var question: String? = null
        var questionId: Int? = null
        if (cursor.moveToFirst()) {
            val questionIndex = cursor.getColumnIndex(COLUMN_PARANOIA_QUESTION)
            val idIndex = cursor.getColumnIndex(COLUMN_PARANOIA_ID)
            if (questionIndex != -1 && idIndex != -1) {
                question = cursor.getString(questionIndex)
                questionId = cursor.getInt(idIndex)
            }
        }
        cursor.close()

        questionId?.let { markParanoiaQuestionAsAsked(it) }
        return question
    }
}
