package com.jasvir.dogsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.jasvir.dogsapp.R
import com.jasvir.dogsapp.coroutines.MainThreadScope
import com.google.android.material.snackbar.Snackbar
import com.jasvir.dogsapp.data.DogData
import com.jasvir.dogsapp.networkstates.BreedState
import com.jasvir.dogsapp.networkstates.DogsState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dogs.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel


class DogsFragment : Fragment(), OndogClickListener {


    private val uiScope = MainThreadScope()
    private var dogsViewModel: DogsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(uiScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dogs, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        val dogAdapter = DogAdapter(emptyList(), this)
        dogsRecyclerView?.apply {
            adapter = dogAdapter
        }

        dogsViewModel = getViewModel()
        dogsViewModel?.dogLiveData?.observe(this, Observer { dogState ->
            if (dogState == null) {
                return@Observer
            }

            when (dogState) {
                is DogsState.Loading -> {
                    setUpdateLayoutVisibilty(View.VISIBLE)
                }

                is DogsState.Error -> {
                    setUpdateLayoutVisibilty(View.GONE)
                    context?.let {
                        val message = dogState.message ?: getString(R.string.error)
                        Snackbar.make(activity!!.rootLayout, message, Snackbar.LENGTH_LONG).show()
                    }
                }

                is DogsState.DogsLoaded -> {
                    setUpdateLayoutVisibilty(View.GONE)
                    dogAdapter.updatedogs(dogState.dogs)
                }
            }
        })

        dogsViewModel?.breedLiveData?.observe(this, Observer {

            if (it == null) {
                return@Observer
            }

            when(it){
                is BreedState.Loaded ->{
                    val spinList = ArrayList<String>()
                    for (breed in it.dogs){
                        spinList.add(breed.name)
                    }

                    val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        activity, android.R.layout.simple_spinner_item,
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

                        val breedId =  it.dogs[position].id
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
            }

        })




        dogsViewModel?.getBreeds()
    }

    override fun dogClicked(dog: DogData) {
     //   findNavController().navigate(dogsFragmentDirections.actiondogsFragmentToCommentsFragment(dog.id))
    }

    fun setUpdateLayoutVisibilty(value: Int) {
        activity!!.updateLayout.apply {
            visibility = value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
