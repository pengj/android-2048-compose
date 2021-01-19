package com.alexjlockwood.twentyfortyeight

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.setContent
import com.alexjlockwood.twentyfortyeight.ui.AppTheme
import com.alexjlockwood.twentyfortyeight.ui.GameUi
import com.alexjlockwood.twentyfortyeight.ui.observer.DirectionObserver
import com.alexjlockwood.twentyfortyeight.ui.observer.HuaweiVoiceObserver
import com.alexjlockwood.twentyfortyeight.ui.observer.ObserverFactory
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModelFactory
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.mlsdk.common.MLApplication

class MainActivity : AppCompatActivity() {

    private lateinit var voiceObserver: DirectionObserver
    private val gameViewModel by viewModels<GameViewModel> { GameViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    GameUi(
                        gridTileMovements = gameViewModel.gridTileMovements,
                        state = gameViewModel.gameState,
                        onDebugRequested = {debug -> gameViewModel.debugChange(debug)},
                        onVoiceRequested = {enabled -> gameViewModel.enableVoice(enabled)},
                        onNewGameRequested = { gameViewModel.startNewGame() },
                    ) { direction -> gameViewModel.move(direction) }
                }
            }
        }

        setObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceObserver.destroy()
    }

    private fun setObservers() {
        voiceObserver = ObserverFactory.getDirectionObserver(this) {
                direction -> gameViewModel.move(direction)
        }
        voiceObserver.init(this)
        gameViewModel.setDirectionObserver(voiceObserver)
    }

}
