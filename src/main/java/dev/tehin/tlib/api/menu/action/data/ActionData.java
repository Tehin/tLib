package dev.tehin.tlib.api.menu.action.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ActionData {
    private final String name;
    private final List<String> lore;
}