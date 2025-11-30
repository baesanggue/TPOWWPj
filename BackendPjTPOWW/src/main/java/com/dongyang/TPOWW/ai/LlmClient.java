package com.dongyang.TPOWW.ai;

import java.io.IOException;

public interface LlmClient {
	/**
     * LLM에 프롬프트를 보내고 응답 텍스트를 받는다.
     */
    String generate(String prompt) throws IOException;
}
