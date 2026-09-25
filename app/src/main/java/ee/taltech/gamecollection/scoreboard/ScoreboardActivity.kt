package ee.taltech.gamecollection.scoreboard

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ee.taltech.gamecollection.R
import org.json.JSONArray
import org.json.JSONObject

class ScoreboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter

    private val players = mutableListOf<Player>()
    private val historyEntries = mutableListOf<HistoryEntry>()

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyHeader: TextView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyPanel: View

    private var historyOpen = false

    private val prefsName = "scoreboard_prefs"
    private val playersKey = "players"
    private val historyKey = "history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_board)

        loadPlayers()
        loadHistory()

        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PlayerAdapter(
            players,
            onDataChanged = { savePlayers() },
            onScoreChanged = { name, delta, total ->
                val sign = if (delta >= 0) "+" else ""
                addHistoryEntry("$name $sign$delta total: $total")
            }
        )
        recyclerView.adapter = adapter

        val bounceAnimation =
            AnimationUtils.loadAnimation(this, R.anim.bounce)

        val buttonNewGame: Button = findViewById(R.id.buttonNewGame)
        buttonNewGame.setOnClickListener {
            it.startAnimation(bounceAnimation)
            showNewGameDialog()
        }

        val buttonBack: ImageButton = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

        val addButton: Button = findViewById(R.id.buttonAddPlayer)
        addButton.setOnClickListener {
            addPlayer()
        }

        historyPanel = findViewById(R.id.historyPanel)
        historyHeader = findViewById(R.id.historyHeader)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(historyEntries)
        historyRecyclerView.adapter = historyAdapter

        historyHeader.setOnClickListener {
            setHistoryOpen(!historyOpen)
        }

        setHistoryOpen(false)

        val scoreboard = findViewById<AppCompatTextView>(R.id.ScoreboardTextView)

        scoreboard.setOnClickListener {
            val language = resources.configuration.locales[0].language
            if (language == "et") {
                scoreboard.setText(R.string.scoreboard_alt)
            }
        }
    }

    private fun setHistoryOpen(open: Boolean) {
        historyOpen = open

        historyHeader.text = getString(
            if (open) R.string.history_up else R.string.history_down
        )

        historyPanel.visibility =
            if (open) View.VISIBLE else View.GONE

        if (open) {
            historyRecyclerView.post {
                scrollHistoryToBottom()
            }
        }
    }

    private fun scrollHistoryToBottom() {
        if (historyEntries.isEmpty()) return

        historyRecyclerView.scrollToPosition(historyEntries.lastIndex)
    }

    private fun showNewGameDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_new_game)

        dialog.window?.setBackgroundDrawableResource(
            R.drawable.dialog_background
        )

        val buttonNewGame: Button =
            dialog.findViewById(R.id.buttonNewGame)

        val buttonSamePlayers: Button =
            dialog.findViewById(R.id.buttonSamePlayers)

        val buttonCancel: Button =
            dialog.findViewById(R.id.buttonCancel)

        buttonNewGame.setOnClickListener {
            newGameFully()
            dialog.dismiss()
        }

        buttonSamePlayers.setOnClickListener {
            newGameSamePlayers()
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun newGameFully() {
        getSharedPreferences(prefsName, MODE_PRIVATE)
            .edit()
            .remove(playersKey)
            .remove(historyKey)
            .apply()

        players.clear()
        adapter.notifyDataSetChanged()

        historyEntries.clear()
        historyAdapter.notifyDataSetChanged()
    }

    private fun newGameSamePlayers() {
        players.forEach { player ->
            player.score = 0
        }

        adapter.notifyDataSetChanged()

        historyEntries.clear()
        historyAdapter.notifyDataSetChanged()

        savePlayers()
        saveHistory()
    }

    private fun addHistoryEntry(text: String) {
        val index = historyEntries.size
        historyEntries.add(HistoryEntry(text))
        historyAdapter.notifyItemInserted(index)

        saveHistory()

        if (historyOpen) {
            historyRecyclerView.post {
                scrollHistoryToBottom()
            }
        }
    }

    private fun addPlayer() {
        val editText = EditText(this)
        editText.hint = getString(R.string.enter_player_name)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.new_player))
            .setView(editText)
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                val name = editText.text.toString().ifEmpty {
                    "Player ${players.size + 1}"
                }

                players.add(Player(name))
                adapter.notifyItemInserted(players.lastIndex)
                savePlayers()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun savePlayers() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val jsonArray = JSONArray()

        players.forEach { player ->
            val obj = JSONObject()
            obj.put("name", player.name)
            obj.put("score", player.score)
            jsonArray.put(obj)
        }

        prefs.edit()
            .putString(playersKey, jsonArray.toString())
            .apply()
    }

    private fun loadPlayers() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val jsonString = prefs.getString(playersKey, "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)

        players.clear()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            players.add(
                Player(
                    name = obj.optString("name", "Player ${i + 1}"),
                    score = obj.optInt("score", 0)
                )
            )
        }
    }

    private fun saveHistory() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val jsonArray = JSONArray()

        historyEntries.forEach { entry ->
            val obj = JSONObject()
            obj.put("text", entry.text)
            jsonArray.put(obj)
        }

        prefs.edit()
            .putString(historyKey, jsonArray.toString())
            .apply()
    }

    private fun loadHistory() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val jsonString = prefs.getString(historyKey, "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)

        historyEntries.clear()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            historyEntries.add(
                HistoryEntry(text = obj.optString("text", ""))
            )
        }
    }
}