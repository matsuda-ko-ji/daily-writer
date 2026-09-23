package com.example.dailywriter.client;

/**
 * AI文章生成処理の共通インターフェース
 */
public interface AiTextGenerator {

    /**
     * プロンプトをもとに文章生成APIのレスポンスを返す
     *
     * @param prompt 生成に使用するプロンプト
     * @return 文章生成結果
     */
    String generate(String prompt);
}