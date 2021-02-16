package com.naldana.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.naldana.myapplication.data.entity.Pokemon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModelFactory by lazy {
        val myApplication = application as MyApplication
        val database = myApplication.database
        val pokeRepo = myApplication.pokemonRepository
        MyViewModelFactory(database,pokeRepo)
    }

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    object PokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onFindPokemon()

        viewModel.pokemonInfo.observe(this) {
            if (it != null) {
                val textView = findViewById<TextView>(R.id.text_pokemon_info)
                textView.text = it.name
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_pokemon_list)
        val adapter = PokemonPageAdapter(PokemonComparator)
        recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.pager.flow.collectLatest {
                if(it != null){
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun onFindPokemon() {
        val find = findViewById<Button>(R.id.action_find_pokemon)
        find.setOnClickListener {
            val queryEdit = findViewById<EditText>(R.id.pokemon_query_edit)
            viewModel.getPokemon(queryEdit.text.toString())
        }
    }
}