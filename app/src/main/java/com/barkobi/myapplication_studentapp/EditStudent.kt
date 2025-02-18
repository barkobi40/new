package com.barkobi.myapplication_studentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.barkobi.myapplication_studentapp.model.Model
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText

class EditStudent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_student)

        // Retrieve data from the intent
        val studentId = intent.getStringExtra("student_id")
        val studentName = intent.getStringExtra("student_name")
        val studentPhone = intent.getStringExtra("student_phone")
        val studentIsChecked = intent.getBooleanExtra("student_isChecked", false)

        // Link UI elements
        val nameEditText: TextInputEditText = findViewById(R.id.NameEditText)
        val idEditText: TextInputEditText = findViewById(R.id.IDEditText)
        val phoneEditText: TextInputEditText = findViewById(R.id.PhoneEditText)
        val checkBox: MaterialCheckBox = findViewById(R.id.checkBox)
        val updateButton: Button = findViewById(R.id.UpdateStudentBTN)
        val cancelButton: Button = findViewById(R.id.CancelBTN)

        // Pre-fill fields
        nameEditText.setText(studentName)
        idEditText.setText(studentId)
        phoneEditText.setText(studentPhone)
        checkBox.isChecked = studentIsChecked

        // Handle Update button click
        updateButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedId = idEditText.text.toString()
            val updatedPhone = phoneEditText.text.toString()
            val isChecked = checkBox.isChecked

            if (updatedName.isEmpty() || updatedId.isEmpty() || updatedPhone.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update the shared model
            val student = Model.shared.students.find { it.id == studentId }
            if (student != null) {
                student.name = updatedName
                student.id = updatedId
                student.number = updatedPhone
                student.isChecked = isChecked
            }

            // Pass updated data back
            val resultIntent = Intent()
            resultIntent.putExtra("updated_name", updatedName)
            resultIntent.putExtra("updated_id", updatedId)
            resultIntent.putExtra("updated_phone", updatedPhone)
            resultIntent.putExtra("updated_isChecked", isChecked)
            setResult(RESULT_OK, resultIntent)

            finish()
        }

        // Handle Cancel button click
        cancelButton.setOnClickListener {
            finish()
        }
    }
}
