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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.imanity.addon.chunkanalyzer.manager.ChunkAnalyzerManager;
import org.imanity.addon.chunkanalyzer.util.DateUtil;
import org.imanity.addon.chunkanalyzer.util.item.ItemBuilder;
import org.imanity.addon.chunkanalyzer.util.menu.Button;
import org.imanity.addon.chunkanalyzer.util.menu.pagination.PaginatedMenu;
import org.imanity.imanityspigot.chunk.ChunkAnalyse;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeMenu extends PaginatedMenu {

    private final ChunkAnalyzerManager manager;

    public HomeMenu(ChunkAnalyzerManager manager) {
        this.manager = manager;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatColor.AQUA + "ChunkAnalyzer";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        ChunkAnalyse chunkAnalyse = Bukkit.imanity().getChunkAnalyse();

        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.INK_SACK)
                        .name(ChatColor.GREEN + "Start ChunkAnalyzer Record")
                        .lore(" ", "&7&oLast analyze has been started at: " + DateUtil.getDateFormat(manager.getStartedAnalyzerRecordTime()))
                        .data(10)
                        .shiny()
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                if (chunkAnalyse.isRecording()) {
                    player.sendMessage(ChatColor.RED + "The ChunkAnalyzer is already started!");
                } else {
                    chunkAnalyse.start();
                    player.sendMessage(ChatColor.GREEN + "You have successfully started the ChunkAnalyzer.");
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
                return new ItemBuilder(Material.BOOK)
                        .name(ChatColor.AQUA + "&lInformations / Help")
                        .lore(
                                "&7&m*------------------------------*",
                                "&7&l• &3&oHow does this addon work, how to use it?",
                                " ",
                                "&7&l» &bThis plugin is using the API of",
                                "&biSpigot3 about ChunkAnalyzer system.",
                                "&7&l» &bIt give you report easily",
                                "&bconfigurables everything via amazing menus.",
                                "&7&l» &bThis is very efficient and simple",
                                "&bto track laggy chunks on your server.",
                                "&7&l» &bFirst, start the analyze thanks to",
                                "&bthe buttons and then click on a world, let's go!",
                                " ",
                                "&3&oStill need help? &b&ldiscord.gg/GBZKR3n",
                                "&7&m*------------------------------*"
                            )
                        .shiny()
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.sendMessage(ChatColor.AQUA + "Imanity Software discord support:§o https://discord.gg/GBZKR3n");
            }
        });
        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.INK_SACK)
                        .name(ChatColor.RED + "End ChunkAnalyzer Record")
                        .lore(" ", "&7&oLast analyze has been ended at: " + DateUtil.getDateFormat(manager.getEndedAnalyzerRecordTime()))
                        .data(1)
                        .shiny()
                        .build();
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                if (!chunkAnalyse.isRecording()) {
                    player.sendMessage(ChatColor.RED + "The ChunkAnalyzer is not started!");
                } else {
                    chunkAnalyse.stop();
                    player.sendMessage(ChatColor.GREEN + "You have successfully ended the ChunkAnalyzer.");
                }
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        for (int i = 9; i < 18; i++) {
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .data(3)
                            .name(" ")
                            .build();
                }
            });
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Bukkit.getWorlds().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(w -> w.getPlayers().size())))
                .collect(Collectors.toList()).forEach(world -> buttons.put(buttons.size() + 9, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        ItemBuilder builder = new ItemBuilder(getMaterialByEnvironment(world.getEnvironment()))
                                .name("&7&l• " + getChatColorByEnvironment(world.getEnvironment()) + world.getName() + " &7&l•");

                        if (player.getWorld() == world) {
                            builder.lore(" ", "&7&oThis is the world you are actually in");
                            builder.shiny();
                        }
                        return builder.build();
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        WorldMenu worldMenu = manager.getMenuByWorld(world);

                        if (worldMenu == null) { // Not supposed to happen but just in case
                            player.sendMessage(ChatColor.RED + "Something wrong happen... Please check the console and report this error.");
                        } else {
                            worldMenu.openMenu(player);
                        }
                    }
                }));
        return buttons;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 27;
    }

    private Material getMaterialByEnvironment(World.Environment environment) { // Better to use set k,v?
        switch (environment) {
            case NETHER:
                return Material.NETHERRACK;
            case THE_END:
                return Material.ENDER_STONE;
            default:
                return Material.GRASS;
        }
    }

    private ChatColor getChatColorByEnvironment(World.Environment environment) { // Better to use set k,v?
        switch (environment) {
            case NETHER:
                return ChatColor.RED;
            case THE_END:
                return ChatColor.DARK_PURPLE;
            default:
                return ChatColor.DARK_GREEN;
        }
    }
}
