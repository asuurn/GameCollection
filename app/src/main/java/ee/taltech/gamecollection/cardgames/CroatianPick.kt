package ee.taltech.gamecollection.cardgames

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ee.taltech.gamecollection.R

class CroatianPick : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter
    private val players = mutableListOf<Player>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_croatian_pick)

        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PlayerAdapter(players)
        recyclerView.adapter = adapter

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val buttonCroatianPickRules: Button = findViewById(R.id.buttonCroatianPickRules)
        buttonCroatianPickRules.setOnClickListener {
            it.startAnimation(bounceAnimation)
            startActivity(Intent(this, CroatianPickRules::class.java))
        }

        val buttonBackFromCroatianPick: Button = findViewById(R.id.buttonBackFromCroatianPick)
        buttonBackFromCroatianPick.setOnClickListener {
            it.startAnimation(bounceAnimation)
            startActivity(Intent(this, CardGamesActivity::class.java))
        }

        val addButton: Button = findViewById(R.id.buttonAddPlayer)
        addButton.setOnClickListener {
            addPlayer()
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
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}