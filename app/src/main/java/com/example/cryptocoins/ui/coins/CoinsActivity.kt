package com.example.cryptocoins.ui.coins

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cryptocoins.domain.Coin
import com.example.cryptocoins.ui.coindetails.CoinDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinsActivity : AppCompatActivity() {
//    class CoinsActivity : AppCompatActivity(), CoinsAdapter.OnItemClickListener {

    private val coinsViewModel: CoinsViewModel by viewModels()
//    private val coinsAdapter = CoinsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            coinsList()
        }

        coinsViewModel.getCoins()

        // TODO: Delete
//        setContentView(R.layout.activity_coins)

        /*
        recyclerView.apply {
            coinsAdapter.onItemClickListener = this@CoinsActivity
            adapter = coinsAdapter
            addItemDecoration(CoinItemDecoration())
        }

        reloadButton.setOnClickListener {
            coinsViewModel.getCoins()
        }


        coinsViewModel.viewCommand.observe(this, Observer { handleCommand(it) })
        coinsViewModel.viewState.observe(this, Observer { handleState(it) })
         */

    }

    @Preview
    @Composable
    fun coinsList(coins: List<Coin>? = null) {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .size(100.dp)
                .verticalScroll(rememberScrollState())
        ) {
            coins?.forEach { coin ->
                Text(
                    coin.name,
                    color = Color.White
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
                // TODO: Update composable here somehow
//                coinsAdapter.submitList(state.coins)
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