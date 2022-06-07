package com.ubaya.advweek4.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ubaya.advweek4.R
import com.ubaya.advweek4.databinding.FragmentStudentDetailBinding
import com.ubaya.advweek4.model.Student
import com.ubaya.advweek4.viewmodel.DetailViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class StudentDetailFragment : Fragment(), ButtonUpdateClickListener, ButtonNotificationClickListener {
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding: FragmentStudentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_student_detail, container, false
        )

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(arguments != null) {
            dataBinding.updateListener = this
            dataBinding.notificationListener = this

            val studentID = StudentDetailFragmentArgs.fromBundle(requireArguments()).studentID

            viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
            viewModel.fetch(studentID)

            observeViewModel()
        } else {
            Toast.makeText(context, "Student not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.studentLiveData.observe(viewLifecycleOwner) {
            dataBinding.student = it
//            val student = it
//            student?.let {
//                imageStudentDetailPhoto.loadImage(it.photoURL, progressBarStudentDetail)
//                editID.setText(it.id)
//                editName.setText(it.name)
//                editDOB.setText(it.dob)
//                editPhone.setText(it.phone)
//            }
        }
    }

    override fun onButtonUpdateClick(v: View, obj: Student) {
        Toast.makeText(context, "Student (${obj.id}) data updated!", Toast.LENGTH_SHORT).show()
    }

    override fun onButtonNotificationClick(v: View, obj: Student) {
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("mynotif", "Notification delayed after 5 seconds")
                MainActivity.showNotification(obj.name.toString(), "Notification created",
                R.drawable.ic_baseline_person_24)
            }
    }
}