package dev.tehin.tlib.api.command;

import java.util.List;

public interface TabCompleterBase {

    List<String> complete(String[] args);

}
