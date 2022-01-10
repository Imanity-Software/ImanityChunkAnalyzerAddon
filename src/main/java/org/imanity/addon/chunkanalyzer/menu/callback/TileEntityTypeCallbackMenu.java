/*
 * MIT License
 *
 * Copyright (c) 2021 Imanity Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.imanity.addon.chunkanalyzer.menu.callback;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.imanity.addon.chunkanalyzer.menu.WorldMenu;
import org.imanity.addon.chunkanalyzer.util.TypeCallback;
import org.imanity.addon.chunkanalyzer.util.item.ItemBuilder;
import org.imanity.addon.chunkanalyzer.util.menu.Button;
import org.imanity.addon.chunkanalyzer.util.menu.Menu;
import org.imanity.imanityspigot.chunk.ChunkAnalyse;

import java.util.HashMap;
import java.util.Map;

public class TileEntityTypeCallbackMenu extends Menu {

    private final WorldMenu worldMenu;
    private final TypeCallback<ChunkAnalyse.TileEntityType> response;

    public TileEntityTypeCallbackMenu(WorldMenu worldMenu, TypeCallback<ChunkAnalyse.TileEntityType> response) {
        this.worldMenu = worldMenu;
        this.response = response;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.AQUA + "Choose one TileEntityType";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (ChunkAnalyse.TileEntityType tileEntityType : ChunkAnalyse.TileEntityType.values()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.CHEST)
                            .name(ChatColor.AQUA + tileEntityType.name())
                            .build();
                }
                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    response.callback(tileEntityType);
                    worldMenu.openMenu(player);
                }
            });
        }
        return buttons;
    }
}
