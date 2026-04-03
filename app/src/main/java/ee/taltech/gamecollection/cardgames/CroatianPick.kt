package ee.taltech.gamecollection.cardgames

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ee.taltech.gamecollection.R
import org.json.JSONArray
import org.json.JSONObject

class CroatianPick : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter
    private val players = mutableListOf<Player>()

    private val prefsName = "croatian_pick_prefs"
    private val playersKey = "players"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_croatian_pick)

        loadPlayers()

        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PlayerAdapter(players) {
            savePlayers()
        }
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
    }

    private fun resetGameAndGoBack() {
        getSharedPreferences(prefsName, MODE_PRIVATE)
            .edit()
            .remove(playersKey)
            .apply()

        players.clear()
        adapter.notifyDataSetChanged()

        startActivity(Intent(this, CardGamesActivity::class.java))
        finish()
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
}