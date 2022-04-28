package com.example.recipeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class RecipeAdapter(private val recipeList : ArrayList<RecipeData>): RecyclerView.Adapter<RecipeAdapter.MyViewHolder>(){
    private lateinit var storage : StorageReference
    private lateinit var database : DatabaseReference
    lateinit var imageUrl: Bitmap
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeAdapter.MyViewHolder, position: Int) {
        val currentiItem = recipeList[position]

        holder.recipeName.text = currentiItem.recipeName
        storage = FirebaseStorage.getInstance().reference.child("${currentiItem.recipeName}")
        val local = File.createTempFile("RecipeImg","")
        storage.getFile(local)
        imageUrl = BitmapFactory.decodeFile("$storage")
        holder.recipeImage.setImageBitmap(imageUrl)

        holder.deleteBtn.setOnClickListener{
            database = FirebaseDatabase.getInstance().getReference("User")
            database.removeValue()
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val recipeImage : ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeName : TextView = itemView.findViewById(R.id.recipeName)
        val ingredient : TextView = itemView.findViewById(R.id.ingredient)
        val step : TextView = itemView.findViewById(R.id.step)
        val deleteBtn : Button = itemView.findViewById(R.id.deleteBtn)
    }
}