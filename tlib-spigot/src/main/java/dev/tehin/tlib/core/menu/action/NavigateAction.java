package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.NavigationAction;
import dev.tehin.tlib.api.menu.manager.MenuManager;
import dev.tehin.tlib.api.tLib;
import dev.tehin.tlib.core.menu.Menu;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@Getter
public class NavigateAction extends CraftMenuAction implements NavigationAction {

    private final Class<? extends Menu> navigation;

    public NavigateAction(Class<? extends Menu> navigation) {
        super(ClickType.LEFT, null);

        this.navigation = navigation;
    }

    @Override
    public void execute(MenuManager manager, Player player) {
        tLib.get().getMenu().open(player, navigation);
    }

    @Override
    public boolean equals(MenuAction equals) {
        if (!(equals instanceof NavigateAction)) return false;
        return ((NavigateAction) equals).getNavigation() == this.navigation;
    }

}
