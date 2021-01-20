package com.alexjlockwood.twentyfortyeight

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.setContent
import com.alexjlockwood.twentyfortyeight.ui.AppTheme
import com.alexjlockwood.twentyfortyeight.ui.GameUi
import com.alexjlockwood.twentyfortyeight.ui.observer.DirectionObserver
import com.alexjlockwood.twentyfortyeight.ui.observer.ObserverFactory
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModel
import com.alexjlockwood.twentyfortyeight.viewmodel.GameViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener


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
                        onDebugRequested = { debug -> gameViewModel.debugChange(debug) },
                        onVoiceRequested = { enabled -> gameViewModel.enableVoice(enabled) },
                        onNewGameRequested = { gameViewModel.startNewGame() },
                    ) { direction -> gameViewModel.move(direction) }
                }
            }
        }

        checkRecordPermission()
        setObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceObserver.destroy()
    }

    private fun checkRecordPermission() {
        if (isGoogleServiceAvailable(this)) {
            return
        }
        Dexter.withContext(this)
            .withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(getPermissionListener())
            .check();
    }

    private fun getPermissionListener() = SnackbarOnDeniedPermissionListener.Builder
        .with(window.decorView, R.string.record_permission)
        .withOpenSettingsButton(R.string.settings)
        .withCallback(object : Snackbar.Callback() {
            override fun onShown(snackbar: Snackbar) {
            }

            override fun onDismissed(snackbar: Snackbar, event: Int) {
            }
        }).build()

    private fun setObservers() {
        voiceObserver = ObserverFactory.getDirectionObserver(this) { direction -> gameViewModel.move(direction)
        }
        voiceObserver.init(this)
        gameViewModel.setDirectionObserver(voiceObserver)
    }

}
