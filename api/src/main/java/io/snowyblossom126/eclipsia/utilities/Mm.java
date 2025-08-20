package io.snowyblossom126.eclipsia.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MiniMessage(Minecraft 텍스트 포맷)를 편리하게 처리하기 위한 유틸리티 클래스
 */
public class Mm {

    /**
     * 문자열을 MiniMessage 컴포넌트로 변환
     *
     * @param input MiniMessage 문자열
     * @return 변환된 Component
     */
    public static Component mm(String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

    /**
     * 문자열 리스트를 MiniMessage 컴포넌트 리스트로 변환
     *
     * @param input 문자열 리스트
     * @return 변환된 Component 리스트 (null 입력 시 빈 리스트 반환)
     */
    public static List<Component> mm(List<String> input) {
        if (input == null) {
            return new ArrayList<>();
        }
        return input.stream().map(MiniMessage.miniMessage()::deserialize).collect(Collectors.toList());
    }
}
