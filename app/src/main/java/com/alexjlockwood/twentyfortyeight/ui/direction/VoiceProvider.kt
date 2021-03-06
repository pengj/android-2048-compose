package com.alexjlockwood.twentyfortyeight.ui.direction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.alexjlockwood.twentyfortyeight.domain.Direction

private const val VOICE_TAG = "voiceObserver"
private const val KEY_UNSTABLE_TEXT = "android.speech.extra.UNSTABLE_TEXT"
internal class VoiceProvider(
    context: Context,
    private val voiceDirectionExtractor: VoiceDirectionExtractor,
    private val onDirectionListener: (direction: Direction) -> Unit
) : RecognitionListener, DirectionProvider {
    private val speech: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val recognizerIntent: Intent
    private var enabled = false
    private var startMap = true

    init {
        speech.setRecognitionListener(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en")
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }


    override fun start() {
        Log.i(VOICE_TAG, "start")
        speech.startListening(recognizerIntent)
        enabled = true
    }

    override fun stop() {
        Log.i(VOICE_TAG, "stop")
        speech.stopListening()
        enabled = false
    }

    override fun destroy() {
        Log.i(VOICE_TAG, "close")
        speech.destroy()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.i(VOICE_TAG, "onReadyForSpeech")
        startMap = true
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onRmsChanged(rmsdB: Float) {
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(error: Int) {
        Log.i(VOICE_TAG, "onError: $error")
        if (error == 7) {
            if (enabled) {
                speech.startListening(recognizerIntent)
            }
        }
    }

    override fun onResults(results: Bundle?) {
        Log.i(VOICE_TAG, "onResults")
        handleResult(results, SpeechRecognizer.RESULTS_RECOGNITION)
        speech.startListening(recognizerIntent)
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Log.i(VOICE_TAG, "onPartialResults")
        handleResult(partialResults, KEY_UNSTABLE_TEXT)
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.i(VOICE_TAG, "onEvent: $eventType")
    }

    private fun handleResult(bundle: Bundle?, mapKey: String) {
        val matches = bundle?.getStringArrayList(mapKey)
        var text = ""

        matches?.forEach {
            text = text.plus(it)
        }

        Log.i(VOICE_TAG, text)
        if (startMap) {
            voiceDirectionExtractor.extractDirection(text)?.let {
                onDirectionListener.invoke(it)
                startMap = false
            }
        }
    }
}