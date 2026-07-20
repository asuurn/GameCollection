package ee.taltech.gamecollection.cardgames.CroatianPick

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ee.taltech.gamecollection.R
import ee.taltech.gamecollection.cardgames.CardGamesActivity
import org.json.JSONArray
import org.json.JSONObject

class CroatianPick : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter
    private val players = mutableListOf<Player>()
    private val historyEntries = mutableListOf<HistoryEntry>()
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyHeader: TextView
    private lateinit var historyRecyclerView: RecyclerView
    private var historyOpen = false
    private val prefsName = "croatian_pick_prefs"
    private val playersKey = "players"
    private val historyKey = "history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_croatian_pick)

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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                resetGameAndGoBack()
            }
        })

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val buttonCroatianPickRules: Button = findViewById(R.id.buttonCroatianPickRules)
        buttonCroatianPickRules.setOnClickListener {
            it.startAnimation(bounceAnimation)
            startActivity(Intent(this, CroatianPickRules::class.java))
        }

        val buttonBackFromCroatianPick: Button = findViewById(R.id.buttonBackFromCroatianPick)
        buttonBackFromCroatianPick.setOnClickListener {
            it.startAnimation(bounceAnimation)
            resetGameAndGoBack()
        }

        val addButton: Button = findViewById(R.id.buttonAddPlayer)
        addButton.setOnClickListener {
            addPlayer()
        }

        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(historyEntries)
        historyRecyclerView.adapter = historyAdapter
        historyHeader = findViewById(R.id.historyHeader)
        historyHeader.setOnClickListener {
            setHistoryOpen(!historyOpen)
        }
    }

    private fun setHistoryOpen(open: Boolean) {
        historyOpen = open
        historyHeader.text = if (open) "History ▲" else "History ▼"
        if (!open) {
            historyRecyclerView.visibility = View.GONE
            return
        }
        historyRecyclerView.visibility = View.VISIBLE
        historyHeader.post { updateHistoryListHeight() }
    }

    private fun updateHistoryListHeight() {
        if (!historyOpen) return

        val headerLocation = IntArray(2)
        historyHeader.getLocationOnScreen(headerLocation)
        val headerBottomY = headerLocation[1] + historyHeader.height
        val screenHeight = resources.displayMetrics.heightPixels
        val marginPx = (24 * resources.displayMetrics.density).toInt()
        val listHeight = (screenHeight - headerBottomY - marginPx).coerceAtLeast(0)

        historyRecyclerView.layoutParams.height = listHeight
        historyRecyclerView.requestLayout()
        scrollHistoryToBottom()
    }

    private fun scrollHistoryToBottom() {
        if (historyEntries.isEmpty()) return
        historyRecyclerView.scrollToPosition(historyEntries.size - 1)
    }

    private fun resetGameAndGoBack() {
        getSharedPreferences(prefsName, MODE_PRIVATE)
            .edit()
            .remove(playersKey)
            .remove(historyKey)
            .apply()

        players.clear()
        adapter.notifyDataSetChanged()

        historyEntries.clear()
        historyAdapter.notifyDataSetChanged()

        startActivity(Intent(this, CardGamesActivity::class.java))
        finish()
    }

    private fun addHistoryEntry(text: String) {
        val index = historyEntries.size
        historyEntries.add(HistoryEntry(text))
        historyAdapter.notifyItemInserted(index)
        saveHistory()
        if (historyOpen) {
            historyHeader.post { scrollHistoryToBottom() }
        }
    }

    private fun addPlayer() {
        val editText = EditText(this)
        editText.hint = "Enter player name"

        AlertDialog.Builder(this)
            .setTitle("New Player")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val name = editText.text.toString().ifEmpty { "Player ${players.size + 1}" }
                players.add(Player(name))
                adapter.notifyItemInserted(players.size - 1)
                savePlayers()
            }
            .setNegativeButton("Cancel", null)
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

        prefs.edit().putString(playersKey, jsonArray.toString()).apply()
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

        prefs.edit().putString(historyKey, jsonArray.toString()).apply()
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