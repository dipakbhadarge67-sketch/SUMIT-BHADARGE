package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ChatInputBar
import com.example.ui.components.ChatMessageItem
import com.example.ui.components.ClearConfirmDialog
import com.example.ui.components.ExplainTopicDialog
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.components.MakeQuizDialog
import com.example.ui.components.QuickToolsRow
import com.example.ui.components.StarterQuestionsRow
import com.example.ui.components.StudyHeader
import com.example.ui.components.SubjectSelector
import com.example.ui.components.ThinkingIndicator
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun StudyBuddyScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Auto-scroll to latest message when messages change or while generating
    LaunchedEffect(uiState.messages.size, uiState.isGenerating) {
        val totalCount = uiState.messages.size + if (uiState.isGenerating) 1 else 0
        if (totalCount > 0) {
            listState.animateScrollToItem(totalCount - 1)
        }
    }

    // Show info messages in snackbar
    LaunchedEffect(uiState.infoMessage) {
        uiState.infoMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissInfoMessage()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .testTag("study_buddy_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Header (🤖 SUMIT AI with Language Switcher)
                StudyHeader(
                    selectedSubject = uiState.selectedSubject,
                    selectedLanguage = uiState.selectedLanguage,
                    onLanguageClick = { viewModel.openLanguageDialog() },
                    isApiKeyActive = uiState.isApiKeyActive
                )

                // Subject Selector (📐 Math, 🔬 Science, 📚 English, 🏛️ History, 💻 Coding)
                SubjectSelector(
                    selectedSubject = uiState.selectedSubject,
                    selectedLanguage = uiState.selectedLanguage,
                    onSubjectSelected = { subject ->
                        viewModel.selectSubject(subject)
                    }
                )

                // Quick Tools (📝 Make Quiz, 💡 Explain Topic, 🗑️ Clear)
                QuickToolsRow(
                    selectedLanguage = uiState.selectedLanguage,
                    onMakeQuizClick = { viewModel.openMakeQuizDialog() },
                    onExplainTopicClick = { viewModel.openExplainDialog() },
                    onClearChatClick = { viewModel.openClearConfirmDialog() }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    thickness = 1.dp
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    thickness = 1.dp
                )

                // Quick starter question pills
                StarterQuestionsRow(
                    subject = uiState.selectedSubject,
                    selectedLanguage = uiState.selectedLanguage,
                    onQuestionClick = { question ->
                        viewModel.sendQuestion(question)
                    }
                )

                // Chat input bar (Text input, Photo Picker attachment, Send button)
                ChatInputBar(
                    inputText = uiState.inputText,
                    onInputTextChanged = { viewModel.onInputTextChanged(it) },
                    onSendClick = { viewModel.sendQuestion() },
                    attachedImageUri = uiState.attachedImageUri,
                    onPhotoSelected = { uri -> viewModel.attachPhoto(uri) },
                    onRemovePhoto = { viewModel.removeAttachedPhoto() },
                    isGenerating = uiState.isGenerating,
                    selectedLanguage = uiState.selectedLanguage
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("chat_messages_list")
            ) {
                items(
                    items = uiState.messages,
                    key = { it.id }
                ) { message ->
                    ChatMessageItem(
                        message = message,
                        onAnswerQuizOption = { msgId, qIdx, optIdx, currentQuiz ->
                            viewModel.onAnswerQuizOption(msgId, qIdx, optIdx, currentQuiz)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (uiState.isGenerating) {
                    item(key = "thinking_indicator") {
                        ThinkingIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // Dialogs
    if (uiState.showLanguageDialog) {
        LanguageSelectorDialog(
            currentLanguage = uiState.selectedLanguage,
            canDismiss = !uiState.isInitialLanguagePrompt,
            onLanguageSelected = { lang ->
                viewModel.selectLanguage(lang)
            },
            onDismiss = { viewModel.closeLanguageDialog() }
        )
    }

    if (uiState.showExplainDialog) {
        ExplainTopicDialog(
            currentSubject = uiState.selectedSubject,
            selectedLanguage = uiState.selectedLanguage,
            onDismiss = { viewModel.closeExplainDialog() },
            onSubmit = { topic, depth ->
                viewModel.submitExplainTopic(topic, depth)
            }
        )
    }

    if (uiState.showMakeQuizDialog) {
        MakeQuizDialog(
            currentSubject = uiState.selectedSubject,
            selectedLanguage = uiState.selectedLanguage,
            onDismiss = { viewModel.closeMakeQuizDialog() },
            onSubmit = { customTopic ->
                viewModel.submitMakeQuiz(customTopic)
            }
        )
    }

    if (uiState.showClearConfirmDialog) {
        ClearConfirmDialog(
            onDismiss = { viewModel.closeClearConfirmDialog() },
            onConfirm = { viewModel.confirmClearChat() },
            selectedLanguage = uiState.selectedLanguage
        )
    }
}
