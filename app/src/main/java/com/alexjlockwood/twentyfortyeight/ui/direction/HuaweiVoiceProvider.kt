package com.alexjlockwood.twentyfortyeight.ui.direction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alexjlockwood.twentyfortyeight.domain.Direction
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants
import com.huawei.hms.mlsdk.asr.MLAsrConstants
import com.huawei.hms.mlsdk.asr.MLAsrListener
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer

private const val ENGLISH_LANGUAGE_CODE = "en-US"
private const val VOICE_TAG = "hmsVoice"

internal class HuaweiVoiceProvider(
    private val context: Context,
    private val voiceDirectionExtractor: VoiceDirectionExtractor,
    private val onDirectionListener: (direction: Direction) -> Unit,
) : MLAsrListener, DirectionProvider {

    private var asrRecognizer: MLAsrRecognizer? = null
    private lateinit var recognizerIntent: Intent
    private var enabled = false

    init {
        setupAsr()
    }

    override fun start() {
        asrRecognizer?.startRecognizing(recognizerIntent)
        enabled = true
    }

    override fun stop() {
        enabled = false
    }

    override fun destroy() {
        asrRecognizer?.destroy()
        asrRecognizer = null
    }

    override fun onResults(result: Bundle?) {
        Log.i(VOICE_TAG, "onResults: ${result?.getString(MLAsrRecognizer.RESULTS_RECOGNIZED)}")
        handleResult(result, MLAsrRecognizer.RESULTS_RECOGNIZED)
    }

    override fun onRecognizingResults(result: Bundle?) {
        Log.i(VOICE_TAG, "onRecognizingResults: ${result?.getString(MLAsrRecognizer.RESULTS_RECOGNIZED)}")
    }

    override fun onError(errorCode: Int, errorMessage: String?) {
        Log.i(VOICE_TAG, "onError: $errorCode, $errorMessage")
    }

    override fun onStartListening() {
        Log.i(VOICE_TAG, "onStartListening")
    }

    override fun onStartingOfSpeech() {
        Log.i(VOICE_TAG, "onStartingOfSpeech")
    }

    override fun onVoiceDataReceived(p0: ByteArray?, p1: Float, p2: Bundle?) {
        Log.i(VOICE_TAG, "onVoiceDataReceived")
    }

    override fun onState(p0: Int, p1: Bundle?) {
        Log.i(VOICE_TAG, "onState: $p0")
    }

    private fun setupAsr() {
        asrRecognizer = MLAsrRecognizer.createAsrRecognizer(context)
        asrRecognizer?.setAsrListener(this)
        recognizerIntent = Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH).apply {
            putExtra(MLAsrCaptureConstants.LANGUAGE, ENGLISH_LANGUAGE_CODE)
            putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
        }
    }

    private fun handleResult(bundle: Bundle?, mapKey: String) {
        val text = bundle?.getString(mapKey) ?: return
        Log.i(VOICE_TAG, "mapping results : $text")
        voiceDirectionExtractor.extractDirection(text)?.let {
            onDirectionListener.invoke(it)
        }
        if (enabled) {
            asrRecognizer?.destroy()
            setupAsr()
            asrRecognizer?.startRecognizing(recognizerIntent)
        }
    }
}