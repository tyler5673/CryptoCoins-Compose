package com.example.cryptocoins.ui.coins

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.cryptocoins.domain.Coin
import com.example.cryptocoins.ui.coindetails.CoinDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinsActivity : AppCompatActivity() {
//    class CoinsActivity : AppCompatActivity(), CoinsAdapter.OnItemClickListener {

    private val coinsViewModel: CoinsViewModel by viewModels()
    // TODO: Just observe directly on the state livedata from the viewmodel doofus
    private val coins = MutableLiveData<List<Coin>?>()
//    private val coinsAdapter = CoinsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            coinsList()
        }

        coinsViewModel.getCoins()

        /*
        recyclerView.apply {
            coinsAdapter.onItemClickListener = this@CoinsActivity
            adapter = coinsAdapter
            addItemDecoration(CoinItemDecoration())
        }

        reloadButton.setOnClickListener {
            coinsViewModel.getCoins()
        }

         */

        coinsViewModel.viewState.observe(this, Observer { handleState(it) })
    }

    @Preview
    @Composable
    fun coinsList() {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {

            val coinData: List<Coin>? by coins.observeAsState()

            coinData?.forEach { coin ->
                Text(
                    coin.name,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

//    override fun onItemClick(coin: Coin) {
//        coinsViewModel.onCoinClicked(coin)
//    }

    /*
    private fun handleCommand(command: CoinsViewModel.ViewCommand) {
        when (command) {
            is CoinsViewModel.ViewCommand.ShowCoinDetails -> {
                val coin = command.coin
                val intent = CoinDetailsActivity.createIntent(this, coin.id)
                startActivity(intent)
            }
        }
    }
     */

    private fun handleState(state: CoinsViewModel.ViewState) {
        when (state) {
            is CoinsViewModel.ViewState.Loading -> {
                showLoading()
                hideError()
                hideContent()
            }
            is CoinsViewModel.ViewState.Error -> {
                showError()
                hideLoading()
                hideContent()
            }
            is CoinsViewModel.ViewState.Success -> {
                coins.value = state.coins
                showContent()
                hideError()
                hideLoading()
            }
        }
    }

    private fun showLoading() {
//        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
//        progressBar.visibility = View.GONE
    }

    private fun showError() {
//        errorLinearLayout.visibility = View.VISIBLE
    }

    private fun hideError() {
//        errorLinearLayout.visibility = View.GONE
    }

    private fun showContent() {
//        recyclerView.visibility = View.VISIBLE
    }

    private fun hideContent() {
//        recyclerView.visibility = View.GONE
    }
}