package com.example.cryptocoins.ui.coins

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.cryptocoins.domain.Coin
import com.example.cryptocoins.R
import com.example.cryptocoins.ui.coindetails.CoinDetailsActivity
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinsActivity : AppCompatActivity() {

    private val coinsViewModel: CoinsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            createUx()
        }

        coinsViewModel.getCoins()
        coinsViewModel.viewCommand.observe(this, { handleCommand(it) })
    }

    @Preview
    @Composable
    fun createUx() {
        val viewState by coinsViewModel.viewState.observeAsState()

        when (viewState) {
            is CoinsViewModel.ViewState.Loading -> {
                createLoadingState()
            }
            is CoinsViewModel.ViewState.Error -> {
                createErrorState()
            }
            is CoinsViewModel.ViewState.Success -> {
                createCoinList((viewState as CoinsViewModel.ViewState.Success).coins)
            }
        }

    }

    @Composable
    fun createLoadingState() {
        Text(getString(R.string.loading))
    }

    @Composable
    fun createErrorState() {
        Text(getString(R.string.error))
    }

    @Composable
    fun createCoinList(coins: List<Coin>) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = coinsViewModel.isRefreshing),
            onRefresh = { coinsViewModel.getCoins() }
            ) {
            Column(
                modifier = Modifier
                    .background(Color.LightGray)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                coins.forEach { coin ->
                    ClickableText(
                        text = AnnotatedString(coin.name),
                        style = TextStyle.Default,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        coinsViewModel.onCoinClicked(coin = coin)
                    }
                }
            }
        }
    }

    private fun handleCommand(command: CoinsViewModel.ViewCommand) {
        when (command) {
            is CoinsViewModel.ViewCommand.ShowCoinDetails -> {
                val coin = command.coin
                val intent = CoinDetailsActivity.createIntent(this, coin.id)
                startActivity(intent)
            }
        }
    }
}