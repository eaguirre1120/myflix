package com.eaguirre.myflix.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eaguirre.myflix.ui.common.basicDiffUtil
import com.bumptech.glide.Glide
import com.eaguirre.myflix.databinding.ViewMovieItemBinding
import com.eaguirre.myflix.model.Movie

/*interface MovieClickedListener{
    fun onMovieClicked(movie: Movie) // (Movie)->Unit
}*/

class MoviesAdapter(
    private val listener: (Movie) -> Unit
    ) :
        RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var movies: List<Movie> by basicDiffUtil(
            emptyList(),
    areItemsTheSame = { old, new -> old.id == new.id})

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { listener(movie) }
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(private val binding: ViewMovieItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(movie: Movie){
            binding.title.text = movie.title
            Glide
                .with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w185/${movie.poster_path}")
                .into(binding.cover)
        }
    }
}