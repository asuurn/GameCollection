package ee.taltech.gamecollection.cardgames.CroatianPick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ee.taltech.gamecollection.R

class PlayerAdapter(
    private val players: MutableList<Player>,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.playerName)
        val score: TextView = view.findViewById(R.id.playerScore)
        val amountInput: EditText = view.findViewById(R.id.scoreAmountInput)
        val addButton: Button = view.findViewById(R.id.addScoreButton)
        val subtractButton: Button = view.findViewById(R.id.subtractScoreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]

        holder.name.text = player.name
        holder.score.text = player.score.toString()

        holder.addButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val amount = holder.amountInput.text.toString().toIntOrNull() ?: 0
            players[pos].score += amount
            holder.amountInput.text.clear()
            notifyItemChanged(pos)
            onDataChanged()
        }

        holder.subtractButton.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val amount = holder.amountInput.text.toString().toIntOrNull() ?: 0
            players[pos].score -= amount
            holder.amountInput.text.clear()
            notifyItemChanged(pos)
            onDataChanged()
        }

        holder.name.setOnClickListener {
            val context = holder.itemView.context
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

            val editText = EditText(context)
            editText.setText(players[pos].name)

            AlertDialog.Builder(context)
                .setTitle("Edit Name")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val newName = editText.text.toString().ifEmpty { players[pos].name }
                    players[pos].name = newName
                    notifyItemChanged(pos)
                    onDataChanged()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount() = players.size
}