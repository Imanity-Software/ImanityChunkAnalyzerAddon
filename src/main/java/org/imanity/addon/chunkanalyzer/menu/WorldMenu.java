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

package org.imanity.addon.chunkanalyzer.menu;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.imanity.addon.chunkanalyzer.util.item.ItemBuilder;
import org.imanity.addon.chunkanalyzer.util.menu.Button;
import org.imanity.addon.chunkanalyzer.util.menu.buttons.BackButton;
import org.imanity.addon.chunkanalyzer.util.menu.pagination.PaginatedMenu;
import org.imanity.imanityspigot.chunk.ChunkAnalyse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WorldMenu extends PaginatedMenu {

    private final World world;

    private ChunkAnalyse.SortTarget sortTarget;
    private ChunkAnalyse.SortMethod sortMethod;

    private ChunkAnalyse.WorldAnalysesExport worldAnalysesExport;

    //private EntityType entityType;
    //private ChunkAnalyse.TileEntityType tileEntityType;

    public WorldMenu(World world) {
        this.world = world;

        this.sortTarget = ChunkAnalyse.SortTarget.ALL;
        this.sortMethod = ChunkAnalyse.SortMethod.BY_AVG;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatColor.BLUE + "World " + this.world.getName();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.TNT)
                        .name(ChatColor.DARK_BLUE + "Change Sort Target")
                        .lore("&7&l• &b" + sortTarget.name())
                        .shiny()
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                switch (sortTarget) {
                    case ALL:
                        sortTarget = ChunkAnalyse.SortTarget.ENTITIES;
                        break;
                    case ENTITIES:
                        sortTarget = ChunkAnalyse.SortTarget.BLOCK_OPERATION;
                        break;
                    case BLOCK_OPERATION:
                        sortTarget = ChunkAnalyse.SortTarget.TILE_ENTITIES;
                        break;
                    case TILE_ENTITIES:
                        sortTarget = ChunkAnalyse.SortTarget.ALL;
                }
            }
            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.SIGN)
                        .name(ChatColor.DARK_BLUE + "Change Sort Method")
                        .lore("&7&l• &b" + sortMethod.name())
                        .shiny()
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                switch (sortMethod) {
                    case BY_AVG:
                        sortMethod = ChunkAnalyse.SortMethod.BY_MAX;
                        break;
                    case BY_MAX:
                        sortMethod = ChunkAnalyse.SortMethod.BY_TOTAL;
                        break;
                    case BY_TOTAL:
                        sortMethod = ChunkAnalyse.SortMethod.BY_AVG;
                }
            }
            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.COMPASS)
                        .name(ChatColor.GREEN + "Start Chunk Analyze")
                        .shiny()
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                ChunkAnalyse chunkAnalyse = Bukkit.imanity().getChunkAnalyse();

                if (!chunkAnalyse.hasRecorded()) {
                    player.sendMessage(ChatColor.RED + "You have to record a ChunkAnalyzer before trying to export an analyze!");
                } else {
                    worldAnalysesExport = Bukkit.imanity().getChunkAnalyse().getAnalyseExport(world, sortTarget, sortMethod);
                }
            }
            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        for (int i = 9; i < 18; i++) {
            buttons.put(i, new BackButton(new HomeMenu()));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (this.worldAnalysesExport == null) {
            return buttons;
        }
        AtomicInteger count = new AtomicInteger(0);

        this.worldAnalysesExport.getChunks().forEach(chunk -> buttons.put(buttons.size() + 9, new Button() {
            private final Location location = new Location(world, chunk.getX() << 4, player.getLocation().getY(), chunk.getZ() << 4);

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.NAME_TAG)
                        .name("&7&o[#" + count.addAndGet(1) + "] &3&l" + this.location.getX() + ", " + this.location.getZ())
                        .lore
                                (
                                        "&7&m*------------------------------*",
                                        "&f&l» &bTotal: &f" + chunk.getTotal().getTotal(),
                                        "&f&l» &bAVG: &f" + chunk.getTotal().getAvg(),
                                        "&f&l» &bMax: &f" + chunk.getTotal().getMax(),
                                        "&f&l» &bCount: &f" + chunk.getTotal().getCount(),
                                        "&7&m*------------------------------*"
                                )
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                if (player.teleport(this.location)) {
                    player.sendMessage(ChatColor.GREEN + "You have been successfully teleported to the chunk. §7§o(" + this.location + ")");
                } else {
                    player.sendMessage(ChatColor.RED + "Oops! Something wrong happened while trying to teleport you the chunk. §7§o(" + this.location + ")");
                }
            }
        }));
        return buttons;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 45;
    }
}