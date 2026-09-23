package com.example.dailywriter.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class AiTextGeneratorConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * AiTextGeneratorの実装がモックだけであること
     */
    @Test
    void onlyMockAiTextGeneratorIsRegistered() {

        // AiTextGenerator型のBeanをすべて取得
        String[] beanNames = applicationContext
                .getBeanNamesForType(AiTextGenerator.class);

        // Beanが1つだけ登録されていること
        assertEquals(1, beanNames.length);

        // 登録されたBeanの実装クラスを確認
        AiTextGenerator generator = applicationContext
                .getBean(AiTextGenerator.class);

        assertInstanceOf(
                MockAiTextGenerator.class,
                generator
        );

        // AiClient型のBeanが存在しないこと
        String[] aiClientBeanNames = applicationContext
                .getBeanNamesForType(AiClient.class);

        assertEquals(0, aiClientBeanNames.length);
    }

    /**
     * AiMessageServiceがSpringから取得できること
     */
    @Test
    void aiMessageServiceIsRegistered() {

        assertTrue(
                applicationContext.containsBean("aiMessageService")
        );
    }
}