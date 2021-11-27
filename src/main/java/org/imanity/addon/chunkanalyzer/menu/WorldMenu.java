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
import org.imanity.addon.chunkanalyzer.data.ChunkAnalyzeResult;
import org.imanity.addon.chunkanalyzer.manager.ChunkAnalyzerManager;
import org.imanity.addon.chunkanalyzer.util.item.ItemBuilder;
import org.imanity.addon.chunkanalyzer.util.menu.Button;
import org.imanity.addon.chunkanalyzer.util.menu.buttons.BackButton;
import org.imanity.addon.chunkanalyzer.util.menu.pagination.PaginatedMenu;
import org.imanity.imanityspigot.chunk.ChunkAnalyse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WorldMenu extends PaginatedMenu {

    private final ChunkAnalyzerManager manager;
    private final World world;

    private ChunkAnalyse.SortTarget sortTarget;
    private ChunkAnalyse.SortMethod sortMethod;

    private ChunkAnalyzeResult lastChunkAnalyzeResult;

    private final static Map<ChunkAnalyse.SortTarget, String> SORT_TARGET_DISPLAY_NAME = new HashMap<>();
    private final static Map<ChunkAnalyse.SortMethod, String> SORT_METHOD_DISPLAY_NAME = new HashMap<>();
    private final static NumberFormat FORMATTER = new DecimalFormat("#0.000");

    static {
        SORT_TARGET_DISPLAY_NAME.put(ChunkAnalyse.SortTarget.ALL, "All");
        SORT_TARGET_DISPLAY_NAME.put(ChunkAnalyse.SortTarget.BLOCK_OPERATION, "Block Operation");
        SORT_TARGET_DISPLAY_NAME.put(ChunkAnalyse.SortTarget.ENTITIES, "Entities");
        SORT_TARGET_DISPLAY_NAME.put(ChunkAnalyse.SortTarget.TILE_ENTITIES, "Tile Entities");

        SORT_METHOD_DISPLAY_NAME.put(ChunkAnalyse.SortMethod.BY_TOTAL, "By Total");
        SORT_METHOD_DISPLAY_NAME.put(ChunkAnalyse.SortMethod.BY_AVG, "By Average");
        SORT_METHOD_DISPLAY_NAME.put(ChunkAnalyse.SortMethod.BY_MAX, "By Maximum");
    }

    public WorldMenu(ChunkAnalyzerManager manager, World world) {
        this.manager = manager;
        this.world = world;

        this.sortTarget = ChunkAnalyse.SortTarget.ALL;
        this.sortMethod = ChunkAnalyse.SortMethod.BY_TOTAL;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatColor.BLUE + "World: " + this.world.getName();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.TNT)
                        .name(ChatColor.AQUA + "Change Sort Target")
                        .lore(" ")
                        .lore(
                                Arrays.stream(ChunkAnalyse.SortTarget.values())
                                        .map(value -> (value == sortTarget ? "&7&l• &a" : "&c") + SORT_TARGET_DISPLAY_NAME.get(value))
                                        .collect(Collectors.toList()))
                        .shiny()
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                ChunkAnalyse.SortTarget[] values = ChunkAnalyse.SortTarget.values();

                sortTarget = values[(sortTarget.ordinal() + 1) % values.length];
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
                        .name(ChatColor.AQUA + "Change Sort Method")
                        .lore(" ")
                        .lore(
                                Arrays.stream(ChunkAnalyse.SortMethod.values())
                                        .map(value -> (value == sortMethod ? "&7&l• &a" : "&c") + SORT_METHOD_DISPLAY_NAME.get(value))
                                        .collect(Collectors.toList()))
                        .shiny()
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                ChunkAnalyse.SortMethod[] values = ChunkAnalyse.SortMethod.values();

                sortMethod = values[(sortMethod.ordinal() + 1) % values.length];
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
                    lastChunkAnalyzeResult = new ChunkAnalyzeResult(sortTarget, sortMethod, world);
                }
            }
            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        for (int i = 9; i < 18; i++) {
            buttons.put(i, new BackButton(new HomeMenu(this.manager)));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (this.lastChunkAnalyzeResult == null) {
            for (int i = 0; i < 9; i++) {
                buttons.put(buttons.size() + 9, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.PAPER)
                                .name(ChatColor.RED + "&oStart the analyze to get a chunk report!")
                                .build();
                    }
                });
            }
            return buttons;
        }
        AtomicInteger count = new AtomicInteger(0);

        this.lastChunkAnalyzeResult.getExport().getChunks().forEach(chunk -> buttons.put(buttons.size() + 9, new Button() {
            private final Location location = new Location(world, chunk.getX() << 4, player.getLocation().getY(), chunk.getZ() << 4);

            @Override
            public ItemStack getButtonItem(Player player) {
                ChunkAnalyzeResult.WarningType warningType = lastChunkAnalyzeResult.getWarningType(chunk);

                return new ItemBuilder(warningType.getIcon())
                        .name("&7&o[#" + count.addAndGet(1) + "] &3&l" + this.location.getX() + ", " + this.location.getZ())
                        .lore
                                (
                                        "&7&m*------------------------------*",
                                        "&f&l» &bTotal: &f" + FORMATTER.format(chunk.getTotal().getTotal()) + "ms",
                                        "&f&l» &bAverage: " + warningType.getColor() + FORMATTER.format(chunk.getTotal().getAvg()) + "ms",
                                        "&f&l» &bMaximum: &f" + FORMATTER.format(chunk.getTotal().getMax()) + "ms",
                                        "&f&l» &bCount: &f" + chunk.getTotal().getCount() + "x",
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
