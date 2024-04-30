package dev.tehin.tlib.core.menu.action;

import dev.tehin.tlib.api.menu.MenuType;
import dev.tehin.tlib.api.menu.action.MenuAction;
import dev.tehin.tlib.api.menu.action.NavigationAction;
import dev.tehin.tlib.api.tLib;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@Getter
public class CraftNavigationAction extends CraftMenuAction implements NavigationAction {

    private final MenuType navigation;

    public CraftNavigationAction(ClickType type, MenuType navigation) {
        super(type, null);

        this.navigation = navigation;
    }

    @Override
    public void execute(Player player) {
        player.closeInventory();

        tLib.get().getMenu().open(player, navigation);
    }

    @Override
    public boolean equals(MenuAction equals) {
        if (!(equals instanceof CraftNavigationAction)) return false;
        return ((CraftNavigationAction) equals).getNavigation() == this.navigation;
    }
}
