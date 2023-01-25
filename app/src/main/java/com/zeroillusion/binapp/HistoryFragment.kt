package com.zeroillusion.binapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zeroillusion.binapp.data.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var db: Database
    private val binViewModel: BinViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnClean = view.findViewById<FloatingActionButton>(R.id.btn_cleaning_history)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = Adapter(binViewModel.listBins.value!!)

        db = Room.databaseBuilder(
            requireContext(),
            Database::class.java, "db"
        ).build()
        val binDao = db.binDao()

        btnClean.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setMessage(resources.getString(R.string.message))
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.positive_button)) { _, _ ->
                    CoroutineScope(Dispatchers.Default).launch {
                        binDao.deleteAll()
                        binViewModel.setListBins(binDao.getBins())
                    }

                    val navHostFragment = requireActivity().supportFragmentManager
                        .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    requireActivity().findNavController(R.id.nav_host_fragment)
                        .navigateUp(AppBarConfiguration(navHostFragment.navController.graph))

                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.result_message),
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton(resources.getString(R.string.negative_button)) { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        db.close()
    }
}