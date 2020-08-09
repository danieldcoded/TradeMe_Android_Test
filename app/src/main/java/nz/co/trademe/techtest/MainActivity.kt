package nz.co.trademe.techtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import nz.co.trademe.techtest.adapters.ListingsAdapter
import nz.co.trademe.techtest.ui.TopSpacingItemDecoration
import nz.co.trademe.wrapper.TradeMeApi
import nz.co.trademe.wrapper.dto.ClosingSoonListings
import nz.co.trademe.wrapper.models.Listing
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<ClosingSoonListings>, ListingsAdapter.Interaction {

    private lateinit var listingsAdapter: ListingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This will hide the top bar from the activity
        hideTopBar()

        // Initialise the recycler view
        initRecyclerView()

        // Start the listing server for retrieving closing soon listings
        TradeMeApi.listingService.retrieveClosingSoonListings().enqueue(this)
    }

    override fun onFailure(call: Call<ClosingSoonListings>, t: Throwable) {
        Toast.makeText(this, "Error loading closing soon listings.", Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<ClosingSoonListings>, response: Response<ClosingSoonListings>) {
        // Make sure that the body of the response is not null
        when (val body = response.body()) {
            null -> println("TODO: SHOW ERROR LAYOUT")
            else -> listingsAdapter.submitList(ArrayList(body.list))
        }
    }

    // This method will initialise the recycler view by attaching the adapter  attached padding between items and
    private fun initRecyclerView() {
        recycler_view.apply {
            listingsAdapter = ListingsAdapter(this@MainActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(resources.getInteger(R.integer.padding_between_listings))
            addItemDecoration(topSpacingDecorator)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listingsAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Listing) {
        println("THIS WORKS NOW")
    }

    private fun hideTopBar() {
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
    }
}
