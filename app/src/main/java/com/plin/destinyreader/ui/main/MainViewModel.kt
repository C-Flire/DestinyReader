package com.plin.destinyreader.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.plin.destinyreader.database.DestinyDatabaseDao
import com.plin.destinyreader.jsonParser.jsonParser.JSONParser
import com.plin.destinyreader.jsonParser.jsonPresentationNode.JSONPresentationNode
import com.plin.destinyreader.ui.abstractList.AbstractListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(dataSource: DestinyDatabaseDao, application: Application) :
    AbstractListViewModel(application, dataSource, 0) {


    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    override val title = MutableLiveData<String>()
    override val _itemsList = MutableLiveData<List<JSONPresentationNode>>()
    override val itemsList: LiveData<List<JSONPresentationNode>>
        get() = _itemsList

    init {
        uiScope.launch {
            _itemsList.value = findItemsFromDatabase(iD)
        }
    }

    val iD: Long = 564676571
    override suspend fun findItemsFromDatabase(id: Long): List<JSONPresentationNode> {
        return withContext(Dispatchers.IO) {
            val binaryArray = database.getDestinyPresentationNode(iD)!!.json
            val string = String(requireNotNull(binaryArray), Charsets.UTF_8).dropLast(1)
            val mainPresentationNode = Gson().fromJson(
                string,
                JSONPresentationNode::class.java
            )

            Log.i("DATABASE", mainPresentationNode.displayProperties.name)
            val items: MutableList<JSONPresentationNode> = ArrayList()
            Log.i(
                "destinyreader",
                "Categories Found :" + mainPresentationNode.children.presentationNodes.size.toString()
            )
            mainPresentationNode.children.presentationNodes.forEach {
                val string = String(
                    requireNotNull(database.getDestinyPresentationNode(JSONParser.hashToId(it.presentationNodeHash))!!.json),
                    Charsets.UTF_8
                ).dropLast(1)
                items.add(
                    Gson().fromJson(
                        string,
                        JSONPresentationNode::class.java
                    )
                )
            }
            Log.i("destinyreader", "items size" + items.size.toString())
            items
        }

    }

}
