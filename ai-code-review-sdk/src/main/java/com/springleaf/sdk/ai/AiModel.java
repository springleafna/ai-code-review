package com.springleaf.sdk.ai;

import com.springleaf.sdk.domain.dto.ChatCompletionRequestDTO;
import com.springleaf.sdk.domain.dto.ChatCompletionSyncResponseDTO;

public interface AiModel {
    ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception;

    String getModel();
}
