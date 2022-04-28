package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ActivityViewdetailBinding
import com.google.firebase.database.*

class ViewRecipe : AppCompatActivity() {
    private lateinit var database : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding : ActivityViewdetailBinding
    lateinit var spinner : Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewdetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBtn.setOnClickListener{
            val intent = Intent(this,AddRecipe::class.java)
            startActivity(intent)

        }
        recyclerView = binding.recipeList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        getData()
    }

    private fun getData() {
        database = FirebaseDatabase.getInstance().getReference("Recipe")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var arrayList = ArrayList<RecipeData>()

                if(snapshot.exists()){
                    for(recipeSnapshot in snapshot.children){
                        val recipe = recipeSnapshot.getValue(RecipeData::class.java)
                        arrayList.add(recipe as RecipeData)
                    }
//                    recyclerView.adapter = RecipeAdapter(arrayList) <---Stuck Here
                }
                if(arrayList.size > 0){
                    Toast.makeText(applicationContext,"There are ${arrayList.size} list of recipe", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}