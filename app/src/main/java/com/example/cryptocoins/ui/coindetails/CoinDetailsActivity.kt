package com.example.cryptocoins.ui.coindetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.cryptocoins.R
import com.example.cryptocoins.domain.Coin
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_coin_details.*

private const val COIN_ID = "COIN_ID"

@AndroidEntryPoint
class CoinDetailsActivity : AppCompatActivity() {

    private var coinId: String = ""
    private val coinDetailsViewModel: CoinDetailsViewModel by viewModels()

    companion object {

        fun createIntent(context: Context, coinId: String): Intent {
            val intent = Intent(context, CoinDetailsActivity::class.java)
            intent.putExtra(COIN_ID, coinId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras.apply {
            coinId = this?.getString(COIN_ID, "") ?: ""
        }

        setContent {
            createUx()
        }


        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        coinDetailsViewModel.getCoinDetails(coinId)

        coinDetailsViewModel.viewCommand.observe(this, Observer { handleCommand(it) })
    }

    @Preview
    @Composable
    fun createUx() {
        val viewState by coinDetailsViewModel.viewState.observeAsState()

        when (viewState) {
            is CoinDetailsViewModel.ViewState.Success -> {
                showSuccessState((viewState as CoinDetailsViewModel.ViewState.Success).coin)
            }
            CoinDetailsViewModel.ViewState.Error -> {
                showError()
            }
        }

    }

    @Composable
    fun showSuccessState(coin: Coin) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(coin.symbol)
            Text(coin.name)
            coin.description?.let {
                Text(coin.description.en)
            }
            Text(coin.name)
            coin.genesisDate?.let {
                Text(coin.genesisDate)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                coinDetailsViewModel.onBackClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleCommand(command: CoinDetailsViewModel.ViewCommand) {
        when (command) {
            is CoinDetailsViewModel.ViewCommand.CloseScreen -> {
                onBackPressed()
            }
        }
    }


    private fun showContent(coin: Coin){
        Picasso.get()
            .load(coin.image?.large)
            .into(coverImageView)

        symbolTextView.text = coin.symbol
        nameTextView.text = coin.name
        descriptionTextView.text = coin.description?.en
        genesisDateTextView.text = coin.genesisDate
    }

    private fun showError() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.oops_something_went_wrong), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.reload)) {
                coinDetailsViewModel.getCoinDetails(coinId)
            }
            .show()
    }

}