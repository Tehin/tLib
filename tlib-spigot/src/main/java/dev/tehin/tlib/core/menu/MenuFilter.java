package dev.tehin.tlib.core.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MenuFilter {

    ALL("Todos"),
    AVAILABLE("Disponibles"),
    UNAVAILABLE("Bloqueados");

    private final String display;

}
