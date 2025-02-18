package com.barkobi.myapplication_studentapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.barkobi.myapplication_studentapp.model.Model

class StudentDetails : AppCompatActivity() {

    companion object {
        const val EDIT_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.student_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val studentName: TextView = findViewById(R.id.name_text_view)
        val studentId: TextView = findViewById(R.id.id_text_view)
        val studentNumber: TextView = findViewById(R.id.number_text_view)
        val editButton: Button = findViewById(R.id.edit_button)
        val cancelButton: Button = findViewById(R.id.cancel_button)
        val deleteButton: Button = findViewById(R.id.delete_button2)
        val checkBox: CheckBox = findViewById(R.id.details_check_box)

        val studentIdString = intent.getStringExtra("id")
        val student = Model.shared.students.find { it.id == studentIdString }

        // Set initial data
        studentName.text = student?.name
        studentId.text = student?.id
        studentNumber.text = student?.number
        checkBox.isChecked = student?.isChecked ?: false

        // Update the model when the checkbox state changes
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            student?.isChecked = isChecked
        }

        // Handle Edit button click
        editButton.setOnClickListener {
            val intent = Intent(this, EditStudent::class.java)
            intent.putExtra("student_name", studentName.text.toString())
            intent.putExtra("student_id", studentId.text.toString())
            intent.putExtra("student_phone", studentNumber.text.toString())
            intent.putExtra("student_isChecked", checkBox.isChecked)
            startActivityForResult(intent, EDIT_REQUEST_CODE)
        }

        // Handle Delete button click
        deleteButton.setOnClickListener {
            if (student != null) {
                Model.shared.students.remove(student)

                val resultIntent = Intent()
                resultIntent.putExtra("deleted_student_id", student.id)
                setResult(RESULT_OK, resultIntent)

                finish()
            }
        }

        // Handle Cancel button click
        cancelButton.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            val updatedName = data?.getStringExtra("updated_name")
            val updatedId = data?.getStringExtra("updated_id")
            val updatedPhone = data?.getStringExtra("updated_phone")
            val updatedIsChecked = data?.getBooleanExtra("updated_isChecked", false)

            val student = Model.shared.students.find { it.id == intent.getStringExtra("id") }
            if (student != null) {
                student.name = updatedName ?: student.name
                student.id = updatedId ?: student.id
                student.number = updatedPhone ?: student.number
                student.isChecked = updatedIsChecked ?: student.isChecked
            }

            // Update UI
            findViewById<TextView>(R.id.name_text_view).text = updatedName
            findViewById<TextView>(R.id.id_text_view).text = updatedId
            findViewById<TextView>(R.id.number_text_view).text = updatedPhone
            findViewById<CheckBox>(R.id.details_check_box).isChecked = updatedIsChecked ?: false
        }
    }
}
