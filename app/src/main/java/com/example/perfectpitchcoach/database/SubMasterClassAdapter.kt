package com.example.perfectpitchcoach.database

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.perfectpitchcoach.R
import com.example.perfectpitchcoach.database.model.SubMasterClass

class SubMasterClassAdapter internal constructor(
    context: Context, userSolvedMasterClass: Int, masterClassName: String
) : RecyclerView.Adapter<SubMasterClassAdapter.WordViewHolder>() {

    private val masterClassName = masterClassName
    private val userSolvedMasterClass = userSolvedMasterClass

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var masterClass = emptyList<SubMasterClass>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.sub_masterclass_item_rows, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = masterClass[position]
        holder.wordItemView.text = current.name + "  -  " + current.file_name

        if( current.solved == "YES" ) {
            holder.itemView.setBackgroundColor( ContextCompat.getColor(inflater.context, R.color.terminalFg) )
        }
        else {
            holder.itemView.setBackgroundColor( ContextCompat.getColor(inflater.context, android.R.color.holo_orange_light) )
        }

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                if( current.name == "Pitch naming drill" ) {

                    val intent = Intent(inflater.context, PitchNamingDrillActivity::class.java)
                    intent.putExtra("sub_master_class_unique_id", current.sub_master_class_unique_id)
                    intent.putExtra("masterClassName", masterClassName)
                    intent.putExtra("subMasterClassName", current.name)
                    intent.putExtra("JsonFilenameWithQuestions", current.file_name)
                    (inflater.context as Activity).startActivity(intent)
                }
                else if( current.name == "Pitch identify drill" ) {

                }
                else if( current.name == "Meditation" ) {

                }
                else {

                }
            }
        })
    }

    internal fun setWords(masterClass: List<SubMasterClass>) {
        this.masterClass = masterClass
        notifyDataSetChanged()
    }

    override fun getItemCount() = masterClass.size

}


