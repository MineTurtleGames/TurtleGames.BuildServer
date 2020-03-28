package co.turtlegames.build.map.menu;

import co.turtlegames.build.map.MapInstance;
import co.turtlegames.core.menu.Page;
import co.turtlegames.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MapRootPage extends Page<MapManageMenu> {

    private MapInstance _mapInstance;

    public MapRootPage(MapManageMenu menu, MapInstance mapInstance) {
        super(menu, 3);

        _mapInstance = mapInstance;

        ItemBuilder infoItem = new ItemBuilder(Material.PAPER, ChatColor.GREEN + ChatColor.BOLD.toString() + "Map Info");
        infoItem.setLore("",
                ChatColor.GRAY + "ID: " + ChatColor.YELLOW + mapInstance.getId(),
                ChatColor.GRAY + "Name: " + ChatColor.YELLOW + mapInstance.getName(),
                ChatColor.GRAY + "Author: " + ChatColor.YELLOW + mapInstance.getAuthor(),
                "",
                ChatColor.GRAY + "Description: ");
        infoItem.addLore(Arrays.stream(mapInstance.getDescription())
                                    .map((String text) -> ChatColor.YELLOW + text)
                                    .collect(Collectors.toList()));

        this.addButton(10, infoItem.build());

    }

}
