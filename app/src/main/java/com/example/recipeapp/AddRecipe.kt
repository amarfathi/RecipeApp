package com.example.recipeapp


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.recipeapp.databinding.ActivityAddRecipeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddRecipe : AppCompatActivity() {
    private lateinit var binding : ActivityAddRecipeBinding
    private lateinit var database : DatabaseReference
    private lateinit var storage : StorageReference
    lateinit var imageView: ImageView
    lateinit var imageUri : Uri
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addImageBtn.setOnClickListener{
            chooseImage()
        }
        binding.addRecipeBtn.setOnClickListener{
            showProgressBar()
            val recipeName = binding.recipeName.text.toString()
            val ingredient = binding.ingredient.text.toString()
            val step = binding.step.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Recipe")
            val Recipe = RecipeData(recipeName, ingredient, step)

                if(recipeName.isNotEmpty() && ingredient.isNotEmpty() && step.isNotEmpty()){
                    database.child(recipeName).setValue(Recipe).addOnSuccessListener {
                        Toast.makeText(this, "Successful Add Recipe",Toast.LENGTH_LONG).show()

                        uploadImage()

                        val intent = Intent(this,ViewRecipe::class.java)
                        startActivity(intent)
                    }.addOnFailureListener{
                        hideProgressBar()
                        Toast.makeText(this, "Failed",Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    hideProgressBar()
                    Toast.makeText(this, "All Field Required",Toast.LENGTH_LONG).show()
                }


        }
        binding.cancelBtn.setOnClickListener{
            val intent = Intent(this,ViewRecipe::class.java)
            startActivity(intent)
        }
    }
    private fun chooseImage() {
        getResult.launch("image/*")
    }
    val getResult = registerForActivityResult(ActivityResultContracts.GetContent()){ result ->
        imageUri = result
        imageView = binding.imageView
        imageView.setImageURI(imageUri)
    }
    private fun uploadImage() {
        val imageURI = imageUri
        storage = FirebaseStorage.getInstance().getReference(""+binding.recipeName.text.toString())
        storage.putFile(imageURI).addOnSuccessListener {
            hideProgressBar()
            Toast.makeText(this, "Success Upload Image",Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            hideProgressBar()
            Toast.makeText(this, "Image Failed Uploaded",Toast.LENGTH_LONG).show()
        }
    }
    private fun showProgressBar(){
        dialog = Dialog(this@AddRecipe)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    private fun hideProgressBar(){
        dialog.dismiss()
    }
}