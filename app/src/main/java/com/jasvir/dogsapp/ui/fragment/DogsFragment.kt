package com.jasvir.dogsapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.PagingData
import com.jasvir.dogsapp.R
import com.jasvir.dogsapp.coroutines.MainThreadScope
import com.google.android.material.snackbar.Snackbar
import com.jasvir.dogsapp.data.Breed
import com.jasvir.dogsapp.data.DogData
import com.jasvir.dogsapp.networkstates.BreedState
import com.jasvir.dogsapp.networkstates.DogsState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dogs.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel


class DogsFragment : Fragment(), LifecycleObserver {
    private val uiScope = MainThreadScope()
    private var dogsViewModel: DogsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(uiScope)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        val dogAdapter = DogAdapter()
        dogsRecyclerView?.apply {
            adapter = dogAdapter
        }

        dogsViewModel = getViewModel()

        dogsViewModel?.breedLiveData?.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is BreedState.Loading -> {
                    setUpdateLayoutVisibilty(View.VISIBLE)
                }

                is BreedState.Error -> {
                    setUpdateLayoutVisibilty(View.GONE)
                    context?.let {
                        val message = getString(R.string.error)
                        Snackbar.make(requireActivity().rootLayout, message, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }

                is BreedState.Loaded -> {
                    setUpdateLayoutVisibilty(View.GONE)
                    setupSpinner(it.dogs, dogAdapter)
                }
            }

        })

        dogsViewModel?.getBreeds()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dogs, container, false)
    }


    fun setupSpinner(list: List<Breed>, dogAdapter: DogAdapter) {
        val spinList = ArrayList<String>()
        for (breed in list) {
            spinList.add(breed.name)
        }
        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item,
            spinList
        ) //selected item will look like a spinner set from XML

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spBreed.setAdapter(spinnerArrayAdapter)
        spBreed.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //  dogsViewModel?.refreshdogs(it.dogs[position].id)
                val breedId = list[position].id
                GlobalScope.launch {
                    dogAdapter.submitData(PagingData.empty())
                    dogsViewModel?.getListforSearch(breedId)?.collect {
                        dogAdapter.submitData(it)
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


    }

    fun setUpdateLayoutVisibilty(value: Int) {
        requireActivity().updateLayout.apply {
            visibility = value
        }
    }

}
