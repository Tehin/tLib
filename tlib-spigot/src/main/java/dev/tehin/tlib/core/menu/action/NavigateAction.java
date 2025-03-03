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

    private final Menu navigation;

    public NavigateAction(Class<? extends Menu> navigation) {
        this(tLib.get().getMenu().getMenu(navigation));
    }

    public NavigateAction(Menu navigation) {
        super(ClickType.LEFT, null);

        this.navigation = navigation;
    }

    @Override
    public void execute(MenuManager manager, Player player) {
        navigation.open(player);
    }

    @Override
    public boolean equals(MenuAction equals) {
        if (!(equals instanceof NavigateAction)) return false;

        return ((NavigateAction) equals).getNavigation().equals(this.navigation);
    }

}
